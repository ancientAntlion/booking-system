package suncertify.ui;

import java.awt.*;

import javax.swing.*;

import suncertify.server.DBConnection;

public class ClientGUI extends JFrame {
	
	private static final long serialVersionUID = 8980678964743452622L;
	
	private static final int JFRAME_WIDTH = 650;
	
	private static final int JFRAME_HEIGHT = 275;
	
	private final DBConnection dbConnection = new DBConnection();
	
	String[] columnNames = {"First Name",
            "Last Name",
            "Sport",
            "# of Years",
            "Vegetarian",
            "aaaa",
            "bbb"};
	
	/**
	 * The constructor for the client GUI
	 */
	public ClientGUI(){
		
		setTitle( "Test Application" );
		setSize( JFRAME_WIDTH, JFRAME_HEIGHT );
		setBackground( Color.GRAY );

		
		
		Panel topPanel = new Panel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		
		JTable table = initTable();
				
		JScrollPane scrollPane = new JScrollPane(table);

		topPanel.add(table);
	}

	public static void main(String[] args) {
		ClientGUI clientGUI	= new ClientGUI();
		clientGUI.setVisible(true);

	}
	
	private JTable initTable(){
		Object[][] dbEntries = dbConnection.getAllEntries();
		
		final JTable table = new JTable(dbEntries, columnNames);
		
		table.setCellSelectionEnabled(true);
		table.setFillsViewportHeight(true);
		
		return table;
	}
	

}
