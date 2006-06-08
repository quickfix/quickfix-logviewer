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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

import quickfix.*;
import quickfix.field.MsgType;

import javax.swing.table.AbstractTableModel;

public class MessagesTableModel extends AbstractTableModel {
	private HashSet tags = new HashSet();
	private HashSet headerTags = new HashSet();
	private HashSet bodyTags = new HashSet();
	private HashSet trailerTags = new HashSet();
	private HashMap colToTag = new HashMap();
	private ArrayList messages = null;
	private ArrayList allMessages = new ArrayList();
	private ArrayList adminMessages = new ArrayList();
	private ArrayList appMessages = new ArrayList();
	private ArrayList filterMessages = new ArrayList();
	private ArrayList categoryMessages = new ArrayList();
	private ArrayList filter = new ArrayList();
	private String[] category = null;
	private DataDictionary dataDictionary = null;
	private LogFile logFile = null;
		
	public MessagesTableModel( DataDictionary aDataDictionary ) {
		dataDictionary = aDataDictionary;
		messages = allMessages;
	}
	
	public MessagesTableModel( DataDictionary aDataDictionary, LogFile aLogFile ) {
		dataDictionary = aDataDictionary;
		logFile = aLogFile;
		messages = allMessages;
	}
		
	public int getFilterType() {
		if( messages == allMessages )
			return FilterType.ALL_MESSAGES;
		else if( messages == appMessages )
			return FilterType.APPLICATION_MESSAGES;
		else if( messages == adminMessages )
			return FilterType.ADMINISTRATIVE_MESSAGES;
		else if( messages == filterMessages )
			return FilterType.CUSTOM_FILTER;
		else if( messages == categoryMessages )
			return Category.categoryToFilterType( category );
		else
			return -1;
	}
	
	public LogFile getLogFile() {
		return logFile;
	}

	private void clear() {
		adminMessages.clear();
		appMessages.clear();
		filterMessages.clear();
		categoryMessages.clear();
		allMessages = null;

		tags.clear();
		headerTags.clear();
		bodyTags.clear();
		trailerTags.clear();
		colToTag.clear();
	}
	
	public void setMessages( ArrayList aMessages, ProgressBarPanel progressBar ) throws CancelException {
		clear();
		allMessages = aMessages;
		setMessages();
		indexMessages( allMessages, progressBar );
		fireTableStructureChanged();
	}
	
	public void addMessages( ArrayList aMessages, ProgressBarPanel progressBar ) throws CancelException {
		if( aMessages != null && messages.size() != 0 ) {
			allMessages.addAll( aMessages );
			boolean newTags = indexMessages( aMessages, progressBar );
			
			if( messages == filterMessages ) {
				filter( filter, aMessages );
			}
			
			if( newTags ) {
				fireTableStructureChanged();
			} else {
				fireTableDataChanged();
			}
		}
	}

	private boolean indexMessages( ArrayList messages, ProgressBarPanel progressBar ) throws CancelException {
		if( progressBar != null ) {
			progressBar.setMaximum(messages.size());
			progressBar.setTask( "Indexing Messages", 0, messages.size(), tags.size() == 0 );
		}
		
		int messageCount = 0;
		Iterator i = messages.iterator();
		while( i.hasNext() ) {
			Message message = (Message)i.next();
			if( message.isAdmin() ) {
				adminMessages.add(message);
			} else if( message.isApp() ) {
				appMessages.add(message);
			}
			
			fillTagSet( message.getHeader(), headerTags );
			fillTagSet( message, bodyTags );
			fillTagSet( message.getTrailer(), trailerTags );
			if( progressBar != null && (messageCount++ % 100) == 0 ) {
				if( !progressBar.increment(100) ) {
					throw new CancelException();
				}
			}
		}
		if( progressBar != null )
			progressBar.done();
		
		int count = colToTag.size();
		Integer tag = null;
		i = headerTags.iterator();
		while( i.hasNext() ) {
			tag = (Integer)i.next();
			if( tags.contains(tag) ) continue;
			colToTag.put(new Integer(count), tag);
			count++;
		}
		i = bodyTags.iterator();
		while( i.hasNext() ) {
			tag = (Integer)i.next();
			if( tags.contains(tag) ) continue;
			colToTag.put(new Integer(count), tag);
			count++;		
		}
		i = trailerTags.iterator();
		while( i.hasNext() ) {
			tag = (Integer)i.next();
			if( tags.contains(tag) ) continue;
			colToTag.put(new Integer(count), tag);
			count++;
		}
		
		int tagCount = tags.size();
		tags.addAll( headerTags );
		tags.addAll( bodyTags );
		tags.addAll( trailerTags );
		return tagCount != tags.size();
	}
	
	private void fillTagSet( FieldMap fieldMap, HashSet tagSet ) {
		Iterator i = fieldMap.iterator();
		while( i.hasNext() ) {
			StringField field = (StringField)i.next();
			tagSet.add( new Integer(field.getField()) );
		}
	}
	
	public HashSet getTags() {
		return tags;
	}
	
	public Integer getTagFromColumn( int column ) {
		return (Integer)colToTag.get( new Integer(column) );
	}
	
	private void setMessages() {
		if( messages != allMessages
			&& messages != adminMessages
			&& messages != appMessages ) {

			messages = allMessages;
		}
	}
	
	public void viewAll() {
		messages = allMessages;
		fireTableStructureChanged();
	}
	
	public void viewAdministrative() {
		messages = adminMessages;
		fireTableStructureChanged();
	}
	
	public void viewApplication() {
		messages = appMessages;
		fireTableStructureChanged();
	}
		
