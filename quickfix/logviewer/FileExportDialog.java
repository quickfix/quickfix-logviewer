package quickfix.logviewer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileExportDialog extends Dialog implements ActionListener {

	private JFileChooser fileChooser = new JFileChooser();

	private static String path = System.getProperty("user.dir");
	private File file = null;
	
	public FileExportDialog(JFrame owner) throws HeadlessException {
		super(owner, "File Export");

		setResizable(false);
		
		getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.weightx = 1.0;
		constraints.weighty = 20.0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		getContentPane().add( fileChooser, constraints );
		
		fileChooser.addActionListener( this );
		fileChooser.setCurrentDirectory( new File(path) );
		fileChooser.setApproveButtonText("Export");
		Dimension size = fileChooser.getPreferredSize();
		size.setSize( size.getWidth() * 1.1, size.getHeight() * 1.25 );
		setSize(size);
	}
	
	public File getFile() {
		return file;
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getActionCommand() == "ApproveSelection" ) {			
			file = fileChooser.getSelectedFile();
			path = file.getPath().toString();
		} else if( e.getActionCommand() == "CancelSelection" ) {
			file = null;
		}
		setVisible( false );
	}
}
