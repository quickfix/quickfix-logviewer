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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ProgressBarPanel extends Panel implements ActionListener {

	private ProgressBar progressBar = null;
	private JButton cancelButton = new JButton("Cancel");
	private boolean cancelled = false;
	
	public ProgressBarPanel( ProgressBar aProgressBar ) throws HeadlessException {
		super();
		progressBar = aProgressBar;

		cancelButton.addActionListener( this );
		
		setLayout( new GridBagLayout() );
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		constraints.weightx = 1;
		add( cancelButton, constraints );
		constraints.weightx = 25;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		add( progressBar, constraints );
		cancelButton.setVisible( false );
		cancelled = false;
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	public void setMaximum( int value ) {
		progressBar.setMaximum( value );
	}
	
	public void setTask( String name, int minimum, int maximum, boolean cancel ) {
		cancelled = false;
		cancelButton.setVisible( cancel );
		progressBar.setTask( name, minimum, maximum );
	}
	
	public void setValue( int value ) {
		progressBar.setValue( value );
	}
	
	public boolean increment() {
		progressBar.increment();
		if( cancelled ) done();
		return !cancelled;
	}
	
	public boolean increment( int value ) {
		progressBar.increment( value );
		if( cancelled ) done();
		return !cancelled;
	}
		
	public void done() {
		cancelButton.setVisible( false );
		progressBar.done();
	}

	public void actionPerformed(ActionEvent e) {
		cancelled = true;
	}
}
