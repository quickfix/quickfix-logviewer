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

import quickfix.field.MsgType;

public final class Category {

	public static int categoryToFilterType( String[] category ) {
		if( category == Indication )
			return FilterType.INDICATION_CATEGORY;
		else if( category == EventCommunication )
			return FilterType.EVENT_COMMUNICATION_CATEGORY;
		else if( category == QuoteNegotiation )
			return FilterType.QUOTATION_NEGOTIATION_CATEGORY;
		else if( category == MarketData )
			return FilterType.MARKET_DATA_CATEGORY;
		else if( category == SecurityAndTradingSessionDefinitionStatus )
			return FilterType.SECURITY_AND_TRADING_SESSION_CATEGORY;
		else if( category == SingleGeneralOrderHandling )
			return FilterType.SINGLE_GENERAL_ORDER_HANDLING_CATEGORY;
		else if( category == CrossOrder )
			return FilterType.CROSS_ORDERS_CATEGORY;
		else if( category == MultiLegOrders )
			return FilterType.MULTILEG_ORDERS_CATEGORY;
		else if( category == ListProgramBasketTrading )
			return FilterType.LIST_PROGRAM_BASKET_TRADING_CATEGORY;
		else if( category == Allocation )
			return FilterType.ALLOCATION_CATEGORY;
		else if( category == Confirmation )
			return FilterType.CONFIRMATION_CATEGORY;
		else if( category == SettlementInstructions )
			return FilterType.SETTLEMENT_INSTRUCTIONS_CATEGORY;
		else if( category == TradeCaptureReporting )
			return FilterType.TRADE_CAPTURE_REPORTING_CATEGORY;
		else if( category == RegistrationInstructions )
			return FilterType.REGISTRATION_INSTRUCTIONS_CATEGORY;
		else if( category == PositionsManagement )
			return FilterType.POSITIONS_MAINTENANCE_CATEGORY;
		else if( category == CollateralManagement )
			return FilterType.COLLATERAL_MANAGEMENT_CATEGORY;
		else
			return -1;
	}
	
	public static final String[] Indication = new String[] { 
		MsgType.ADVERTISEMENT,
		MsgType.INDICATION_OF_INTEREST
	};
	
	public static final String[] EventCommunication = new String[] {
		MsgType.NEWS,
		MsgType.EMAIL
	};
	
	public static final String[] QuoteNegotiation = new String[] {
		MsgType.QUOTE_REQUEST,
		MsgType.QUOTE_RESPONSE,
		MsgType.QUOTE_REQUEST_REJECT,
		MsgType.RFQ_REQUEST,
		MsgType.QUOTE,
		MsgType.QUOTE_CANCEL,
		MsgType.QUOTE_STATUS_REQUEST,
		MsgType.QUOTE_STATUS_REPORT,
		MsgType.MASS_QUOTE,
		MsgType.MASS_QUOTE_ACKNOWLEDGEMENT
	};
	
	public static final String[] MarketData = new String[] {
		MsgType.MARKET_DATA_REQUEST,
		MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH,
		MsgType.MARKET_DATA_INCREMENTAL_REFRESH,
		MsgType.MARKET_DATA_REQUEST_REJECT
	};
	
	public static final String[] SecurityAndTradingSessionDefinitionStatus = new String[] {
		MsgType.SECURITY_DEFINITION_REQUEST,
		MsgType.SECURITY_DEFINITION,
		MsgType.SECURITY_TYPE_REQUEST,
		MsgType.SECURITY_TYPES,
		MsgType.SECURITY_LIST_REQUEST,
		MsgType.SECURITY_LIST,
		MsgType.DERIVATIVE_SECURITY_LIST_REQUEST,
		MsgType.DERIVATIVE_SECURITY_LIST,
		MsgType.SECURITY_STATUS_REQUEST,
		MsgType.SECURITY_STATUS,
		MsgType.TRADING_SESSION_STATUS_REQUEST,
		MsgType.TRADING_SESSION_STATUS
	};
	
