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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import quickfix.DataDictionary;
import quickfix.StringField;

public class CustomFilterDialog extends Dialog implements ActionListener {
	private ButtonPanel buttonPanel = new ButtonPanel();
	private ArrayList filterPanels = new ArrayList();
	private ArrayList filter = null;
	
	class ComboBoxItem extends Object {
		private Integer tag = null;
		private DataDictionary dataDictionary = null;
		
		ComboBoxItem( Integer aTag, DataDictionary aDataDictionary ) {
			tag = aTag;
			dataDictionary = aDataDictionary;
		}
		
		public String toString() {
			return dataDictionary.getFieldName( tag.intValue() ) + "(" + tag + ")";
		}
	}
	
	class OperatorComboBox extends JComboBox {
		OperatorComboBox() {
			this.addItem( "=" );
			this.addItem( "!=" );
			this.addItem( "<" );
			this.addItem( "<=" );
			this.addItem( ">");
			this.addItem( ">=" );
		}
	}
	
	class FilterPanel extends JPanel implements ChangeListener {
		private JCheckBox checkBox = new JCheckBox();
		private JComboBox comboBox = new JComboBox();
		private JComboBox operatorComboBox = new OperatorComboBox();
		private JTextField textField = new JTextField();
				
		FilterPanel( SortedSet tags, DataDictionary dataDictionary ) {
			
			setLayout( new FlowLayout() );
			enablePanel( false );
			
			checkBox.setPreferredSize( new Dimension(25, 25) );
			add( checkBox );
			comboBox.setPreferredSize( new Dimension(200, 25) );
			add( comboBox );
			operatorComboBox.setPreferredSize( new Dimension(50, 25) );
			add( operatorComboBox );
			textField.setPreferredSize( new Dimension(275, 25) );
			add( textField );
			add( new JLabel() );
			
			TreeSet sortedTags = new TreeSet(tags);
			Iterator i = sortedTags.iterator();
			while( i.hasNext() ) {
				comboBox.addItem( new ComboBoxItem((Integer)i.next(), dataDictionary ));
			}
			
			checkBox.addChangeListener( this );
		}

		private void enablePanel(boolean b) {
			comboBox.setEnabled(b);
			textField.setEnabled(b);
		}

		public void stateChanged(ChangeEvent e) {
			enablePanel( checkBox.isSelected() );
		}

		public void set(FieldFilter fieldFilter) {
			for( int i = 0; i < comboBox.getItemCount(); ++i ) {
				ComboBoxItem item = (ComboBoxItem)comboBox.getItemAt(i);
				if( item.tag.intValue() == fieldFilter.getTag() ) {
					comboBox.setSelectedIndex( i );
					operatorComboBox.setSelectedIndex( fieldFilter.getOperator() );
					textField.setText( fieldFilter.getValue() );
					checkBox.setSelected(true);
				}
			}
		}
	}
	
	class ButtonPanel extends JPanel {
		private JButton cancel = new JButton("Cancel");
		private JButton apply = new JButton("Apply");
		
		ButtonPanel() {
			setLayout( new GridBagLayout() );
			
			GridBagConstraints constraints = new GridBagConstraints();
			add( cancel, constraints );
			add( apply, constraints );
		}
	}
	
	public CustomFilterDialog(JFrame owner, ArrayList filter, SortedSet tags, DataDictionary dataDictionary) throws HeadlessException {
		super(owner, "Custom Filter");
		
		int rows = 10;
		Dimension dimension = new Dimension();
		dimension.height = rows * 35 + 60;
		dimension.width = 600;
		setSize( dimension );
		setResizable( false );
		
		getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		
		for( int i = 1; i <= rows; ++i ) {
			constraints.weightx = 10;
			constraints.gridx = 1;
			constraints.weighty = 1;
			constraints.gridy = i;
			FilterPanel filterPanel = new FilterPanel( tags, dataDictionary );
			if( filter.size() >= i ) {
				FieldFilter fieldFilter = (FieldFilter)filter.get(i-1);
				filterPanel.set( fieldFilter );
			}
			filterPanels.add( filterPanel );
			getContentPane().add( filterPanel, constraints );
		}
		
		constraints.weighty = 1;
		constraints.gridy = rows+1;
		constraints.weightx = 10;
		constraints.gridx = 1;
		getContentPane().add( buttonPanel, constraints );
		
		buttonPanel.cancel.addActionListener( this );
		buttonPanel.apply.addActionListener( this );
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == buttonPanel.cancel ) {
			setVisible( false );
			filter = null;
		} else if( e.getSource() == buttonPanel.apply ) {
			filter = new ArrayList();
			Iterator i = filterPanels.iterator();
			while( i.hasNext() ) {
				FilterPanel filterPanel = (FilterPanel)i.next();
				if( filterPanel.checkBox.isSelected() ) {
					ComboBoxItem item = (ComboBoxItem)filterPanel.comboBox.getSelectedItem();
					int tag = item.tag.intValue();
					String value = filterPanel.textField.getText();
					FieldFilter fieldFilter = new FieldFilter
					  ( new StringField(tag, value), 
					  		convertOperator((String)filterPanel.operatorComboBox.getSelectedItem()));
					filter.add( fieldFilter );
				}
			}
			setVisible( false );
		}
	}

	public static int convertOperator( String stringOperator ) {
		if( stringOperator.equals("=") )
			return FieldFilter.EQUAL;
		if( stringOperator.equals("!=") )
			return FieldFilter.NOT_EQUAL;
		if( stringOperator.equals("<") )
			return FieldFilter.LESS_THAN;
		if( stringOperator.equals("<=") )
			return FieldFilter.LESS_THAN_OR_EQUAL;
		if( stringOperator.equals(">") )
			return FieldFilter.GREATER_THAN;
		if( stringOperator.equals(">=") )
			return FieldFilter.GREATER_THAN_OR_EQUAL;
		return FieldFilter.EQUAL;
	}
		
	public ArrayList getFilter() {
		return filter;
	}
}
