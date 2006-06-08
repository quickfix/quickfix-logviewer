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
import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import quickfix.DataDictionary;

public class Frame extends JFrame {
	
	public Frame( DataDictionary dataDictionary ) throws HeadlessException {
		super();		
		
		ProgressBarPanel progressBarPanel = new ProgressBarPanel( new ProgressBar() );
		MenuBar menuBar = new MenuBar();
		setJMenuBar( menuBar );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		SplitPane pane = new SplitPane( 
				this, 
				progressBarPanel, 
				new MessageTable(), new MessageTree(), 
				new JTabbedPane(), new JTabbedPane(), 
				dataDictionary );
		
		pane.setDividerLocation(300);
		
        getContentPane().setLayout(new BorderLayout());
		getContentPane().add( pane, BorderLayout.CENTER );
		getContentPane().add( progressBarPanel, BorderLayout.PAGE_END );
		setTitle( null );
		setSize( 800, 600 );
        fileLoaded( false );
        menuBar.addActionListener(pane);
	}

	public void fileLoaded(boolean value) {
		if( !value )
			super.setTitle( "QuickFIX Log Viewer (no file loaded)" );
		else
			super.setTitle("QuickFIX Log Viewer");
	}
}
