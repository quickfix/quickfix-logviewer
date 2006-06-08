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
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;
import quickfix.*;

public class MessageTableModel extends AbstractTableModel {
	private HashMap rowToField = new HashMap();
	private HashMap rowToDepth = new HashMap();
	private Message message = null;
	private DataDictionary dataDictionary = null;
	private int rowCount = 0;
	private int colCount = 0;
	private int groupDepth = 0;
	private int depth = 0;
	
	public MessageTableModel( Message aMessage, DataDictionary aDataDictionary ) {
		super();
		
		message = aMessage;
		dataDictionary = aDataDictionary;
		if( message == null ) return;
		
		insertFieldMap( message.getHeader(), 0 );
		insertFieldMap( message, 0 );
		insertFieldMap( message.getTrailer(), 0 );
		
		colCount = 4 + groupDepth;
	}

	DataDictionary getDataDictionary() {
		return dataDictionary;
	}
	
	private void insertFieldMap(FieldMap fieldMap, int depth) {
		Iterator i = fieldMap.iterator();
		while( i.hasNext() ) {
			StringField field = (StringField)i.next();
			rowToField.put( new Integer(rowCount), field );
			rowToDepth.put( new Integer(rowCount++), new Integer(depth) );
			insertGroup( message, field );
		}
	}
	
	private void insertGroup(Message message, StringField field) {
		depth += 1;
		if( depth > groupDepth )
			groupDepth = depth;
		
		try {
			Group group = new Group( field.getField(), 1 );
			IntField integerField = new IntField( field.getField() );
			message.getField( integerField );
			for( int count = 1; count <= integerField.getValue(); ++count ) {
				message.getGroup( count, group );
				insertFieldMap( group, depth );
				insertBlank();
			}
		} catch( Exception e ) {} 
		
		depth -= 1;
	}
	
	private void insertBlank() {
		rowToField.put( new Integer(rowCount), null );
		rowToDepth.put( new Integer(rowCount++), null );
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return colCount;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		StringField field = (StringField) rowToField.get( new Integer(rowIndex) );
		Integer depth = (Integer) rowToDepth.get( new Integer(rowIndex) );
		
		if( field == null )
			return "";
		if( columnIndex < depth.intValue() )
			return " -> ";
		columnIndex -= depth.intValue();
		
		Integer tag = new Integer(field.getField());
		switch( columnIndex ) {
			case 0: return tag.toString();
			case 1: return dataDictionary.getFieldName(tag.intValue());
			case 2: return "=";
			case 3: return field.getValue();
			case 4: return dataDictionary.hasFieldValue(tag.intValue()) ? dataDictionary.getValueName(tag.intValue(), field.getValue()) : "";
			default: return "";
		}
	}
	
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}
	
	public String getColumnName(int column) {
		return "";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
