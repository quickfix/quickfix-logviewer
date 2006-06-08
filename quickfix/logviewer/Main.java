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
import quickfix.*;

import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.swing.UIManager;

public class Main {

	static {
		TimeZone.setDefault( new SimpleTimeZone(SimpleTimeZone.UTC_TIME,"GMT") );
	}
	
	public static void main( String[] args ) throws Exception {
		
		if( args.length != 1 ) {
			System.out.println( "Usage: " + "FIXLogViewer dictionary");
			return;
		}
				
		String dictionaryFileName = args[0];
		
		DataDictionary dataDictionary =
			new DataDictionary( dictionaryFileName );
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch( Exception e ) {
		}
		
		Frame frame = new Frame( dataDictionary );
		frame.setVisible( true );
 	}
}
