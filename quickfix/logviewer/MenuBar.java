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
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class MenuBar extends javax.swing.JMenuBar implements ActionListener {
	private JMenu fileMenu = new JMenu("File");
	public static final JMenuItem fileOpen = new JMenuItem( "Open" );
	public static final JMenuItem fileClose = new JMenuItem( "Close" );
	private JMenu fileExportMenu = new JMenu( "Export" );
	public static final JMenuItem fileExportFIX = new JMenuItem( "To FIX" );
	public static final JMenuItem fileExportXML = new JMenuItem( "To XML" );
	public static final JMenuItem fileExportCSV = new JMenuItem( "To CSV" );
	public static final JCheckBoxMenuItem fileTrace = new JCheckBoxMenuItem( "Trace" );
	
	private JMenu viewMenu = new JMenu("View");
	public static final JMenuItem viewAutosizeColumns = new JMenuItem( "Autosize Columns" );
	public static final JMenuItem viewAutosizeAndHideColumns = new JMenuItem( "Autosize And Hide Columns" );
	private JMenu viewExportMenu = new JMenu( "Export" );
	public static final JMenuItem viewExportFIX = new JMenuItem( "To FIX" );
	public static final JMenuItem viewExportXML = new JMenuItem( "To XML" );
	public static final JMenuItem viewExportCSV = new JMenuItem( "To CSV" );
	
	private JMenu filterMenu = new JMenu("Filter");
	public static final JCheckBoxMenuItem filterAllMessages = new JCheckBoxMenuItem( "All Messages" );
	public static final JCheckBoxMenuItem filterAdministrativeMessages = new JCheckBoxMenuItem( "Administrative Messages" );
	public static final JCheckBoxMenuItem filterApplicationMessages = new JCheckBoxMenuItem( "Application Messages" );
	public static final JCheckBoxMenuItem filterCustomFilter = new JCheckBoxMenuItem( "Custom Filter" );
	public static final JCheckBoxMenuItem filterIndicationCategory = new JCheckBoxMenuItem( "Indication" );
	public static final JCheckBoxMenuItem filterEventCommunicationCategory = new JCheckBoxMenuItem( "Event Communication" );
	public static final JCheckBoxMenuItem filterQuotationNegotiationCategory = new JCheckBoxMenuItem( "Quotation/Negotiation" );
	public static final JCheckBoxMenuItem filterMarketDataCategory = new JCheckBoxMenuItem( "Market Data" );
	public static final JCheckBoxMenuItem filterSecurityAndTradingSessionCategory = new JCheckBoxMenuItem( "Security And Trading Session" );
	public static final JCheckBoxMenuItem filterSingleGeneralOrderHandlingCategory = new JCheckBoxMenuItem( "Single/General Order Handling" );
	public static final JCheckBoxMenuItem filterCrossOrdersCategory = new JCheckBoxMenuItem( "Cross Orders" );
	public static final JCheckBoxMenuItem filterMultilegOrdersCategory = new JCheckBoxMenuItem( "Multileg Orders" );
	public static final JCheckBoxMenuItem filterListProgramBasketTradingCategory = new JCheckBoxMenuItem( "List/Program/Basket Trading" );
	public static final JCheckBoxMenuItem filterAllocationCategory = new JCheckBoxMenuItem( "Allocation" );
	public static final JCheckBoxMenuItem filterConfirmationCategory = new JCheckBoxMenuItem( "Confirmation" );
	public static final JCheckBoxMenuItem filterSettlementInstructionsCategory = new JCheckBoxMenuItem( "Settlement Instructions" );
	public static final JCheckBoxMenuItem filterTradeCaptureReportingCategory = new JCheckBoxMenuItem( "Trade Capture Reporting" );
	public static final JCheckBoxMenuItem filterRegistrationInstructionsCategory = new JCheckBoxMenuItem( "Registration Instructions" );
	public static final JCheckBoxMenuItem filterPositionsMaintenanceCategory = new JCheckBoxMenuItem( "Positions Maintenance" );
	public static final JCheckBoxMenuItem filterCollateralManagementCategory = new JCheckBoxMenuItem( "Collateral Management" );
	private JCheckBoxMenuItem currentViewSource = filterAllMessages;
	private JCheckBoxMenuItem previousViewSource = filterAllMessages;

	private JMenu helpMenu = new JMenu("Help");
	public static final JMenuItem helpAbout = new JMenuItem( "About" );
	
	ArrayList actionListeners = new ArrayList();
	
	public MenuBar() throws HeadlessException {
		super();
	
		int keyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		fileOpen.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, keyMask ) );
		fileMenu.add( fileOpen );
		fileClose.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_W, keyMask ) );			
		fileMenu.add( fileClose );
		fileMenu.addSeparator();
		fileExportMenu.add( fileExportFIX );
		fileExportMenu.add( fileExportXML );
		fileExportMenu.add( fileExportCSV );
		fileMenu.add( fileExportMenu );
		fileMenu.add( new JSeparator() );
		fileTrace.setEnabled( true );		
		fileMenu.add( fileTrace );		
		add( fileMenu );
		
		viewMenu.add( viewAutosizeColumns );
		viewMenu.add( viewAutosizeAndHideColumns );
		viewMenu.addSeparator();
		viewExportMenu.add( viewExportFIX );
		viewExportMenu.add( viewExportXML );
		viewExportMenu.add( viewExportCSV );
		viewMenu.add( viewExportMenu );
		add( viewMenu );

		filterAllMessages.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_M, keyMask ) );
		filterMenu.add( filterAllMessages );
		filterAdministrativeMessages.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_D, keyMask ) );
		filterMenu.add( filterAdministrativeMessages );
		filterApplicationMessages.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_P, keyMask ) );
		filterMenu.add( filterApplicationMessages );
		filterMenu.add( new JSeparator() );
		filterCustomFilter.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F, keyMask ) );
		filterMenu.add( filterCustomFilter );
		filterMenu.add( new JSeparator() );
		filterMenu.add( filterIndicationCategory );
		filterMenu.add( filterEventCommunicationCategory );
		filterMenu.add( filterQuotationNegotiationCategory );
		filterMenu.add( filterMarketDataCategory );
		filterMenu.add( filterSecurityAndTradingSessionCategory );
		filterMenu.add( new JSeparator() );
		filterMenu.add( filterSingleGeneralOrderHandlingCategory );
		filterMenu.add( filterCrossOrdersCategory );
		filterMenu.add( filterMultilegOrdersCategory );
		filterMenu.add( filterListProgramBasketTradingCategory );
		filterMenu.add( new JSeparator() );
		filterMenu.add( filterAllocationCategory );
		filterMenu.add( filterConfirmationCategory );
		filterMenu.add( filterSettlementInstructionsCategory );
		filterMenu.add( filterTradeCaptureReportingCategory );
		filterMenu.add( filterRegistrationInstructionsCategory );
		filterMenu.add( filterPositionsMaintenanceCategory );
		filterMenu.add( filterCollateralManagementCategory );
		add( filterMenu );
	
		helpMenu.add( helpAbout );
		add( helpMenu );
		
		reset();
	
		addActionListener( this );		
		filterAllMessages.setSelected( true );
	}

	public void reset() {
		setFileOpen( false );
		setSelectedFilter( filterAllMessages );		
	}

	public void setFileOpen( boolean value ) {
		viewMenu.setEnabled( value );
		filterMenu.setEnabled( value );
		fileExportMenu.setEnabled( value );
		viewExportMenu.setEnabled( value );
		fileClose.setEnabled( value );
	}
	
	public void customFilter() {
		previousViewSource = currentViewSource;
		currentViewSource = filterCustomFilter;
		setSelectedFilter( currentViewSource );
	}
	
	public void addActionListener( ActionListener l ) {
		if( l == this ) {
			fileOpen.addActionListener( l );
			fileClose.addActionListener( l );
			fileExportFIX.addActionListener( l );
			fileExportXML.addActionListener( l );
			fileExportCSV.addActionListener( l );
			fileTrace.addActionListener( l );
			viewAutosizeColumns.addActionListener( l );
			viewAutosizeAndHideColumns.addActionListener( l );
			viewExportFIX.addActionListener( l );
			viewExportXML.addActionListener( l );
			viewExportCSV.addActionListener( l );
			helpAbout.addActionListener( l );
			Component[] components = filterMenu.getMenuComponents();
			for( int i = 0; i < components.length; ++i ) {
				try {
					JCheckBoxMenuItem component = (JCheckBoxMenuItem)components[i];
					component.addActionListener(l);
				} catch( ClassCastException cce ) {				
				}
			}
		} else {
			actionListeners.add( l );
		}
	}
	
	public void undo() {
		setSelectedFilter( previousViewSource );
	}

	public void actionPerformed(ActionEvent e) {
		try {
			previousViewSource = currentViewSource;
			JCheckBoxMenuItem source = (JCheckBoxMenuItem)e.getSource();
			currentViewSource = source;
			setSelectedFilter( source );
		} catch( ClassCastException cce ) {}
		
		Iterator i = actionListeners.iterator();
		while( i.hasNext() ) {
			ActionListener actionListener = (ActionListener)i.next();
			actionListener.actionPerformed( e );
		}
	}

	public void setSelectedFilter( int filter ) {
		if( filter == FilterType.ALL_MESSAGES )
			setSelectedFilter( filterAllMessages );
		else if( filter == FilterType.ADMINISTRATIVE_MESSAGES )
			setSelectedFilter( filterAdministrativeMessages );
		else if( filter == FilterType.APPLICATION_MESSAGES )
			setSelectedFilter( filterApplicationMessages );
		else if( filter == FilterType.CUSTOM_FILTER )
			setSelectedFilter( filterCustomFilter );
		else if( filter == FilterType.INDICATION_CATEGORY )
			setSelectedFilter( filterIndicationCategory );
		else if( filter == FilterType.EVENT_COMMUNICATION_CATEGORY )
			setSelectedFilter( filterEventCommunicationCategory );
		else if( filter == FilterType.QUOTATION_NEGOTIATION_CATEGORY )
			setSelectedFilter( filterQuotationNegotiationCategory );
		else if( filter == FilterType.MARKET_DATA_CATEGORY )
			setSelectedFilter( filterMarketDataCategory );
		else if( filter == FilterType.SECURITY_AND_TRADING_SESSION_CATEGORY )
			setSelectedFilter( filterSecurityAndTradingSessionCategory );
		else if( filter == FilterType.SINGLE_GENERAL_ORDER_HANDLING_CATEGORY )
			setSelectedFilter( filterSingleGeneralOrderHandlingCategory );
		else if( filter == FilterType.CROSS_ORDERS_CATEGORY )
			setSelectedFilter( filterCrossOrdersCategory );
		else if( filter == FilterType.MULTILEG_ORDERS_CATEGORY )
			setSelectedFilter( filterMultilegOrdersCategory );
		else if( filter == FilterType.LIST_PROGRAM_BASKET_TRADING_CATEGORY )
			setSelectedFilter( filterListProgramBasketTradingCategory );
		else if( filter == FilterType.ALLOCATION_CATEGORY )
			setSelectedFilter( filterAllocationCategory );
		else if( filter == FilterType.CONFIRMATION_CATEGORY )
			setSelectedFilter( filterConfirmationCategory );
		else if( filter == FilterType.SETTLEMENT_INSTRUCTIONS_CATEGORY )
			setSelectedFilter( filterSettlementInstructionsCategory );
		else if( filter == FilterType.TRADE_CAPTURE_REPORTING_CATEGORY )
			setSelectedFilter( filterTradeCaptureReportingCategory );
		else if( filter == FilterType.REGISTRATION_INSTRUCTIONS_CATEGORY )
			setSelectedFilter( filterRegistrationInstructionsCategory );
		else if( filter == FilterType.POSITIONS_MAINTENANCE_CATEGORY )
			setSelectedFilter( filterPositionsMaintenanceCategory );
		else if( filter == FilterType.COLLATERAL_MANAGEMENT_CATEGORY )
			setSelectedFilter( filterCollateralManagementCategory );
		else
			setSelectedFilter( null );
	}
	
	private void setSelectedFilter( JCheckBoxMenuItem source ) {
		Component[] components = filterMenu.getMenuComponents();
		for( int i = 0; i < components.length; ++i ) {
			try {
				JCheckBoxMenuItem component = (JCheckBoxMenuItem)components[i];
				component.setSelected( component == source );
			} catch( ClassCastException cce ) {				
			}
		}
	}

}
