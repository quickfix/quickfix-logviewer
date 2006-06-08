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

import quickfix.StringField;

public class FieldFilter {

	public final static int EQUAL = 0;
	public final static int NOT_EQUAL = 1;
	public final static int LESS_THAN = 2;
	public final static int LESS_THAN_OR_EQUAL = 3;
	public final static int GREATER_THAN = 4;
	public final static int GREATER_THAN_OR_EQUAL = 5;
	
	private StringField field = null;
	private int operator = 0;
	
	public FieldFilter( StringField aField, int aOperator ) {
		field = aField;
		operator = aOperator;
	}

	public StringField getField() {
		return field;
	}
	
	public int getTag() {
		return field.getField();
	}

	public String getValue() {
		return field.getValue();
	}
	
	public int getOperator() {
		return operator;
	}
}
