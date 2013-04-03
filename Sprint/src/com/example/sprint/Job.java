package com.example.sprint;

public class Job {
	
	private String id;
	private String name;
	private String user;
	private String type;
	
	public Job(String id, String name, String user, String type){
		this.setId(id);
		this.setName(name);
		this.setUser(user);
		this.setType(type);
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
