package suncertify.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.ui.records.Record;

public class ClientTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1111923761540802741L;
	private static final String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate", "Date", "Owner" };
    protected List<String[]> recordList;

    public ClientTableModel(List<String[]> recordList)
    {
        this.recordList = recordList;
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int columnIndex)
    {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        if(row < 0 || row >= recordList.size()){
        	return null;
        }        
        
        String[] record = recordList.get(row);
        
        return record[column];
    }

    @Override
    public int getRowCount()
    {
        return recordList.size();
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

}
