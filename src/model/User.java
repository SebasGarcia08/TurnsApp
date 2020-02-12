package model;

public class User {

	private String name;
	private String id;
	private String typeOfDocument;
	private String cellphoneNumber;
	private String address;
	private Turn turn;

	/**
	 * 
	 * @param n
	 * @param id
	 * @param tod
	 * @param cpn
	 * @param a
	 */
	public User(String n, String id, String tod, String cpn, String a, Turn t) {
		this.name = n;
		this.id = id;
		this.typeOfDocument = tod;
		this.cellphoneNumber = cpn;
		this.address = a;
		this.turn = t;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", id=" + id + ", typeOfDocument=" + typeOfDocument + ", cellphoneNumber="
				+ cellphoneNumber + ", address=" + address + ", turn=" + turn.toString() + "]";
	}

	public Turn getTurn() {
		return turn;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getTypeOfDocument() {
		return this.typeOfDocument;
	}

	/**
	 * 
	 * @param typeOfDocument
	 */
	public void setTypeOfDocument(String typeOfDocument) {
		this.typeOfDocument = typeOfDocument;
	}

	public String getCellphoneNumber() {
		return this.cellphoneNumber;
	}

	/**
	 * 
	 * @param cellphoneNumber
	 */
	public void setCellphoneNumber(String cellphoneNumber) {
		this.cellphoneNumber = cellphoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

	/**
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

}