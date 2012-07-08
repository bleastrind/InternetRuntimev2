package models;
import java.io.*;
import java.util.List;
import java.util.*;


public class Favor {
	//Fields
	
	public String id;
	public List<String> Address = new ArrayList<String>();

	//Constructors
	public Favor(){
	}
	
	public Favor(String id){
	this.id = id;
	}
	
	public Favor(String id,List<String> Address){
	this.id = id;
	this.Address = Address;
	}
	
	//Property Accessors
	
	public String getId(){
	return id;
	}
	
	public void setId(String id){
	this.id = id;
	}
	
	public List<String> getAddress(){
	return this.Address;
	}
	
	public void setAddress(List<String> Address){
	this.Address = Address;
	}
	
	public void addAddress(String Address){
	this.Address.add(Address);
	}
	
	
}