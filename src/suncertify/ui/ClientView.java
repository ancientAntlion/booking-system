package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import suncertify.server.exceptions.BookingServiceException;
import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.InvalidModeException;
import suncertify.ui.exceptions.ServiceUnavailableException;

/**
 * The ClientView contains all the UI & swing components, as well as the actionlisteners attached
 * To some of those components. It's function is to display a UI to the user, and fire requests from
 * The user up to the controller to be handled.
 * 
 * @author Aaron
 *
 */
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
	 * 
	 * @param mode
	 * @throws ServiceUnavailableException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws BookingServiceException
	 */
	public ClientView(final Mode mode) throws ServiceUnavailableException, RemoteException, NotBoundException, BookingServiceException {

		// Set properties
		this.setTitle("URLyBird");
		this.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		this.setBackground(Color.GRAY);
		this.setResizable(false);
		
		// Initialize all components
		mainPanel = new JPanel();
		controller = new ClientController(mode);
		
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
		
		table = initTable(tableModel);
		table.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		scrollPane = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		bookButton = createBookButton();
		unBookButton = createUnBookButton();
		searchButton = createSearchButton();

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

		topPanel.setLayout(new GridLayout(2, 3, 10, 0));
		topPanel.setBorder(new EmptyBorder(new Insets(5, 130, 5, 0)));

		topPanel.add(nameSearchLabel);
		topPanel.add(locationSearchLabel);
		topPanel.add(whitespaceLabel);
		topPanel.add(nameSearchBar);
		topPanel.add(locationSearchBar);
		topPanel.add(searchButtonPanel);

		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(new EmptyBorder(new Insets(15, 10, 5, 10)));
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

	/**
	 * Initializes the table
	 * 
	 * @param tableModel
	 * @return
	 */
	private JTable initTable(final TableModel tableModel) {
		return new JTable(tableModel);
	}

	/**
	 * Requests a tablemodel from the controller and uses it in our table, which updates our table
	 * 
	 * @param name
	 * @param location
	 * @throws ServiceUnavailableException
	 * @throws BookingServiceException
	 */
	private void updateTable(final String name, final String location) throws ServiceUnavailableException, BookingServiceException {
		
		if (name == null && location == null) {
			tableModel = controller.getAllEntries();
		} else {
			tableModel = controller.getSpecificEntries(name, location);
		}
		
		table.setModel(tableModel);
	}

	/**
	 * Initializes the bookButton and attaches an ActionListener to it
	 * 
	 * @return bookButton
	 */
	private JButton createBookButton() {
		final JButton bookButton = new JButton("Create Booking");

		bookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleBooking();
			}
		});

		return bookButton;
	}

	/**
	 * Initializes the unbookButton and attaches an ActionListener to it
	 * 
	 * @return unbookButton
	 */
	private JButton createUnBookButton() {
		final JButton unBookButton = new JButton("Remove Booking");

		unBookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleUnbooking();
			}
		});

		return unBookButton;
	}

	/**
	 * Initializes the searchButton and attaches an ActionListener to it
	 * 
	 * @return searchButton
	 */
	private JButton createSearchButton() {
		final JButton searchButton = new JButton("Search");

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				try{
					updateTable(nameSearchBar.getText(), locationSearchBar.getText());
				}catch(final BookingServiceException bse){
					JOptionPane.showMessageDialog(null, bse.getMessage());
					System.exit(0);
				}catch(final ServiceUnavailableException sue){
					JOptionPane.showMessageDialog(null, sue.getMessage());
					System.exit(0);
				}
			}
		});

		return searchButton;
	}

	/**
	 * Sends the request for a booking up to the controller and updates our table afterward
	 */
	private void handleBooking() {
		String customerID;
		int recNo = table.getSelectedRow();
		if(recNo == -1){
			return;
		}
		
		try {
			customerID = JOptionPane.showInputDialog(mainPanel,
					"Enter Customer ID (8 Digits)");
			if (customerID != null) {	
				controller.reserveRoom(recNo, customerID);
	
				updateTable(nameSearchBar.getText(),
						locationSearchBar.getText());
			}
		} catch (final InvalidCustomerIDException icide) {
			JOptionPane.showMessageDialog(mainPanel, "Invalid format!");
		} catch (final BookingServiceException bse) {
			JOptionPane.showMessageDialog(mainPanel, bse.getMessage());
		} catch (final ServiceUnavailableException sue) {
			JOptionPane.showMessageDialog(null, sue.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * Sends the request for an unbooking up to the controller and updates our table afterward
	 */
	private void handleUnbooking() {
		try {
			int recNo = table.getSelectedRow();
			if(recNo == -1){
				return;
			}

			controller.unreserveRoom(recNo);
			
			updateTable(nameSearchBar.getText(), locationSearchBar.getText());
		} catch (BookingServiceException bse) {
			JOptionPane.showMessageDialog(mainPanel,
					"Record has not been booked!");
		} catch (ServiceUnavailableException sue) {
			JOptionPane.showMessageDialog(null, sue.getMessage());
			System.exit(0);
		}
		
	}
	
}
