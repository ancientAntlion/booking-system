package suncertify.db;

public class Data implements DB {
	
	private final DataAccessObject dao;
	
	public Data(final String dbLocation){
		dao = new DataAccessObject(dbLocation);
	}
	
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		return dao.read(recNo);
	}

	@Override
	public void update(int recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		dao.update(recNo, data, lockCookie);
	}

	@Override
	public void delete(int recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		dao.delete(recNo, lockCookie);
	}

	@Override
	public int[] find(String[] criteria) {
		return dao.find(criteria);
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
