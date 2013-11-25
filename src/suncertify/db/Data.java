package suncertify.db;

public class Data implements DB {
	
	public static void main(String[] args) {
		Data data = new Data();
		try {
			data.read(2);
		} catch (RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		
		DataAccessObject dao = new DataAccessObject();
		
		return dao.read(recNo);
		
	}

	@Override
	public void update(int recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] find(String[] criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void unlock(int recNo, long cookie) throws RecordNotFoundException,
			SecurityException {
		// TODO Auto-generated method stub
		
	}

}
