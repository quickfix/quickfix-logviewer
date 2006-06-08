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

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import quickfix.DataDictionary;
import quickfix.StringField;
import quickfix.Group;
import quickfix.IntField;
import quickfix.Message;

public class MessageTreeModel implements TreeModel {
	private String root = new String("Message");
	private String header = new String("Header");
	private String body = new String("Body");
	private String trailer = new String("Trailer");
	
	public class Node {
		private StringField field = null;
		private ArrayList children = new ArrayList();
		
		Node( StringField aField ) {
			field = aField;
		}
		
		public StringField getField() {
			return field;
		}
		
		public void addChild( Node node ) {
			children.add( node );
		}
		
		public ArrayList getChildren() {
			return children;
		}
		
		public String toString() {
			Integer tag = new Integer(field.getField());
			String result = new String();
			result =
				dataDictionary.getFieldName(tag.intValue())
				+ " (" + field.getField() + ")"
				+ " = ";
			
			if( dataDictionary.hasFieldValue(tag.intValue()) ) {
				String value = dataDictionary.getValueName( tag.intValue(), field.getValue() );
				result += field.getObject().toString();
				if( value != null )
					result += " (" + dataDictionary.getValueName(tag.intValue(), field.getValue() ) + ")";
			} else {
				result += field.getObject().toString();				
			}
			return result;
		}
	}
	
	private ArrayList headerFields = new ArrayList();
	private ArrayList bodyFields = new ArrayList();
	private ArrayList trailerFields = new ArrayList();
	
	private Message message = null;
	private DataDictionary dataDictionary = null;
	
	MessageTreeModel( Message aMessage, DataDictionary aDataDictionary ) {
		message = aMessage;
		dataDictionary = aDataDictionary;
		if( message == null ) return;
		
		Node node = null;
		
		Iterator i = message.getHeader().iterator();
		while( i.hasNext() ) {
			node = new Node((StringField)i.next());
			headerFields.add( node );
		}
		
		i = message.iterator();
		while( i.hasNext() ) {
			node = new Node((StringField)i.next());
			bodyFields.add( node );
		}
		addGroups( message, node );
		
		i = message.getTrailer().iterator();
		while( i.hasNext() ) {
			node = new Node((StringField)i.next());
			trailerFields.add( node );
		}
	}
	
	void addGroups( Message message, Node node ) {
		Iterator i = message.iterator();
		while( i.hasNext() ) {
			StringField field = (StringField)i.next();
			try {
				Group group = new Group( field.getField(), 1 );
				IntField integerField = new IntField( field.getField() );
				message.getField( integerField );
				for( int count = 1; count <= integerField.getValue(); ++count ) {
					message.getGroup( count, group );
					Node firstNode = null;
					Iterator j = group.iterator();
					while( j.hasNext() ) {
						field = (StringField)j.next();
						if( firstNode == null ) {
							firstNode = new Node( field );
							continue;
						}
						firstNode.addChild( new Node(field) );
					}
					node.addChild( firstNode );
				}
			} catch( Exception e ) {}
		}
	}

	public Object getRoot() {
		return root;
	}
	
	public Object getHeader() {
		return header;
	}

	public Object getBody() {
		return body;
	}

	public Object getTrailer() {
		return trailer;
	}

	public Object getChild(Object parent, int index) {
		if( parent == root ) {
			switch( index ) {
				case 0: return header;
				case 1: return body;
				case 2: return trailer;
			}
		} else if( parent == header ) {
			return headerFields.get(index);
		} else if( parent == body ) {
			return bodyFields.get(index);
		} else if( parent == trailer ) {
			return trailerFields.get(index);
		} else if( parent.getClass() == Node.class ) {
			Node node = (Node)parent;
			return node.getChildren().get(index);
		}
		return null;
	}

	public int getChildCount(Object parent) {
		if( parent == root ) {
			return 3;
		} else if( parent == header ) {
			return headerFields.size();
		} else if( parent == body ) {
			return bodyFields.size();
		} else if( parent == trailer ) {
			return trailerFields.size();
		} else if( parent.getClass() == Node.class ) {
			Node node = (Node)parent;
			return node.getChildren().size();
		}
		return 0;
	}

	public boolean isLeaf(Object aNode) {
		if( aNode == root ) {
			return false;
		} else if( aNode == header ) {
			return false;
		} else if( aNode == body ) {
			return false;
		} else if( aNode == trailer ) {
			return false;
		} else if( aNode.getClass() == Node.class ) {
			Node node = (Node)aNode;
			return node.getChildren().size() == 0;
		}
		return true;
	}
	
	public boolean isRoot(Object aNode) {
		return aNode == root;
	}

	public boolean isHeader(Object aNode) {
		return aNode == header;
	}

	public boolean isBody(Object aNode) {
		return aNode == body;
	}

	public boolean isTrailer(Object aNode) {
		return aNode == trailer;
	}
	
	public boolean isField(Object aNode) {
		return aNode.getClass() == Node.class;
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}

	public void addTreeModelListener(TreeModelListener l) {
	}

	public void removeTreeModelListener(TreeModelListener l) {
	}
}
