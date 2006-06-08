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

public class FilterType {
	public static final int ALL_MESSAGES = 0;
	public static final int ADMINISTRATIVE_MESSAGES = 1;
	public static final int APPLICATION_MESSAGES = 2;
	public static final int CUSTOM_FILTER = 3;
	public static final int INDICATION_CATEGORY = 4;
	public static final int EVENT_COMMUNICATION_CATEGORY = 5;
	public static final int QUOTATION_NEGOTIATION_CATEGORY = 6;
	public static final int MARKET_DATA_CATEGORY = 7;
	public static final int SECURITY_AND_TRADING_SESSION_CATEGORY = 8;
	public static final int SINGLE_GENERAL_ORDER_HANDLING_CATEGORY = 9;
	public static final int CROSS_ORDERS_CATEGORY = 10;
	public static final int MULTILEG_ORDERS_CATEGORY = 11;
	public static final int LIST_PROGRAM_BASKET_TRADING_CATEGORY = 12;
	public static final int ALLOCATION_CATEGORY = 13;
	public static final int CONFIRMATION_CATEGORY = 14;
	public static final int SETTLEMENT_INSTRUCTIONS_CATEGORY = 15;
	public static final int TRADE_CAPTURE_REPORTING_CATEGORY = 16;
	public static final int REGISTRATION_INSTRUCTIONS_CATEGORY = 17;
	public static final int POSITIONS_MAINTENANCE_CATEGORY = 18;
	public static final int COLLATERAL_MANAGEMENT_CATEGORY = 19;
}