	public void viewCategory( String[] types ) {
		category = types;
		categoryMessages.clear();
		Iterator i = appMessages.iterator();
		while( i.hasNext() ) {
			Message message = (Message)i.next();
			try {
				String msgType = message.getHeader().getString( MsgType.FIELD );
				for( int j = 0; j < types.length; ++ j ) {
					String type = (String)types[j];
					if( msgType.equals(type) ) {
						categoryMessages.add( message );
						break;
					}
				}
			} catch (FieldNotFound e) {
			}
		}
		
		messages = categoryMessages;
		fireTableStructureChanged();
	}
		
	public void filter( ArrayList fields ) {
		filter( fields, allMessages );
	}
	
	public void filter( ArrayList fields, ArrayList aMessages ) {
		filter = fields;
		if( aMessages == allMessages )
			filterMessages.clear();
		Iterator i = aMessages.iterator();

		while( i.hasNext() ) {
			boolean addMessage = true;
			Message message = (Message)i.next();
			Iterator j = fields.iterator();
			while( j.hasNext() ) {
				FieldFilter fieldFilter = (FieldFilter)j.next();
				int tag = fieldFilter.getTag();
				StringField field = new StringField( tag, "" );
				try {
					if( dataDictionary.isHeaderField(tag) )
						message.getHeader().getField( field );
					else if( dataDictionary.isTrailerField(tag) )
						message.getTrailer().getField( field );
					else
						message.getField( field );

					int compareResults = 0;
					String value1 = field.getValue();
					String value2 = fieldFilter.getValue();
					int fieldType = dataDictionary.getFieldType( tag );
					
					switch( fieldType ) {
					// doubles
					case 3: case 5: case 6: case 14: case 15: case 22:
						try {
							Double doubleValue1 = new Double( Double.parseDouble( value1 ) );
							Double doubleValue2 = new Double( Double.parseDouble( value2 ) );
							compareResults = doubleValue1.compareTo( doubleValue2 );
						} catch( NumberFormatException e ) {
							addMessage = false;
						}
						break;
					// integers
					case 4: case 21: case 23: case 24:
						try {
							Integer integerValue1 = new Integer( Integer.parseInt( value1 ) );
							Integer integerValue2 = new Integer( Integer.parseInt( value2 ) );
							compareResults = integerValue1.compareTo( integerValue2 );
						} catch( NumberFormatException e ) {
							addMessage = false;
						}
						break;
					default:
						compareResults = value1.compareTo( value2 );
					}
					
					if( compareResults != 0 ) {
						String value1Name = dataDictionary.getValueName( tag, value1 );
						if( value1Name != null )
							compareResults = value1Name.toUpperCase().compareTo(value2.toUpperCase());
					}
					
					int operator = fieldFilter.getOperator();
					switch( operator ) {
						case FieldFilter.EQUAL: if( !(compareResults == 0)) addMessage = false; break;
						case FieldFilter.NOT_EQUAL: if( !(compareResults != 0) ) addMessage = false; break;
						case FieldFilter.LESS_THAN: if( !(compareResults < 0)) addMessage = false; break;
						case FieldFilter.LESS_THAN_OR_EQUAL: if( !(compareResults <= 0) ) addMessage = false; break;
						case FieldFilter.GREATER_THAN: if( !(compareResults > 0) ) addMessage = false; break;
						case FieldFilter.GREATER_THAN_OR_EQUAL: if( !(compareResults >= 0)) addMessage = false; break;
					}
				} catch (FieldNotFound e) {
					addMessage = false;
				}
			}
			if( addMessage )
				filterMessages.add( message );
		}
		messages = filterMessages;
		fireTableStructureChanged();
	}
	
	public ArrayList getFilter() {
		return filter;
	}
	
	public int getRowCount() {
		return messages.size();
	}
	 
	public int getColumnCount() {
		return tags.size();
	}

	private String getDisplayString( Message message, int tag, boolean rawValue ) {
		FieldMap map = message;
		if( dataDictionary.isHeaderField(tag) ) {
			map = message.getHeader();
		} else if( dataDictionary.isTrailerField(tag) ) {
			map = message.getTrailer();
		}
		
		if( !map.isSetField(tag) )
			return "";
		
		try {
			StringBuffer value = new StringBuffer();
			if( dataDictionary.hasFieldValue( tag ) ) {
				value.append(dataDictionary.getValueName( tag, map.getString( tag )));
				if( !rawValue )  {
					value.append(" (")
					.append(map.getString( tag ))
					.append(")");
				}
			} else {
				value.append(map.getString( tag ));
			}
			return value.toString();
		} catch( FieldNotFound e ) {
			return "";
		}
	}

	public String getDisplayString(int rowIndex, int columnIndex, boolean rawValue ) {
		Message message = (Message)messages.get( rowIndex );
		Integer tag = (Integer)colToTag.get( new Integer(columnIndex) );
		if( tag == null )
			return "";
		
		return getDisplayString( message, tag.intValue(), rawValue );
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if( rowIndex > getRowCount() || columnIndex > getColumnCount() )
			return "";
		
		Message message = (Message)messages.get( rowIndex );
		Integer tag = (Integer)colToTag.get( new Integer(columnIndex) );
		if( tag == null )
			return "";
		
		return getDisplayString( message, tag.intValue(), false );
	}
	
	public Message getMessageAt(int rowIndex) {
		try {
			return (Message)messages.get( rowIndex );
		} catch( IndexOutOfBoundsException e ) {
			return null;
		}
	}
	
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}
	
	public String getColumnName(int column) {
		Integer tag = (Integer)colToTag.get( new Integer(column) );
		String fieldName = dataDictionary.getFieldName(tag.intValue());
		if( fieldName != null )
			return fieldName + "(" + tag + ")";
		else
			return "(" + tag + ")";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
}
