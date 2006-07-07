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
import java.awt.Point;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import quickfix.StringField;

public class MessageTree extends JTree {

	private static final long serialVersionUID = 1L;
	private boolean firstModel = true;
	
	MessageTree() {
	}
	
	public void setModel(MessageTreeModel tm) {
		if( tm == null ) {
			super.setModel( null );
			return;
		}
		Object[] parentObjects = new Object[1];
		Object[] childObjects = new Object[2];
		
		boolean rootExpanded = false;
		boolean headerExpanded = false;
		boolean bodyExpanded = false;
		boolean trailerExpanded = false;

		if( !firstModel ) {
			parentObjects[0] = tm.getRoot();
			childObjects[0] = tm.getRoot();
			rootExpanded = isExpanded( new TreePath(parentObjects) );
			childObjects[1] = tm.getHeader();
			headerExpanded = isExpanded( new TreePath(childObjects) );
			childObjects[1] = tm.getBody();
			bodyExpanded = isExpanded( new TreePath(childObjects) );
			childObjects[1] = tm.getTrailer();
			trailerExpanded = isExpanded( new TreePath(childObjects) );
		}

		super.setModel( tm );

		parentObjects[0] = tm.getRoot();
		childObjects[0] = tm.getRoot();
		if( rootExpanded ) expandPath( new TreePath(parentObjects) );
		childObjects[1] = tm.getHeader();
		if( headerExpanded ) expandPath( new TreePath(childObjects) );
		childObjects[1] = tm.getBody();
		if( bodyExpanded || firstModel ) expandPath( new TreePath(childObjects) );
		childObjects[1] = tm.getTrailer();
		if( trailerExpanded ) expandPath( new TreePath(childObjects) );

		firstModel = false;
	}
	
	public StringField getFieldFromPoint( Point point ) {
		MessageTreeModel tm = (MessageTreeModel)getModel();
		int row = getRowForLocation( (int)point.getX(), (int)point.getY() );
		TreePath path = getPathForLocation( (int)point.getX(), (int)point.getY() );
		if( row == -1 ) return null;
		try {
			MessageTreeModel.Node node =
				(MessageTreeModel.Node)path.getPathComponent(2);
			if( !tm.isField(node) ) return null;
			return node.getField();
		} catch( Exception e ) {
			return null;
		}
	}
	
	public Integer getTagFromPoint( Point point ) {
		StringField field = getFieldFromPoint( point );
		if( field == null ) return null;
		return new Integer(field.getField());
    }
    
    public String getValueFromPoint( Point point ) {
		StringField field = getFieldFromPoint( point );
		if( field == null ) return null;
		return field.getValue();
    }
}