package suncertify.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.shared.model.Record;

/**
 * The ClientTableModel is the model that is update which shows the client what they have requested.
 * 
 * This class contains a list of records and overrides some AbstractTableModel methods so that
 * A table with this model in place will display data from this list of records.
 * 
 * @author Aaron
 */
public class ClientTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1111923761540802741L;
	
	private static final String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate", "Date", "Owner" };
	
    protected List<Record> recordList;

    /**
     * Constructor
     * 
     * @param recordList
     */
    public ClientTableModel(List<Record> recordList)
    {
        this.recordList = recordList;
        fireTableDataChanged();
    }
    
    /**
     * @param recordNumber
     * @return record
     */
    public Record getRecord(final int recordNumber){
    	return recordList.get(recordNumber);
    }

    /**
     * @param columnIndex
     * @return columnName
     */
    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    /**
     * @param row
     * @param columnIndex
     * @return false
     */
    @Override
    public boolean isCellEditable(int row, int columnIndex)
    {
        return false;
    }

    /**
     * @param row
     * @param columnIndex
     * @return value
     */
    @Override
    public Object getValueAt(int row, int column)
    {
        if(row < 0 || row >= recordList.size()){
        	return null;
        }        
        
        Record record = recordList.get(row);
                
        return record.getRecordData()[column];
    }

    /**
     * @return rowCount
     */
    @Override
    public int getRowCount()
    {
        return recordList.size();
    }

    /**
     * @return columnCount
     */
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

}
