package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import suncertify.server.DBConnection;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 8980678964743452622L;

	private static final int JFRAME_WIDTH = 950;

	private static final int JFRAME_HEIGHT = 475;

	private final DBConnection dbConnection = new DBConnection();

	String[] columnNames = { "First Name", "Last Name", "Sport", "# of Years",
			"Vegetarian", "aaaa", "bbb" };

	/**
	 * The constructor for the client GUI
	 */
	public ClientGUI() {

		// Set properties
		setTitle("Test Application");
		setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		setBackground(Color.GRAY);

		// Initialize all components
		final JPanel mainPanel = new JPanel();

		final JPanel topPanel = new JPanel();
		final JPanel sidePanel = new JPanel();
		final JPanel tablePanel = new JPanel();
		final JPanel buttonPanel = new JPanel();
		final JPanel searchButtonPanel = new JPanel();
		final JTextField nameSearchBar = createNameSearchBar();
		final JTextField locationSearchBar = createLocationSearchBar();

		final JTable table = initTable();
		final JScrollPane scrollPane = new JScrollPane(table);
		// scrollPane.requestFocusInWindow();

		final JButton bookButton = createBookButton();
		final JButton unBookButton = createUnBookButton();
		final JButton searchButton = createSearchButton();

		// addButton.requestFocusInWindow();

		table.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		searchButtonPanel.setLayout(new BorderLayout());
		searchButtonPanel.add(searchButton, BorderLayout.WEST);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
		mainPanel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(new Insets(25, 0, 15, 50)));
		buttonPanel.add(bookButton);
		buttonPanel.add(unBookButton);

		sidePanel.setLayout(new BorderLayout());
		sidePanel.add(buttonPanel, BorderLayout.NORTH);

		topPanel.setLayout(new GridLayout(1, 3, 0, 0));
		// topPanel.setPreferredSize(new Dimension(700, 100));
		topPanel.add(nameSearchBar);
		topPanel.add(locationSearchBar);
		topPanel.add(searchButtonPanel);

		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(new EmptyBorder(new Insets(15, 0, 15, 50)));
		// tablePanel.setPreferredSize(new Dimension(700, 475));
		tablePanel.add(scrollPane);

		final JToolBar vertical = new JToolBar(SwingConstants.VERTICAL);
		vertical.setPreferredSize(new Dimension(100, 200));
		vertical.setFloatable(false);
		vertical.setMargin(new Insets(10, 5, 5, 5));

		mainPanel.add(topPanel, BorderLayout.PAGE_START);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(sidePanel, BorderLayout.EAST);

		getContentPane().add(mainPanel);

		pack();

		scrollPane.requestFocusInWindow();

	}

	public static void main(final String[] args) {
		final ClientGUI clientGUI = new ClientGUI();
		clientGUI.setVisible(true);

	}

	private JTable initTable() {
		final Object[][] dbEntries = dbConnection.getAllEntries();

		final JTable table = new JTable(new UneditableTableModel(dbEntries,
				columnNames));

		// table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setFillsViewportHeight(true);

		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(false);

		return table;
	}

	private JButton createBookButton() {
		final JButton bookButton = new JButton("Create Booking");

		bookButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// Execute when button is pressed
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
				// Execute when button is pressed
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
				// Execute when button is pressed
				System.out.println("Search Button Clicked");
			}
		});

		return searchButton;
	}

	private JTextField createNameSearchBar() {
		final JTextField nameSearchBar = new JTextField("Name");

		nameSearchBar.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent fEvt) {
				final JTextField tField = (JTextField) fEvt.getSource();
				tField.selectAll();
			}
		});

		return nameSearchBar;
	}

	private JTextField createLocationSearchBar() {
		final JTextField locationSearchBar = new JTextField("Location");

		locationSearchBar.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent fEvt) {
				final JTextField tField = (JTextField) fEvt.getSource();
				tField.selectAll();
			}
		});

		return locationSearchBar;
	}

}
