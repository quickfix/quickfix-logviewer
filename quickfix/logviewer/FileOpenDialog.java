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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import quickfix.DataDictionary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class FileOpenDialog extends Dialog implements ActionListener, PropertyChangeListener {
	private DataDictionary dataDictionary = null;
	private JFileChooser fileChooser = new JFileChooser();
	private JLabel label = new JLabel("Time Range");
	private JSpinner startTimeControl = null;
	private JSpinner endTimeControl = null;
	private Date selectedStartTime = null;
	private Date selectedEndTime = null;
	private Date startTime = null;
	private Date endTime = null;
	
	private static String path = System.getProperty("user.dir");
	private File file = null;
	
	public FileOpenDialog(JFrame owner, DataDictionary dataDictionary) throws HeadlessException {	
		super(owner, "File Open");

		setResizable(false);
		
		getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.weightx = 1.0;
		constraints.weighty = 20.0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		getContentPane().add( fileChooser, constraints );
		
		Dimension size = fileChooser.getPreferredSize();
		size.setSize( size.getWidth() * 1.1, size.getHeight() * 1.25 );
		setSize(size);
		
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.gridwidth = 1;
		getContentPane().add( new JLabel(), constraints );

		constraints.weightx = 1.0;
		constraints.gridwidth = 10;
		startTimeControl = new JSpinner(new SpinnerDateModel());
		startTimeControl.setEditor(new JSpinner.DateEditor(startTimeControl, "HH:mm:ss MM/dd/yyyy"));
		startTimeControl.setValue(new Date());
		getContentPane().add( startTimeControl, constraints );

		constraints.weightx = 1.0;
		constraints.gridwidth = 1;
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		getContentPane().add( label, constraints );

		constraints.weightx = 1.0;
		constraints.gridwidth = 10;
		endTimeControl = new JSpinner(new SpinnerDateModel());
		endTimeControl.setEditor(new JSpinner.DateEditor(endTimeControl, "HH:mm:ss MM/dd/yyyy"));
		endTimeControl.setValue(new Date());
		getContentPane().add( endTimeControl, constraints );
		
		constraints.weightx = 1.0;
		constraints.gridwidth = 1;
		getContentPane().add( new JLabel(), constraints );

		fileChooser.setCurrentDirectory( new File(path) );
		fileChooser.addActionListener( this );
		fileChooser.addPropertyChangeListener( this );
	
		setSize( 640, 480 );
	}
	
	public File getFile() {
		return file;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getActionCommand() == "ApproveSelection" ) {
			selectedStartTime = (Date)startTimeControl.getValue();
			selectedEndTime = (Date)endTimeControl.getValue();
			
			roundDate(startTime, true);
			roundDate(endTime, false);
			roundDate(selectedStartTime, true);
			roundDate(selectedEndTime, false);
			
			file = fileChooser.getSelectedFile();
			path = file.getPath().toString();

			if( startTime != null && startTime.equals(selectedStartTime))
				startTime = null;
			else
				startTime = selectedStartTime;
			if( endTime != null && endTime.equals(selectedEndTime))
				endTime = null;
			else
				endTime = selectedEndTime;
		} else if( e.getActionCommand() == "CancelSelection" ) {
			file = null;
		}
		setVisible( false );
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if( !evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) )
			return;
		File file = fileChooser.getSelectedFile();
		if( file == null || file.isDirectory() )
			return;
		
		try {
			LogFile logFile = new LogFile( fileChooser.getSelectedFile(), dataDictionary );
			startTime = logFile.getStartTime();
			roundDate( startTime, true );
			if( startTime != null ) startTimeControl.setValue( startTime );
			endTime = logFile.getEndTime();
			roundDate( endTime, false );
			if( endTime != null ) endTimeControl.setValue( endTime );
		} catch (FileNotFoundException e1) {
		}
	}

	public Date roundDate( Date date, boolean roundDown ) {
		if( date == null ) return date;
		long time = date.getTime();
		long diff = (time % 1000);
		if( diff != 0 ) diff = 1000 - diff;
		time += diff;
		date.setTime( time );
		
		if( date.getMinutes() == 0 || date.getSeconds() == 0 )
			return date;
		date.setMinutes( 0 );
		date.setSeconds( 0 );
		
		if( roundDown == false )
			date.setTime( date.getTime() + 1000 * 60 * 60 );

		return date;
	}
}
