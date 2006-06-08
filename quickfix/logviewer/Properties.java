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

public class Properties extends java.util.Properties {
	
	final static int MAJOR_VERSION = 1;
	final static int MINOR_VERSION = 0;
	final static int REVISION = 0;
	
	static private Properties instance = null;
	
	static public Properties getInstance() {
		if( instance == null )
			instance = new Properties();
		return instance;
	}
	
	private Properties() {
		this.setProperty( "majorVersion", String.valueOf(majorVersion()) );
		this.setProperty( "minorVersion", String.valueOf(minorVersion()) );
		this.setProperty( "revision", String.valueOf(revision()) );
		this.setProperty( "version", version() );
	}
	
	public int majorVersion() {
		return MAJOR_VERSION;
	}
	
	public int minorVersion() {
		return MINOR_VERSION;
	}
	
	public int revision() {
		return REVISION;
	}
	
	public String version() {
		String majorVersion = String.valueOf( MAJOR_VERSION );
		String minorVersion = String.valueOf( MINOR_VERSION );
		String revision = String.valueOf( REVISION );
		return majorVersion + "." + minorVersion + "." + revision;
	}
}
