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
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import quickfix.DataDictionary;

public class MessageTable extends JTable {

	public MessageTable() {
		setAutoResizeMode( AUTO_RESIZE_OFF );
		setShowGrid( false );
		selectionModel.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	}

	public void setModel(TableModel dm) {
		super.setModel(dm);
		
		int rows = dm.getRowCount();
		int columns = dm.getColumnCount();
		for( int column = 0; column < columns; ++column ) {
			getColumnModel().getColumn(column).setPreferredWidth(0);
			for( int row = 0; row < rows; ++row ) {
				String value = (String)dm.getValueAt( row, column );
				if( value == null ) continue;
				int width = getFontMetrics(getFont()).charsWidth( value.toCharArray(), 0, value.length() );
				if( getColumnModel().getColumn(column).getPreferredWidth() < width+10 )
					getColumnModel().getColumn(column).setPreferredWidth(width+10);
			}
		}
	}
	
	private int getTagColumn( int row, MessageTableModel dm ) {
		// find first non-empty string, should be field number
		int columnCount = dm.getColumnCount();
		String value = null;
		int tagColumn = 0;
		for( tagColumn = 0; tagColumn < columnCount; ++tagColumn ) {
			value = dm.getValueAt( row, tagColumn ).toString();
			try {
				Integer.parseInt(value);
				break;
			} catch( NumberFormatException e ) {
			}
		}
		if( value.length() == 0 )
			return -1;
		
		return tagColumn;
	}
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		MessageTableModel dm = (MessageTableModel)getModel();
		
		int tagColumn = getTagColumn( row, dm );
		String value = dm.getValueAt( row, tagColumn ).toString();
		
		if( tagColumn < 0 )
			return super.prepareRenderer(renderer, row, column);

		DefaultTableCellRenderer r = (DefaultTableCellRenderer)renderer;
		r.setForeground( Color.black );
		
		int tag = Integer.parseInt(value);
		DataDictionary dataDictionary = dm.getDataDictionary();
		
		if( dataDictionary.isHeaderField(tag) || dataDictionary.isTrailerField(tag) ) {
			if( column == tagColumn )
				r.setForeground(new Color(192,192,255));
			else if( column == tagColumn + 1 )
				r.setForeground(new Color(128,128,255));
			else if( column == tagColumn + 2 )
				r.setForeground(Color.black);
			else if( column == tagColumn + 3 )
				r.setForeground(new Color(255,128,255));
			else if( column == tagColumn + 4 )
				r.setForeground(new Color(255,128,255));
		} else {
			if( column == tagColumn )
				r.setForeground(new Color(64,64,255));
			else if( column == tagColumn + 1 )
				r.setForeground(new Color(0,0,255));
			else if( column == tagColumn + 2 )
				r.setForeground(Color.black);
			else if( column == tagColumn + 3 )
				r.setForeground(new Color(255,0,255));
			else if( column == tagColumn + 4 )
				r.setForeground(new Color(255,0,255));
		}

		return super.prepareRenderer(renderer, row, column);
	}
	
	public Integer getTagFromPoint( Point point ) {
		MessageTableModel dm = (MessageTableModel)getModel();
		int row = rowAtPoint(point);
		int column = getTagColumn( row, dm );
		if( column < 0 ) return null;
		String value = dm.getValueAt( row, column ).toString();
		return new Integer(Integer.parseInt( value ));
    }
    
    public String getValueFromPoint( Point point ) {
		MessageTableModel dm = (MessageTableModel)getModel();
		int row = rowAtPoint(point);
		int column = getTagColumn( row, dm ) + 3;
		if( column < 0 ) return null;
		String value = dm.getValueAt( row, column ).toString();
		return value;
    }
}
