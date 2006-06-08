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

import java.awt.HeadlessException;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class Dialog extends JDialog {

	public Dialog(JFrame owner, String label) throws HeadlessException {
		super(owner, label, true);
		
		setSize( 640, 480 );
	}
	
	public void setSize( int width, int height ) {
		super.setSize( width, height );
		Rectangle ownerBounds = this.getOwner().getBounds();
		Rectangle bounds = getBounds();
		
		int w = bounds.getSize().width;
		int h = bounds.getSize().height;
		int x = ((ownerBounds.getSize().width-w)/2) + ownerBounds.x;
		int y = ((ownerBounds.getSize().height-h)/2) + ownerBounds.y;
		     
		setBounds(x, y, w, h);
	}
}