	public static final String[] SingleGeneralOrderHandling = new String[] {
		MsgType.ORDER_SINGLE,
		MsgType.EXECUTION_REPORT,
		MsgType.DONT_KNOW_TRADE,
		MsgType.ORDER_CANCEL_REPLACE_REQUEST,
		MsgType.ORDER_CANCEL_REQUEST,
		MsgType.ORDER_CANCEL_REJECT,
		MsgType.ORDER_CANCEL_REJECT,
		MsgType.ORDER_STATUS_REQUEST,
		MsgType.ORDER_MASS_CANCEL_REQUEST,
		MsgType.ORDER_MASS_CANCEL_REPORT,
		MsgType.ORDER_MASS_STATUS_REQUEST		
	};
	
	public static final String[] CrossOrder = new String[] {
		MsgType.NEW_ORDER_CROSS,
		MsgType.CROSS_ORDER_CANCEL_REPLACE_REQUEST,
		MsgType.CROSS_ORDER_CANCEL_REQUEST
	};
	
	public static final String[] MultiLegOrders = new String[] {
		MsgType.NEW_ORDER_MULTILEG,
		MsgType.MULTILEG_ORDER_CANCEL_REPLACE
	};
	
	public static final String[] ListProgramBasketTrading = new String[] {
		MsgType.BID_REQUEST,
		MsgType.BID_RESPONSE,
		MsgType.ORDER_LIST,
		MsgType.LIST_STRIKE_PRICE,
		MsgType.LIST_STATUS,
		MsgType.LIST_EXECUTE,
		MsgType.LIST_CANCEL_REQUEST,
		MsgType.LIST_STATUS_REQUEST
	};
	
	public static final String[] Allocation = new String[] {
		MsgType.ALLOCATION_INSTRUCTION,
		MsgType.ALLOCATION_INSTRUCTION_ACK,
		MsgType.ALLOCATION_REPORT,
		MsgType.ALLOCATION_REPORT_ACK
	};
	
	public static final String[] Confirmation = new String[] {
		MsgType.CONFIRMATION,
		MsgType.CONFIRMATION_ACK,
		MsgType.CONFIRMATION_REQUEST
	};
	
	public static final String[] SettlementInstructions = new String[] {
		MsgType.SETTLEMENT_INSTRUCTIONS,
		MsgType.SETTLEMENT_INSTRUCTION_REQUEST
	};
	
	public static final String[] TradeCaptureReporting = new String[] {
		MsgType.TRADE_CAPTURE_REPORT_REQUEST,
		MsgType.TRADE_CAPTURE_REPORT_REQUEST_ACK,
		MsgType.TRADE_CAPTURE_REPORT,
		MsgType.TRADE_CAPTURE_REPORT_ACK
	};
	
	public static final String[] RegistrationInstructions = new String[] {
		MsgType.REGISTRATION_INSTRUCTIONS,
		MsgType.REGISTRATION_INSTRUCTIONS_RESPONSE
	};
	
	public static final String[] PositionsManagement = new String[] {
		MsgType.POSITION_MAINTENANCE_REQUEST,
		MsgType.POSITION_MAINTENANCE_REPORT,
		MsgType.REQUEST_FOR_POSITIONS,
		MsgType.REQUEST_FOR_POSITIONS_ACK,
		MsgType.POSITION_REPORT,
		MsgType.ASSIGNMENT_REPORT
	};
	
	public static final String[] CollateralManagement = new String[] {
		MsgType.COLLATERAL_REQUEST,
		MsgType.COLLATERAL_ASSIGNMENT,
		MsgType.COLLATERAL_RESPONSE,
		MsgType.COLLATERAL_REPORT,
		MsgType.COLLATERAL_INQUIRY,
		MsgType.COLLATERAL_INQUIRY_ACK
	};
	
	private Category() {}
}
