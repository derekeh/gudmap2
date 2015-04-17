package org.gudmap.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;

import org.gudmap.globals.Globals;
import org.gudmap.queries.generic.GenericQueries;

import java.io.Serializable;

@Named(value="loginBean")
@SessionScoped
public class LoginBean implements Serializable{
	
	 @Resource(name="jdbc/Gudmap_jdbcResource")
	  private DataSource ds;
	  private Connection con;
	  private PreparedStatement ps;
	  private ResultSet result;
	
	private static final long serialVersionUID = 1L;

	private String username,password,role=null;
	
	public LoginBean() {
		/*try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}*/
		
	}
	
	public void setUsername(String username){
		this.username=username;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public String getPassword() {
		return password;
	}
	public void setRole(String role){
		this.role=role;
	}
	public String getRole() {
		return role;
	}
	
	public String login() {
		String RET="loginFailure";
		String queryString=GenericQueries.LOGINS;
		//verify from DB
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, username);
			ps.setString(2, password);
			result =  ps.executeQuery();
			
			if(result.next())
			{
				username=result.getString(1);
				role=result.getString(3);
				RET="database_homepage";
			}
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		//set RET to forward to correct page or create a FacesMessage to notify user and force null return
		return RET;
	}
	
	public String logout () {
		String RET="database_homepage";//name of redirect page
		username=null;
		password=null;
		role=null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		
		return RET;
	}
}
