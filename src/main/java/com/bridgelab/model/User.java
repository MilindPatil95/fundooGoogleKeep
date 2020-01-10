package com.bridgelab.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String address;
	private String mobile_no;
	private String password;
	private String email;
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", address=" + address + ", mobile_no=" + mobile_no + ", password="
				+ password + ", email=" + email + "]";
	}
	public User(int id, String name, String address, String mobile_no, String password, String email) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.mobile_no = mobile_no;
		this.password = password;
		this.email = email;
	}
	public User() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
