package suncertify.ui;

import javax.swing.table.DefaultTableModel;

public class ClientTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1442875306611676732L;

	public ClientTableModel(final Object[][] data, final Object[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return false;
	}

}
