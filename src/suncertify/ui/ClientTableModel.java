package suncertify.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.shared.model.Record;

/**
 * @author Aaron
 *
 */
public class ClientTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1111923761540802741L;
	
	private static final String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate", "Date", "Owner" };
	
    protected List<Record> recordList;

    /**
     * @param recordList
     */
    public ClientTableModel(List<Record> recordList)
    {
        this.recordList = recordList;
        fireTableDataChanged();
    }
    
    /**
     * @param recordNumber
     * @return
     */
    public Record getRecord(final int recordNumber){
    	return recordList.get(recordNumber);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int columnIndex)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
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

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        return recordList.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

}
