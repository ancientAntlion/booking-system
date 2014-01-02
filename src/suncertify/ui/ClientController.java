package suncertify.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DBConnection;

public class ClientController {
	
	private final DBConnection dbConnection;
	
	String[] columnNames = { "First Name", "Last Name", "Sport", "# of Years",
			"Vegetarian", "aaaa", "bbb" };
	
	public ClientController(){
		dbConnection = new DBConnection();
	}
	
	public TableModel getAllEntries(){
		final List<String[]> entryList = new ArrayList<String[]>();
		try{
			for(int i = 0;;i++){
				entryList.add(dbConnection.read(i));
			}
		}catch(RecordNotFoundException rnfe){
			//End of file reached, all records read, carry on
		}
		
		final String[][] allEntries;
		
		if(entryList.isEmpty()){
			allEntries = new String[0][0];
		}else{
		
			allEntries = new String[entryList.size()][entryList.get(0).length];
			
			for(int i = 0; i<entryList.size(); i++){
				final String[] singleEntry = entryList.get(i);
				allEntries[i] = singleEntry;
			}
		}
		
		return new ClientTableModel(allEntries, columnNames);
	}
	
	public TableModel getSpecificEntries(final String name, final String location){
		String[] criteria = constructCriteria(name, location);
		final List<String[]> entryList = new ArrayList<String[]>();
		
		int[] matchedEntries = dbConnection.find(criteria);
		
		try{
			for(int i : matchedEntries){
				entryList.add(dbConnection.read(i));
			}
		}catch(RecordNotFoundException rnfe){
			//End of file reached, all records read, carry on
		}
		
		final String[][] allEntries;
		
		if(entryList.isEmpty()){
			allEntries = null;
		}else{
		
			allEntries = new String[entryList.size()][entryList.get(0).length];
			
			for(int i = 0; i<entryList.size(); i++){
				final String[] singleEntry = entryList.get(i);
				allEntries[i] = singleEntry;
			}
		}
		
		return new ClientTableModel(allEntries, columnNames);
	}
	
	private String[] constructCriteria(final String name, final String location){
		final String[] criteria = new String[7];
		criteria[0] = name;
		criteria[1] = location;
		
		return criteria;
	}

}
