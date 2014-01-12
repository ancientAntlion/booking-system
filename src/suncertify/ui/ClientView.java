package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.RecordAlreadyBookedException;
import suncertify.ui.exceptions.RecordNotBookedException;

public class ClientView extends JFrame {

	private static final long serialVersionUID = 8980678964743452622L;

	private static final int JFRAME_WIDTH = 950;

	private static final int JFRAME_HEIGHT = 475;

	private final ClientController controller;

	final JPanel mainPanel;
	final JPanel topPanel;
	final JPanel bottomPanel;
	final JPanel tablePanel;
	final JPanel buttonPanel;
	final JPanel searchButtonPanel;
	final JTextField nameSearchBar;
	final JTextField locationSearchBar;
	final JLabel nameSearchLabel;
	final JLabel locationSearchLabel;
	final JLabel whitespaceLabel;
	final JTable table;
	TableModel tableModel;
	final JScrollPane scrollPane;
	final JButton bookButton;
	final JButton unBookButton;
	final JButton searchButton;

	/**
	 * The constructor for the client GUI
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	public ClientView() throws RemoteException, NotBoundException {

		// Set properties
		setTitle("URLyBird");
		setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		setBackground(Color.GRAY);
		
		// Initialize all components
		mainPanel = new JPanel();

		controller = new ClientController(Mode.LOCAL);
		
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		tablePanel = new JPanel();
		buttonPanel = new JPanel();
		searchButtonPanel = new JPanel();
		nameSearchBar = new JTextField();
		locationSearchBar = new JTextField();
		nameSearchLabel = new JLabel("Name");
		locationSearchLabel = new JLabel("Location");
		whitespaceLabel = new JLabel();

		whitespaceLabel.setVisible(false);

		tableModel = controller.getAllEntries();
		table = initTable();
		scrollPane = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		bookButton = createBookButton();
		unBookButton = createUnBookButton();
		searchButton = createSearchButton();

		table.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		searchButtonPanel.setLayout(new BorderLayout());
		searchButtonPanel.add(searchButton, BorderLayout.WEST);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
		mainPanel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		buttonPanel.setLayout(new GridLayout(1, 2, 50, 0));
		buttonPanel.setBorder(new EmptyBorder(new Insets(25, 0, 15, 50)));
		buttonPanel.add(bookButton);
		buttonPanel.add(unBookButton);

		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBorder(new EmptyBorder(new Insets(10, 150, 10, 150)));
		bottomPanel.add(buttonPanel, BorderLayout.CENTER);

		// sidePanel.setLayout(new BorderLayout());
		// sidePanel.add(buttonPanel, BorderLayout.NORTH);

		topPanel.setLayout(new GridLayout(2, 3, 10, 0));
		topPanel.setBorder(new EmptyBorder(new Insets(5, 130, 5, 0)));
		// topPanel.setPreferredSize(new Dimension(700, 100));
		topPanel.add(nameSearchLabel);
		topPanel.add(locationSearchLabel);
		topPanel.add(whitespaceLabel);
		topPanel.add(nameSearchBar);
		topPanel.add(locationSearchBar);
		topPanel.add(searchButtonPanel);

		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(new EmptyBorder(new Insets(15, 10, 5, 10)));
		// tablePanel.setPreferredSize(new Dimension(700, 475));
		tablePanel.add(scrollPane);

		final JToolBar vertical = new JToolBar(SwingConstants.VERTICAL);
		vertical.setPreferredSize(new Dimension(100, 200));
		vertical.setFloatable(false);
		vertical.setMargin(new Insets(10, 5, 5, 5));

		mainPanel.add(topPanel, BorderLayout.PAGE_START);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

		getContentPane().add(mainPanel);

		pack();

		scrollPane.requestFocusInWindow();

	}

	public static void main(final String[] args) {
		try{
			final ClientView clientGUI = new ClientView();
			clientGUI.setVisible(true);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Exception encountered : " + e);
//			JOptionPane.showInputDialog("Exception encountered : " + e);
		}

	}

	private JTable initTable() {
		return new JTable(tableModel);
	}

	private void updateTable(final String name, final String location) {
		
		if (name == null && location == null) {
			tableModel = controller.getAllEntries();
		} else {
			tableModel = controller.getSpecificEntries(name, location);
		}

		table.setModel(tableModel);
	}

	private JButton createBookButton() {
		final JButton bookButton = new JButton("Create Booking");

		bookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleBooking();

				System.out.println("Create Booking Button Clicked");
			}
		});

		return bookButton;
	}

	private JButton createUnBookButton() {
		final JButton unBookButton = new JButton("Remove Booking");

		unBookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleUnbooking();
				
				System.out.println("Remove Booking Button Clicked");
			}
		});

		return unBookButton;
	}

	private JButton createSearchButton() {
		final JButton searchButton = new JButton("Search");

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				System.out.println("Search Button Clicked");
				updateTable(nameSearchBar.getText(), locationSearchBar.getText());
			}
		});

		return searchButton;
	}

	private void handleBooking() {
		boolean inputAccepted = false;
		String customerID;
		int recNo;
		while (!inputAccepted) {
			try {
				customerID = JOptionPane
						.showInputDialog(mainPanel, "Enter Customer ID (8 Digits)");
				if(customerID == null){
					break;
				}
				//TODO using selected row is not good enough when results are filtered, fix this
				recNo = table.getSelectedRow();

				controller.reserveRoom(recNo, customerID);
				
				updateTable(nameSearchBar.getText(), locationSearchBar.getText());
				
				inputAccepted = true;
			} catch (final InvalidCustomerIDException e) {
				JOptionPane.showMessageDialog(mainPanel, "Invalid format!");
			} catch (final RecordAlreadyBookedException e) {
				JOptionPane.showMessageDialog(mainPanel,
						"Record already booked!");
			}
		}
	}
	
	private void handleUnbooking() {
		try {
			//TODO using selected row is not good enough when results are filtered, fix this
			int recNo = table.getSelectedRow();

			controller.unreserveRoom(recNo);
			
			updateTable(nameSearchBar.getText(), locationSearchBar.getText());
		} catch (final RecordNotBookedException e) {
			JOptionPane.showMessageDialog(mainPanel,
					"Record has not been booked!");
		}
		
	}
	
}
