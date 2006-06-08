/****************************************************************************
** Copyright (c) quickfixengine.org  All rights reserved.
**
** This file is part of the QuickFIX FIX Engine
**
** This file may be distributed under the terms of the quickfixengine.org
** license as defined by quickfixengine.org and appearing in the file
** LICENSE included in the packaging of this file.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
** See http://www.quickfixengine.org/LICENSE for licensing information.
**
** Contact ask@quickfixengine.org if any conditions of this licensing are
** not clear to you.
**
****************************************************************************/

package quickfix.logviewer;
import quickfix.*;
import quickfix.field.SendingTime;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogFile {
	public final static int TYPE_UNKNOWN = 0;
	public final static int TYPE_PLAIN = 1;
	public final static int TYPE_B4B = 2;

	private File logFile = null;
	private FileReader logFileReader = null;
	private BufferedReader bufferedLogFileReader = null;
	private DataDictionary dataDictionary = null;
	private ArrayList messages = new ArrayList();
	private ArrayList invalidMessages = new ArrayList();
	private ArrayList newMessages = new ArrayList();
	private ArrayList newInvalidMessages = new ArrayList();
	private int lastPosition = 0;
	private int type = TYPE_UNKNOWN;
	private char delimiter = 0;
		
	public LogFile( String name, DataDictionary aDataDictionary ) throws FileNotFoundException {
		logFile = new File(name);
		dataDictionary = aDataDictionary;
		initialize();
	}
	
	public LogFile( File file, DataDictionary aDataDictionary ) throws FileNotFoundException {
		logFile = file;
		dataDictionary = aDataDictionary;
		initialize();
	}
	
	public int getType() {
		return type;
	}
	
	public char getDelimiter() {
		return delimiter;
	}
	
	public ArrayList getMessages() {
		return messages;
	}
	
	public ArrayList getInvalidMessages() {
		return invalidMessages;
	}
	
	private void initialize() throws FileNotFoundException {
		try {
			if( logFileReader != null )
				logFileReader.close();
			logFileReader = new FileReader( logFile );
			bufferedLogFileReader = new BufferedReader( logFileReader );
			bufferedLogFileReader.mark(0);
		} catch( IOException ioe ) {
		}
		if( type == TYPE_UNKNOWN )
			type = determineType();
	}

	public int determineType() {
		try {
			bufferedLogFileReader.reset();
			bufferedLogFileReader.readLine();
			String line = bufferedLogFileReader.readLine();
			if( line != null && line.matches(".*B4B FIX Engine.*") ) {
				return TYPE_B4B;
			}
		} catch( IOException ioe ) {
		}
		return TYPE_PLAIN;
	}
	
	public static boolean skipLine( String line, int type ) {
		if( type == TYPE_B4B ) {
			return line.matches("\\d{8}-\\d{2}:\\d{2}:\\d{2} Driver.*");
		}
		return false;
	}
	
	public static char getDelimiter( String line ) {
		if( line.length() < 10 )
			return 0;
		return line.charAt(9);
	}
	
	public ArrayList parseMessages( ProgressBarPanel progressBar, int startingPosition, int endingPosition ) throws IOException, CancelException {
		initialize();
		messages = new ArrayList();
		invalidMessages = new ArrayList();
		newMessages = new ArrayList();
		newInvalidMessages = new ArrayList();
		String line = null;
		lastPosition = endingPosition;
		
		if( progressBar != null )
			progressBar.setTask( "Parsing FIX Messages", startingPosition, endingPosition, true );

		initialize();
		bufferedLogFileReader.skip( startingPosition );
		int bytesRead = 0;
		int totalBytesRead = 0;
		int totalBytes = endingPosition - startingPosition;
		
		while( (line = bufferedLogFileReader.readLine()) != null && totalBytesRead < totalBytes ) {
			bytesRead += line.length();
			totalBytesRead += line.length();
			
			if( bytesRead > 30000 ) {
				if( progressBar != null ) {
					if( !progressBar.increment( bytesRead ) ) {
						messages.clear();
						invalidMessages.clear();
						throw new CancelException();
					}
				}
				bytesRead = 0;
			}

			Message message = parseLine( line, dataDictionary );
			if( message == null )
				continue;
			messages.add( message );
		}
		
		if( progressBar != null )
			progressBar.done();
		messages.addAll( newMessages );
		invalidMessages.addAll( newInvalidMessages );
		return messages;
	}
	
	public ArrayList parseMessages( ProgressBarPanel progressBar, Date startTime, Date endTime ) throws IOException, CancelException {
		int startingPosition = findPositionByTime( progressBar, startTime, 0, true );
		int endingPosition = (int)logFile.length();
		if( endTime != null )
			endingPosition = findPositionByTime( progressBar, endTime, startingPosition, false );

		ArrayList messages = parseMessages( progressBar, startingPosition, endingPosition );
		return trimMessages( messages, startTime, endTime );
	}
	
	public ArrayList parseNewMessages( ProgressBarPanel progressBar ) throws IOException, CancelException {
		int startingPosition = lastPosition;
		int endingPosition = (int)logFile.length();
		
		ArrayList messages = parseMessages( progressBar, startingPosition, endingPosition );
		return messages;
	}
	
	private ArrayList trimMessages( ArrayList messages, Date startTime, Date endTime ) {
		SendingTime sendingTime = new SendingTime();
		
		while( startTime != null && messages.size() > 0 ) {
			Message message = (Message)messages.get(0);
			try {
				message.getHeader().getField( sendingTime );
				if( startTime.compareTo(sendingTime.getValue()) > 0 ) {
					messages.remove(0);
				} else {
					break;
				}
			} catch (FieldNotFound e) {
				messages.remove(0);
				continue;
			}
		}

		while( endTime != null && messages.size() > 0 ) {
			Message message = (Message)messages.get(messages.size() - 1);
			try {
				message.getHeader().getField( sendingTime );
				if( endTime.compareTo(sendingTime.getValue()) < 0 ) {
					messages.remove(messages.size() - 1);
				} else {
					break;
				}
			} catch (FieldNotFound e) {
				messages.remove(messages.size() - 1);
				continue;
			}
		}
		return messages;
	}
	
	private int findPositionByTime( ProgressBarPanel progressBar, Date time, int position, boolean before ) throws CancelException {
		String line = null;
		int bytesRead = 0;
		int mark = position;

		if( progressBar != null ) {
			progressBar.setTask( "Scanning Dates", 0, lastPosition = (int)logFile.length(), true );
			progressBar.setValue( position );
		}

		try {
			if( position == 0 )
				initialize();
			SendingTime sendingTime = new SendingTime();

			int count = 0;
			boolean lastMessageWasBad = false;
			
			if( time != null ) {
				while( (line = bufferedLogFileReader.readLine()) != null ) {
					bytesRead += line.length();
					if( (++count % 1000) == 0 || lastMessageWasBad ) {
						lastMessageWasBad = false;
						Message message = parseLine( line, dataDictionary );
						try {
							if( message == null ) {
								lastMessageWasBad = true;
								continue;
							}
							message.getHeader().getField( sendingTime );
						} catch (FieldNotFound e) {
							lastMessageWasBad = true;
						}
	
						if( time.compareTo(sendingTime.getValue()) < 0 ) {
							break;
						}
						
						mark += bytesRead;
						if( progressBar != null )
							if(!progressBar.increment( bytesRead ))
								throw new CancelException();
						if( bytesRead > 30000 )
							bytesRead = 0;						
					}
				}
			}
		} catch (IOException e) {
		}
		if( progressBar != null )
			progressBar.done();
		return before ? mark : mark + bytesRead;
	}

	private Message parseLine( String line, DataDictionary dataDictionary ) {
		if( skipLine(line, type) ) {
			return null;
		}
		
		try {
			String[] split = line.split("8=",2);
			if( split.length != 2 )
				return null;
			line = "8=" + split[1];
			
			if( delimiter == 0 )
				delimiter = getDelimiter(line);
			String regexp = "[" + delimiter + "]";
			if( delimiter == '^' ) regexp = "[\\^].";
			if( delimiter != '\001' && delimiter != 0 )
				line = line.replaceAll(regexp, "\001");
			int lastChar = line.lastIndexOf('\001');
			if( lastChar != line.length() )
				line = line.substring(0, lastChar+1);
			if( dataDictionary == null )
				return new Message( line, false );
			else
				return new Message( line, dataDictionary, false );
		} catch( quickfix.InvalidMessage e1 ) {
			return null;
		} catch( ArrayIndexOutOfBoundsException e2 ) {
			return null;
		}
	}
	
	public Date getStartTime() {
		RandomAccessFile randomFile;
		try {
			randomFile = new RandomAccessFile( logFile, "r" );
			int length = (int)(randomFile.length() > 30000 ? 30000 : randomFile.length());
			byte[] bytes = new byte[length];
			randomFile.readFully( bytes );
			String string = new String( bytes );
			int index = string.indexOf("52=");
			if( index == -1 ) return null;
			String value = string.substring(index + 3, index + 20) + ", GMT";
			SimpleDateFormat format = new SimpleDateFormat( "yyyyMMdd-HH:mm:ss, z");
			return format.parse( value );
 		} catch (Exception e) {
 			return null;
		}	
 	}
	
	public Date getEndTime() {
		RandomAccessFile randomFile;
		try {
			randomFile = new RandomAccessFile( logFile, "r" );
			int length = (int)(randomFile.length() > 30000 ? 30000 : randomFile.length());
			randomFile.seek( randomFile.length() - length );
			byte[] bytes = new byte[length];
			randomFile.readFully( bytes );
			String string = new String( bytes );
			int index = string.lastIndexOf("52=");
			if( index == -1 ) return null;
			String value = string.substring(index + 3, index + 20) + ", GMT";
			SimpleDateFormat format = new SimpleDateFormat( "yyyyMMdd-HH:mm:ss, z");
			return format.parse( value );
 		} catch (Exception e) {
 			return null;
		}			
	}	
}
