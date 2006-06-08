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
	public static final String FILE_OPEN = "Open";
	public static final String FILE_CLOSE = "Close";
	public static final String FILE_TRACE = "Trace";
	public static final String VIEW_AUTOSIZE_COLUMNS = "Autosize Columns";
	public static final String VIEW_AUTOSIZE_AND_HIDE_COLUMNS = "Autosize And Hide Columns";
	public static final String MESSAGE_VALIDATE = "Validate";
	public static final String FILTER_ALL_MESSAGES = "All Messages";
	public static final String FILTER_ADMINISTRATIVE_MESSAGES = "Administrative Messages";
	public static final String FILTER_APPLICATION_MESSAGES = "Application Messages";
	public static final String FILTER_CUSTOM_FILTER = "Custom Filter";
	public static final String FILTER_INDICATION_CATEGORY = "Category: Indication";
	public static final String FILTER_EVENT_COMMUNICATION_CATEGORY = "Category: Event Communication";
	public static final String FILTER_QUOTATION_NEGOTIATION_CATEGORY = "Category: Quotation/Negotiation";
	public static final String FILTER_MARKET_DATA_CATEGORY = "Category: Market Data";
	public static final String FILTER_SECURITY_AND_TRADING_SESSION_CATEGORY = "Category: Security And Trading Session";
	public static final String FILTER_SINGLE_GENERAL_ORDER_HANDLING_CATEGORY = "Category: Single/General Order Handling";
	public static final String FILTER_CROSS_ORDERS_CATEGORY = "Category: Cross Orders";
	public static final String FILTER_MULTILEG_ORDERS_CATEGORY = "Category: Multileg Orders";
	public static final String FILTER_LIST_PROGRAM_BASKET_TRADING_CATEGORY = "Category: List/Program/Basket Trading";
	public static final String FILTER_ALLOCATION_CATEGORY = "Category: Allocation";
	public static final String FILTER_CONFIRMATION_CATEGORY = "Category: Confirmation";
	public static final String FILTER_SETTLEMENT_INSTRUCTIONS_CATEGORY = "Category: Settlement Instructions";
	public static final String FILTER_TRADE_CAPTURE_REPORTING_CATEGORY = "Category: Trade Capture Reporting";
	public static final String FILTER_REGISTRATION_INSTRUCTIONS_CATEGORY = "Category: Registration Instructions";
	public static final String FILTER_POSITIONS_MAINTENANCE_CATEGORY = "Category: Positions Maintenance";
	public static final String FILTER_COLLATERAL_MANAGEMENT_CATEGORY = "Category: Collateral Management";
	public static final String HELP_ABOUT = "About";
	
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem fileOpen = new JMenuItem( FILE_OPEN );
	private JMenuItem fileClose = new JMenuItem( FILE_CLOSE );
	private JCheckBoxMenuItem fileTrace = new JCheckBoxMenuItem( FILE_TRACE );
	
	private JMenu viewMenu = new JMenu("View");
	private JMenuItem autosizeColumns = new JMenuItem( VIEW_AUTOSIZE_COLUMNS );
	private JMenuItem autosizeAndHideColumns = new JMenuItem( VIEW_AUTOSIZE_AND_HIDE_COLUMNS );
	
	private JMenu filterMenu = new JMenu("Filter");
	private JCheckBoxMenuItem filterAllMessages = new JCheckBoxMenuItem( FILTER_ALL_MESSAGES );
	private JCheckBoxMenuItem filterAdministrativeMessages = new JCheckBoxMenuItem( FILTER_ADMINISTRATIVE_MESSAGES );
	private JCheckBoxMenuItem filterApplicationMessages = new JCheckBoxMenuItem( FILTER_APPLICATION_MESSAGES );
	private JCheckBoxMenuItem filterCustomFilter = new JCheckBoxMenuItem( FILTER_CUSTOM_FILTER );
	private JCheckBoxMenuItem filterIndicationCategory = new JCheckBoxMenuItem( FILTER_INDICATION_CATEGORY );
	private JCheckBoxMenuItem filterEventCommunicationCategory = new JCheckBoxMenuItem( FILTER_EVENT_COMMUNICATION_CATEGORY );
	private JCheckBoxMenuItem filterQuotationNegotiationCategory = new JCheckBoxMenuItem( FILTER_QUOTATION_NEGOTIATION_CATEGORY );
	private JCheckBoxMenuItem filterMarketDataCategory = new JCheckBoxMenuItem( FILTER_MARKET_DATA_CATEGORY );
	private JCheckBoxMenuItem filterSecurityAndTradingSessionCategory = new JCheckBoxMenuItem( FILTER_SECURITY_AND_TRADING_SESSION_CATEGORY );
	private JCheckBoxMenuItem filterSingleGeneralOrderHandlingCategory = new JCheckBoxMenuItem( FILTER_SINGLE_GENERAL_ORDER_HANDLING_CATEGORY );
	private JCheckBoxMenuItem filterCrossOrdersCategory = new JCheckBoxMenuItem( FILTER_CROSS_ORDERS_CATEGORY );
	private JCheckBoxMenuItem filterMultilegOrdersCategory = new JCheckBoxMenuItem( FILTER_MULTILEG_ORDERS_CATEGORY );
	private JCheckBoxMenuItem filterListProgramBasketTradingCategory = new JCheckBoxMenuItem( FILTER_LIST_PROGRAM_BASKET_TRADING_CATEGORY );
	private JCheckBoxMenuItem filterAllocationCategory = new JCheckBoxMenuItem( FILTER_ALLOCATION_CATEGORY );
	private JCheckBoxMenuItem filterConfirmationCategory = new JCheckBoxMenuItem( FILTER_CONFIRMATION_CATEGORY );
	private JCheckBoxMenuItem filterSettlementInstructionsCategory = new JCheckBoxMenuItem( FILTER_SETTLEMENT_INSTRUCTIONS_CATEGORY );
	private JCheckBoxMenuItem filterTradeCaptureReportingCategory = new JCheckBoxMenuItem( FILTER_TRADE_CAPTURE_REPORTING_CATEGORY );
	private JCheckBoxMenuItem filterRegistrationInstructionsCategory = new JCheckBoxMenuItem( FILTER_REGISTRATION_INSTRUCTIONS_CATEGORY );
	private JCheckBoxMenuItem filterPositionsMaintenanceCategory = new JCheckBoxMenuItem( FILTER_POSITIONS_MAINTENANCE_CATEGORY );
	private JCheckBoxMenuItem filterCollateralManagementCategory = new JCheckBoxMenuItem( FILTER_COLLATERAL_MANAGEMENT_CATEGORY );
	private JCheckBoxMenuItem currentViewSource = filterAllMessages;
	private JCheckBoxMenuItem previousViewSource = filterAllMessages;

	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem about = new JMenuItem( HELP_ABOUT );
	
	ArrayList actionListeners = new ArrayList();
	
	public MenuBar() throws HeadlessException {
		super();
	
		int keyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		fileOpen.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, keyMask ) );
		fileMenu.add( fileOpen );
		fileClose.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_W, keyMask ) );			
		fileMenu.add( fileClose );
		fileMenu.add( new JSeparator() );
		fileTrace.setEnabled( true );		
		fileMenu.add( fileTrace );		
		add( fileMenu );
		
		viewMenu.add( autosizeColumns );
		viewMenu.add( autosizeAndHideColumns );
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
	
		helpMenu.add( about );
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
			fileTrace.addActionListener( l );
			autosizeColumns.addActionListener( l );
			autosizeAndHideColumns.addActionListener( l );
			about.addActionListener( l );
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
