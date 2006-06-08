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

package quickfix.logviewer.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import quickfix.DataDictionary;
import quickfix.logviewer.LogFile;
import junit.framework.TestCase;

public class LogFileTestCase extends TestCase {
	private DataDictionary dataDictionary = null;
	
	public LogFileTestCase() throws quickfix.ConfigError {
		dataDictionary = new DataDictionary("lib/FIX44.xml");
	}
	
	public void testLoadFile() throws Exception {

		LogFile logFile = new LogFile( "test.log", dataDictionary );
		ArrayList messages = logFile.parseMessages( null, null, null );
		assertEquals( 22, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );

		messages = logFile.parseMessages( null, null, null );
		assertEquals( 22, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );
		
		Calendar calendar = new GregorianCalendar( TimeZone.getDefault() );
		calendar.set(2004, 4, 11, 0, 0, 0);
		Date startDate = calendar.getTime();
		messages = logFile.parseMessages( null, startDate, null );
		assertEquals( 12, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );
		
		calendar.set(2004, 4, 15, 0, 0, 0);
		Date endDate = calendar.getTime();
		messages = logFile.parseMessages( null, null, endDate );
		assertEquals( 14, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );
		
		messages = logFile.parseMessages( null, startDate, endDate );
		assertEquals( 4, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );		
	}
	
	public void testSkipLine() {
		assertFalse(LogFile.skipLine("#*@JDUJ@UI", LogFile.TYPE_PLAIN));
		assertFalse(LogFile.skipLine("8=FIX.4.1", LogFile.TYPE_PLAIN));
		
		assertFalse(LogFile.skipLine("20040206-14:47:33 Recv: 8=FIX.4.1", LogFile.TYPE_B4B));
		assertTrue(LogFile.skipLine("20040206-14:47:33 Driver Inbound: 8=FIX.4.1", LogFile.TYPE_B4B));
		assertTrue(LogFile.skipLine("20040206-14:47:33 Driver Outbound: 8=FIX.4.1", LogFile.TYPE_B4B));
		assertFalse(LogFile.skipLine("8=FIX.4.1", LogFile.TYPE_B4B));
	}
	
	public void testGetDelimiter() {
		assertEquals('\001', LogFile.getDelimiter("8=FIX.4.2\001"));
		assertEquals('|', LogFile.getDelimiter("8=FIX.4.2|"));
		assertEquals(0, LogFile.getDelimiter("8=FIX.4.2"));
	}
	
	public void testDetermineType() throws Exception {
		LogFile logFile = new LogFile( "test.log", dataDictionary );
		assertEquals( LogFile.TYPE_PLAIN, logFile.getType() );
		logFile = new LogFile( "testB4B.log", dataDictionary );
		assertEquals( LogFile.TYPE_B4B, logFile.getType() );
	}
	
	public void testDetermineDelimiter() throws Exception {
		LogFile logFile = new LogFile( "test.log", dataDictionary );
		ArrayList messages = logFile.parseMessages( null, null, null );
		assertEquals( 22, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );
		assertEquals( '\001', logFile.getDelimiter() );
			
		logFile = new LogFile( "testDelimiter.log", dataDictionary );
		messages = logFile.parseMessages( null, null, null );
		assertEquals( 20, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );
		assertEquals( '|', logFile.getDelimiter() );
		
		logFile = new LogFile( "testMultiCharDelimiter.log", dataDictionary );
		messages = logFile.parseMessages( null, null, null );
		assertEquals( 20, messages.size() );
		assertEquals( 0, logFile.getInvalidMessages().size() );
		assertEquals( '^', logFile.getDelimiter() );
	}
	
	public void testGetStartTime() throws Exception {
		LogFile logFile = new LogFile( "test.log", dataDictionary );
		Date time = logFile.getStartTime();
		assertEquals("Sat May 01 13:26:51 GMT 2004", time.toString());
	}
	
	public void testGetEndTime() throws Exception {
		LogFile logFile = new LogFile( "test.log", dataDictionary );
		Date time = logFile.getEndTime();
		assertEquals("Wed May 19 14:27:52 GMT 2004", time.toString());
	}
}
