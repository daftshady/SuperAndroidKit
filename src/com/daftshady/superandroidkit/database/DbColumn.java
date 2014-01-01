package com.daftshady.superandroidkit.database;

/**
 * POJO for database column.
 * @author parkilsu
 *
 */
public class DbColumn {
	public DbColumn(String name, DbEnum.Type type) {
		this.name = name;
		this.type = type;
	}
	
	private String name;
	private DbEnum.Type type;
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DbEnum.Type getType() {
		return type;
	}
	
	public void setType(DbEnum.Type type) {
		this.type = type;
	}
}