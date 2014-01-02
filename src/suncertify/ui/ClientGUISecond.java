package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ClientGUISecond extends JFrame {

	public ClientGUISecond() {

		initUI();
	}

	public final void initUI() {

		final JMenuBar menubar = new JMenuBar();
		final JMenu file = new JMenu("File");

		menubar.add(file);
		setJMenuBar(menubar);

		final JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		final ImageIcon exit = new ImageIcon("exit.png");
		final JButton bexit = new JButton(exit);
		bexit.setBorder(new EmptyBorder(0, 0, 0, 0));
		toolbar.add(bexit);

		add(toolbar, BorderLayout.NORTH);

		final JToolBar vertical = new JToolBar(SwingConstants.VERTICAL);
		vertical.setFloatable(false);
		vertical.setMargin(new Insets(10, 5, 5, 5));

		final ImageIcon select = new ImageIcon("drive.png");
		final ImageIcon freehand = new ImageIcon("computer.png");
		final ImageIcon shapeed = new ImageIcon("printer.png");

		final JButton selectb = new JButton(select);
		selectb.setBorder(new EmptyBorder(3, 0, 3, 0));

		final JButton freehandb = new JButton(freehand);
		freehandb.setBorder(new EmptyBorder(3, 0, 3, 0));
		final JButton shapeedb = new JButton(shapeed);
		shapeedb.setBorder(new EmptyBorder(3, 0, 3, 0));

		vertical.add(selectb);
		vertical.add(freehandb);
		vertical.add(shapeedb);

		add(vertical, BorderLayout.WEST);

		add(new JTextArea(), BorderLayout.CENTER);

		final JLabel statusbar = new JLabel(" Statusbar");
		statusbar.setPreferredSize(new Dimension(-1, 22));
		statusbar.setBorder(LineBorder.createGrayLineBorder());
		add(statusbar, BorderLayout.SOUTH);

		setSize(350, 300);
		setTitle("BorderLayout");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final ClientGUISecond ex = new ClientGUISecond();
				ex.setVisible(true);
			}
		});
	}
}