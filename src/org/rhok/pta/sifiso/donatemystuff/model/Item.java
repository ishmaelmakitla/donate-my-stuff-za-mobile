package org.rhok.pta.sifiso.donatemystuff.model;

public class Item {
	private String id;
	private String name;
	private int size = 0;
	private int agerestriction = 0;
	private int age = 0;
	private String type;
	private String isbn;

	public Item() {
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn
	 *            the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getAgerestriction() {
		return agerestriction;
	}

	public void setAgerestriction(int agerestriction) {
		this.agerestriction = agerestriction;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
