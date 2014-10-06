package org.gudmap.beans.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.models.Usertest;

//@ManagedBean (name="userbean")
//@SessionScoped
@Named (value="userbean")
@RequestScoped
public class ValidateUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String username="derekeh";
	//resource injection
	@Resource(name="jdbc/Gudmap_jdbcResource")
	private DataSource ds;
	
	public ValidateUser() {
		
	}
	
	//if resource injection is not support, you still can get it manually.
		/*public ValidateUser(){
			try {
				Context ctx = new InitialContext();
				ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
			} catch (NamingException e) {
				e.printStackTrace();
			}
	 
		}*/
	
	//connect to DB and get User list
		public List<Usertest> getUserList() throws SQLException{
	 
			if(ds==null)
				throw new SQLException("Can't get data source");
	 
			//get database connection
			Connection con = ds.getConnection();
	 
			if(con==null)
				throw new SQLException("Can't get database connection");
			
			PreparedStatement ps 
				= con.prepareStatement(
				   "SELECT jsf_forename, jsf_surname, jsf_email FROM jsftest"); 
	 
			//get customer data from database
			ResultSet result =  ps.executeQuery();
			
			List<Usertest> list = new ArrayList<Usertest>();
			
			while(result.next()){
			Usertest user = new Usertest();
			user.setForename(result.getString("jsf_forename"));
			user.setSurname(result.getString("jsf_surname"));
			user.setEmail(result.getString("jsf_email"));
			
			//store all data into a List
			list.add(user);
			}
			return list;
		}
		
		public String getUsername(){
			return username;
		}
		
		public void setUsername(String username){
			this.username=username;
		}
	
}
