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
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelListener;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public class MessagesTable extends JTable {

	public MessagesTable(TableModel dm) {
		super(dm);
		setAutoResizeMode( AUTO_RESIZE_OFF );
		selectionModel.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	}
	
	public void autoSizeHeaders() {
		
		MessagesTableModel dm = (MessagesTableModel)getModel();
		int columns = dm.getColumnCount();
		
		FontMetrics fontMetrics = getFontMetrics(getFont());
		
		for( int column = 0; column < columns; ++column ) {
			TableColumn tableColumn = getColumnModel().getColumn(column);			
			String value = dm.getColumnName( column );
			int width = fontMetrics.charsWidth( value.toCharArray(), 0, value.length() );
			if( tableColumn.getPreferredWidth() < width+10 )
				tableColumn.setPreferredWidth(width+10);
		}
	}
	
	public void autoSizeColumns( ProgressBarPanel progressBar, int sample ) {

		MessagesTableModel dm = (MessagesTableModel)getModel();
		int rows = dm.getRowCount();
		int columns = dm.getColumnCount();
		if( columns == 0 || rows == 0 ) return;
		
		int increment = 1;
		if( sample >= 0 )
			increment = rows / sample;
		if( increment < 1 )
			increment = 1;

		FontMetrics fontMetrics = getFontMetrics(getFont());

		progressBar.setTask( "Auto-Sizing Columns", 0, sample*columns, true );

		autoSizeHeaders();
		
		boolean columnIsEmpty = true;
		HashSet emptyColumns = new HashSet();

		for( int column = 0; column < columns; ++column, columnIsEmpty = true ) {
			TableColumn tableColumn = getColumnModel().getColumn(column);

			for( int row = 0; row < rows; ++row ) {
				String value = (String)dm.getValueAt( row, column );
				if( value.length() != 0 ) {
					columnIsEmpty = false;	
				}
			}
			
			if( columnIsEmpty ) {
				emptyColumns.add( tableColumn );
				continue;
			}
			
			for( int row = 0; row < rows; row = row + increment ) {
				String value = (String)dm.getDisplayString( row, column, false );
				int width = fontMetrics.charsWidth( value.toCharArray(), 0, value.length() );
				if( tableColumn.getPreferredWidth() < width+10 )
					tableColumn.setPreferredWidth(width+10);
				if( !progressBar.increment() ) return;
			}
		}

		if( sample == 1 ) {
			Iterator i = emptyColumns.iterator();
			while( i.hasNext() ) {
				final TableColumn tableColumn = (TableColumn)i.next();
				Runnable runnable = new Runnable() {
					public void run() {
						//getColumnModel().remove(tableColumn);
						tableColumn.setPreferredWidth(0);
					}
				};
				SwingUtilities.invokeLater( runnable );
			}
		}
		progressBar.done();
	}
	
	public void addColumnModelListener( TableColumnModelListener listener ) {
		columnModel.addColumnModelListener( listener );
	}
	
	public void addListSelectionListener( ListSelectionListener listener ) {
		selectionModel.addListSelectionListener( listener );
	}
	
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    		DefaultTableCellRenderer r = (DefaultTableCellRenderer)renderer;
    		r.setForeground(Color.black);

    		if((row%2) == 0)
    			r.setBackground(Color.white);
    		else
    			r.setBackground(Color.lightGray);

    		return super.prepareRenderer(renderer, row, column);
    }

    public Integer getTagFromPoint( Point point ) {
		int column = columnAtPoint(point);
		return ((MessagesTableModel)getModel()).getTagFromColumn( column );
    }
    
    public String getValueFromPoint( Point point ) {
		int row = rowAtPoint(point);
		int column = columnAtPoint(point);
    		return ((MessagesTableModel)getModel()).getDisplayString( row, column, true );
    }
}
