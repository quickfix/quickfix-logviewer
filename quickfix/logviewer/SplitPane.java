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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import quickfix.DataDictionary;
import quickfix.Message;
import quickfix.StringField;

public class SplitPane extends JSplitPane 
	implements ListSelectionListener, ActionListener, MouseListener, ChangeListener {
	
	private Frame frame = null;
	private MenuBar menuBar = null;
	private ProgressBarPanel progressBar = null;
	private MessageTable messageTable = null;
	private MessageTree messageTree = null;
	private MessagesTable currentTable = null;
	private MessagesTableModel currentModel = null;
	private JTabbedPane upperTabbedPane = null;
	private DataDictionary dataDictionary = null;
	private Timer tracer = null;
		
	private Message message = null;
		
	public SplitPane( 
		Frame aFrame,
		ProgressBarPanel aProgressBar,
		MessageTable aMessageTable,
		MessageTree aMessageTree,
		JTabbedPane aUpperTabbedPane,
		JTabbedPane aLowerTabbedPane,
		DataDictionary aDataDictionary ) {
		super( JSplitPane.VERTICAL_SPLIT, aUpperTabbedPane, aLowerTabbedPane );
		
		frame = aFrame;
		menuBar = (MenuBar)aFrame.getJMenuBar();
		progressBar = aProgressBar;
		messageTable = aMessageTable;
		messageTree = aMessageTree;
		messageTree.setModel(null);
		upperTabbedPane = aUpperTabbedPane;
		dataDictionary = aDataDictionary;
		
		aLowerTabbedPane.add( "Grid", new JScrollPane(messageTable) );
		aLowerTabbedPane.add( "Tree", new JScrollPane(messageTree) );
		aUpperTabbedPane.addChangeListener( this );
		messageTable.addMouseListener( this );
		messageTree.addMouseListener( this );
		
		tracer = new Timer( 5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				traceFile();
			}
		});		
	}

	public void valueChanged(ListSelectionEvent e) {
		message = currentModel.getMessageAt( currentTable.getSelectedRow() );
		messageTable.setModel( new MessageTableModel(message, dataDictionary) );
		messageTree.setModel( new MessageTreeModel(message, dataDictionary ) );
	}

	public void actionPerformed(final ActionEvent e) {
		Object source = e.getSource();
		
		if( source == MenuBar.fileClose ) {
			closeFile();
		} else if( source == MenuBar.fileTrace ) {
			if( tracer.isRunning() )
				tracer.stop();
			else
				tracer.start();
		} else if( source == MenuBar.filterAllMessages ) {
			currentModel.viewAll();
		} else if( source == MenuBar.filterAdministrativeMessages ) {
			currentModel.viewAdministrative();
		} else if( source == MenuBar.filterApplicationMessages ) {
			currentModel.viewApplication();
		} else if( source == MenuBar.filterIndicationCategory ) {
			currentModel.viewCategory( Category.Indication );
		} else if( source == MenuBar.filterEventCommunicationCategory ) {
			currentModel.viewCategory( Category.EventCommunication );
		} else if( source == MenuBar.filterQuotationNegotiationCategory ) {
			currentModel.viewCategory( Category.QuoteNegotiation );
		} else if( source == MenuBar.filterMarketDataCategory ) {
			currentModel.viewCategory( Category.MarketData );
		} else if( source == MenuBar.filterSecurityAndTradingSessionCategory ) {
			currentModel.viewCategory( Category.SecurityAndTradingSessionDefinitionStatus );
		} else if( source == MenuBar.filterSingleGeneralOrderHandlingCategory ) {
			currentModel.viewCategory( Category.SingleGeneralOrderHandling );
		} else if( source == MenuBar.filterCrossOrdersCategory ) {
			currentModel.viewCategory( Category.CrossOrder );
		} else if( source == MenuBar.filterMultilegOrdersCategory ) {
			currentModel.viewCategory( Category.MultiLegOrders );
		} else if( source == MenuBar.filterListProgramBasketTradingCategory ) {
			currentModel.viewCategory( Category.ListProgramBasketTrading );
		} else if( source == MenuBar.filterAllocationCategory ) {
			currentModel.viewCategory( Category.Allocation );
		} else if( source == MenuBar.filterConfirmationCategory ) {
			currentModel.viewCategory( Category.Confirmation );
		} else if( source == MenuBar.filterSettlementInstructionsCategory ) {
			currentModel.viewCategory( Category.SettlementInstructions );
		} else if( source == MenuBar.filterTradeCaptureReportingCategory ) {
			currentModel.viewCategory( Category.TradeCaptureReporting );
		} else if( source == MenuBar.filterRegistrationInstructionsCategory ) {
			currentModel.viewCategory( Category.RegistrationInstructions );
		} else if( source == MenuBar.filterPositionsMaintenanceCategory ) {
			currentModel.viewCategory( Category.PositionsManagement );
		} else if( source == MenuBar.filterCollateralManagementCategory ) {
			currentModel.viewCategory( Category.CollateralManagement );
		} else if( source == MenuBar.filterCustomFilter ) {
			applyFilter( null );
		} else if( source == MenuBar.helpAbout ) {
			showAbout();
		} else {
			setEnabled( false );
			menuBar.setEnabled( false );

			new ActionThread( e ) {
			
				public void run() {
					Object source = e.getSource();
						
					if( source == MenuBar.fileOpen ) {
						openFile();
					} else if( source == MenuBar.fileExportFIX ) {
						exportFile( source );
					} else if( source == MenuBar.fileExportXML ) {
						exportFile( source );
					} else if( source == MenuBar.fileExportCSV ) {
						exportFile( source );
					} else if( source == MenuBar.viewExportFIX ) {
						exportFile( source );
					} else if( source == MenuBar.viewExportXML ) {
						exportFile( source );
					} else if( source == MenuBar.viewExportCSV ) {
						exportFile( source );
					} else if( source == MenuBar.viewAutosizeColumns ) {
						currentTable.autoSizeColumns( progressBar, 100 );
					} else if( source == MenuBar.viewAutosizeAndHideColumns ) {
						currentTable.autoSizeColumns( progressBar, 1 );
					}
								
					setEnabled( true );
					menuBar.setEnabled( true );
				}
			}.start();
		}
	}
	
	private void openFile() {
		FileOpenDialog dialog = new FileOpenDialog( frame );
		dialog.setVisible( true );
		final File file = dialog.getFile();
		final Date startTime = dialog.getStartTime();
		final Date endTime = dialog.getEndTime();
		dialog.dispose();

		boolean traceRunning = tracer.isRunning();
		if( traceRunning ) tracer.stop();
		
		if( file != null ) {
			try {
				LogFile logFile = new LogFile( file, dataDictionary );
				ArrayList messages = logFile.parseMessages( progressBar, startTime, endTime );
				currentModel = new MessagesTableModel( dataDictionary, logFile );
				currentTable = new MessagesTable( currentModel );
				currentTable.addListSelectionListener( this );
				currentTable.addMouseListener( this );
				JScrollPane component = new JScrollPane( currentTable );
				upperTabbedPane.add( file.getName(), component );
				upperTabbedPane.setSelectedComponent( component );
				currentModel.setMessages( messages, progressBar );
				menuBar.reset();
				menuBar.setFileOpen( true );
			} catch( CancelException e ) {
				closeFile();
			} catch( FileNotFoundException e ) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		if( traceRunning ) tracer.start();		
	}
	
	private void exportFile( final Object command ) {
		FileExportDialog dialog = new FileExportDialog( frame );
		dialog.setVisible( true );
		File file = dialog.getFile();
		dialog.dispose();
		
		boolean traceRunning = tracer.isRunning();
		if( traceRunning ) tracer.stop();
		
		if( file != null ) {
			try {
				if( command == MenuBar.fileExportFIX)
					currentModel.exportAllToFIX( progressBar, file );
				else if( command == MenuBar.fileExportXML )
					currentModel.exportAllToXML( progressBar, file );
				else if( command == MenuBar.fileExportCSV )
					currentModel.exportAllToCSV( progressBar, file );
				else if( command == MenuBar.viewExportFIX )
					currentModel.exportViewToFIX( progressBar, file );
				else if( command == MenuBar.viewExportXML )
					currentModel.exportViewToXML( progressBar, file );
				else if( command == MenuBar.viewExportCSV )
					currentModel.exportViewToCSV( progressBar, file );
			} catch( IOException e ) {
				System.out.println(e);
			}
		}
		
		if( traceRunning ) tracer.start();
	}
	
	private boolean applyFilter( FieldFilter fieldFilter ) {
		if( currentModel == null )
			return false;
		
		boolean result = false;
		ArrayList filter = (ArrayList)currentModel.getFilter().clone();
		if( fieldFilter != null )
			filter.add( fieldFilter );
		
		CustomFilterDialog dialog = 
			new CustomFilterDialog( frame, filter, currentModel.getTags(), dataDictionary );
		
		dialog.setVisible( true );
		filter = dialog.getFilter();
		if( filter != null ) {
			currentModel.filter( filter );
			result = true;
		} else {
			menuBar.undo();
			result = false;
		}
		dialog.dispose();
		return result;
	}
	
	private void showAbout() {
		AboutDialog dialog = new AboutDialog( frame );
		dialog.setVisible( true );
	}
	
	private void closeFile() {
		upperTabbedPane.remove( upperTabbedPane.getSelectedComponent() );
		menuBar.setFileOpen( upperTabbedPane.getComponentCount() > 0 );
		System.gc();
	}
	
	private void traceFile() {
		int count =	upperTabbedPane.getComponentCount();		
		for( int i = 0; i < count; ++i ) {
			JScrollPane scrollPane = ((JScrollPane)upperTabbedPane.getComponent(i));
			MessagesTable table = (MessagesTable)scrollPane.getViewport().getView();
			MessagesTableModel model = (MessagesTableModel)table.getModel();
			LogFile logFile = model.getLogFile();
			if( logFile == null ) continue;
			try {
				ArrayList messages = logFile.parseNewMessages( null );
				model.addMessages( messages, null );
			} catch (IOException e) {				
			} catch (CancelException e) {				
			}
		}
	}
	
	private class ActionThread extends Thread {
		protected ActionEvent e = null;
		
		public ActionThread( ActionEvent ae ) {
			e = ae;
		}
	}

	public void mouseClicked(MouseEvent e) {
		if( e.getClickCount() < 2 ) return;
		Integer tag = null;
		String value = null;
		
		if( e.getSource() == currentTable ) {
			tag = currentTable.getTagFromPoint(e.getPoint());
			value = currentTable.getValueFromPoint(e.getPoint());
		} if( e.getSource() == messageTable ) {
			tag = messageTable.getTagFromPoint(e.getPoint());
			value = messageTable.getValueFromPoint(e.getPoint());
		} if( e.getSource() == messageTree ) {
			tag = messageTree.getTagFromPoint(e.getPoint());
			value = messageTree.getValueFromPoint(e.getPoint());
		}
		
		if( tag == null || value == null )
			return;
		
		menuBar.customFilter();
		applyFilter( new FieldFilter(new StringField(tag.intValue(), value), FieldFilter.EQUAL ));
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void stateChanged(ChangeEvent e) {
		JScrollPane scrollPane = ((JScrollPane)upperTabbedPane.getSelectedComponent());
		frame.fileLoaded( scrollPane != null );
		if( scrollPane == null ) return;
		
		currentTable = (MessagesTable)scrollPane.getViewport().getView();
		currentModel = (MessagesTableModel)currentTable.getModel();
		menuBar.setSelectedFilter( currentModel.getFilterType() );
	}
}
