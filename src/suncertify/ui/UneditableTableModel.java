package suncertify.ui;

import javax.swing.table.DefaultTableModel;

public class UneditableTableModel extends DefaultTableModel {

	public UneditableTableModel(final Object[][] data,
			final Object[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return false;
	}

}
