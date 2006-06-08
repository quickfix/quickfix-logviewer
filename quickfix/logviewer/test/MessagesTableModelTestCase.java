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

import quickfix.DataDictionary;
import quickfix.logviewer.FieldFilter;
import quickfix.logviewer.LogFile;
import quickfix.logviewer.MessagesTableModel;
import quickfix.logviewer.ProgressBar;
import quickfix.logviewer.ProgressBarPanel;
import junit.framework.TestCase;

public class MessagesTableModelTestCase extends TestCase {
	private DataDictionary dataDictionary = null;
	private ProgressBarPanel progressBar = null;
	
	public MessagesTableModelTestCase() throws quickfix.ConfigError {
		dataDictionary = new DataDictionary("lib/FIX44.xml");
		progressBar = new ProgressBarPanel(new ProgressBar());
	}
	
	public void testSetMessages() throws Exception {
		LogFile logFile = new LogFile( "test.log", dataDictionary );
		ArrayList messages = logFile.parseMessages( progressBar, null, null );
		MessagesTableModel tableModel = new MessagesTableModel( dataDictionary );
		tableModel.setMessages( messages, progressBar );
		
		assertEquals( "MsgSeqNum(34)", tableModel.getColumnName(0) );
		assertEquals( "BodyLength(9)", tableModel.getColumnName(1) );
		assertEquals( "BeginString(8)", tableModel.getColumnName(2) );
		assertEquals( "MsgType(35)", tableModel.getColumnName(4) );
		assertEquals( "Symbol(55)", tableModel.getColumnName(23) );
		assertEquals( "CheckSum(10)", tableModel.getColumnName(34) );

		assertEquals( "1", tableModel.getValueAt(0,0) );
		assertEquals( "64", tableModel.getValueAt(0,1) );
		assertEquals( "FIX.4.2", tableModel.getValueAt(0,2) );
		assertEquals( "", tableModel.getValueAt(0,10) );
		assertEquals( "137", tableModel.getValueAt(0,34) );
		
		assertEquals( "5", tableModel.getValueAt(4,0) );
		assertEquals( "103", tableModel.getValueAt(4,1) );
		assertEquals( "FIX.4.2", tableModel.getValueAt(4,2) );
		assertEquals( "DELL", tableModel.getValueAt(4, 23) );
		assertEquals( "043", tableModel.getValueAt(4,34) );
	}
	
	public void testFilter() throws Exception {
		LogFile logFile = new LogFile( "test.log", dataDictionary );
		ArrayList messages = logFile.parseMessages( progressBar, null, null );
		MessagesTableModel tableModel = new MessagesTableModel( dataDictionary );
		tableModel.setMessages( messages, progressBar );
		
		ArrayList filter = new ArrayList();
		filter.add( new FieldFilter(new quickfix.StringField(9, "52"), FieldFilter.EQUAL ));
		tableModel.filter( filter );
		assertEquals( "52", tableModel.getValueAt(0,1) );
		assertEquals( "52", tableModel.getValueAt(1,1) );
		assertEquals( "52", tableModel.getValueAt(2,1) );
		assertEquals( "52", tableModel.getValueAt(3,1) );
		assertEquals( "52", tableModel.getValueAt(4,1) );
		
		filter = new ArrayList();
		filter.add( new FieldFilter(new quickfix.StringField(55, "DELL"), FieldFilter.EQUAL ));
		tableModel.filter( filter );
		assertEquals( "DELL", tableModel.getValueAt(0,23) );
		assertEquals( "NewOrderSingle (D)", tableModel.getValueAt(0, 4) );
		assertEquals( "DELL", tableModel.getValueAt(1,23) );
		assertEquals( "NewOrderSingle (D)", tableModel.getValueAt(1, 4) );
		assertEquals( "DELL", tableModel.getValueAt(2,23) );
		assertEquals( "NewOrderSingle (D)", tableModel.getValueAt(2, 4) );
		assertEquals( "DELL", tableModel.getValueAt(3,23) );
		assertEquals( "OrderCancelRequest (F)", tableModel.getValueAt(3, 4) );
		assertEquals( "DELL", tableModel.getValueAt(4,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(4, 4) );
		
		filter.add( new FieldFilter(new quickfix.StringField(35, "8"), FieldFilter.EQUAL ));
		tableModel.filter( filter );
		assertEquals( "DELL", tableModel.getValueAt(0,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(0, 4) );
		assertEquals( "DELL", tableModel.getValueAt(1,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(1, 4) );
		assertEquals( "DELL", tableModel.getValueAt(2,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(2, 4) );
		assertEquals( "DELL", tableModel.getValueAt(3,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(3, 4) );
		assertEquals( "DELL", tableModel.getValueAt(4,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(4, 4) );
		
		filter.add( new FieldFilter(new quickfix.StringField(10, "253"), FieldFilter.EQUAL ));
		tableModel.filter( filter );
		assertEquals( "DELL", tableModel.getValueAt(0,23) );
		assertEquals( "ExecutionReport (8)", tableModel.getValueAt(0, 4) );
		assertEquals( "148", tableModel.getValueAt(0,1) );
	}
}
