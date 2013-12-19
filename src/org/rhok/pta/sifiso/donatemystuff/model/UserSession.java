package org.rhok.pta.sifiso.donatemystuff.model;

import java.io.Serializable;

import com.google.gson.Gson;
/**
 * This class is used to carry user session data such as logged in Username and UserID which is used to 
 * be passed on to other activities. The UserID is used as Beneficiary/Donor ID to associate a request/offer with a specific user
 * @author Ishmael Makitla
 * 		   GDG/RHoK Pretoria
 * 			South Africa
 * 			2013
 *
 */
public class UserSession implements Serializable{

	private static final long serialVersionUID = 9175065014422245294L;
	
	private String username;
	private String userID;
			
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	

	public String toString(){
		return (new Gson()).toJson(this, UserSession.class);
	}
}
