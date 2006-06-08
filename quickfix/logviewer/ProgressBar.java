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

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBar extends JProgressBar {

	public ProgressBar() {
		super();
		paintString = true;
		done();
	}
	
	public void setTask( final String name, final int minimum, final int maximum ) {
		if( SwingUtilities.isEventDispatchThread() ) {
			_setTask( name, minimum, maximum );
		} else {
			Runnable runnable = new Runnable() {
				public void run() {
					_setTask( name, minimum, maximum );
				}
			};
			SwingUtilities.invokeLater( runnable );
		}
	}
	
	private void _setTask( final String name, final int minimum, final int maximum ) {
		setString( name );
		setMinimum( minimum );
		setMaximum( maximum );
		setValue( 0 );
	}
	
	public void setValue( final int value ) {
		if( SwingUtilities.isEventDispatchThread() ) {
			_setValue( value );
		} else {
			Runnable runnable = new Runnable() {
				public void run() {
					_setValue( value );
				}
			};
			SwingUtilities.invokeLater( runnable );
		}
	}
	
	private void _setValue( int value ) {
		super.setValue( value );
	}
	
	public void increment() {
		if( SwingUtilities.isEventDispatchThread() ) {
			_increment();
		} else {
			Runnable runnable = new Runnable() {
				public void run() {
					_increment();
				}
			};
			SwingUtilities.invokeLater( runnable );
		}
	}
	
	private void _increment() {
		if( getValue() >= getMaximum() )
			return;
		
		super.setValue( getValue() + 1 );
		if( getValue() == getMaximum() ) {
			done();
		}
	}
	
	public void increment( final int value ) {
		if( SwingUtilities.isEventDispatchThread() ) {
			_increment( value );
		} else {
			Runnable runnable = new Runnable() {
				public void run() {
					_increment( value );
				}
			};
			SwingUtilities.invokeLater( runnable );
		}
	}
	
	private void _increment( int value ) {
		if( getValue() >= getMaximum() )
			return;
		
		super.setValue( getValue() + value );
		if( getValue() >= getMaximum() ) {
			done();
		}
	}
	
	public void done() {
		if( SwingUtilities.isEventDispatchThread() ) {
			_done();
		} else {
			Runnable runnable = new Runnable() {
				public void run() {
					_done();
				}
			};
			SwingUtilities.invokeLater( runnable );
		}
	}
	
	private void _done() {
		super.setValue( 0 );
		setString( "" );
	}	
}
