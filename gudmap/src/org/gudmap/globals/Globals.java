package org.gudmap.globals;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//@ManagedBean (eager=true)
//@ApplicationScoped
public class Globals {
	public Globals(){
		
	}
	
	public static void closeQuietly(Connection connection, PreparedStatement statement, ResultSet resultSet) {
	    if (resultSet != null) try { resultSet.close(); } catch (SQLException logOrIgnore) {}
	    if (statement != null) try { statement.close(); } catch (SQLException logOrIgnore) {}
	    if (connection != null) try { connection.close(); } catch (SQLException logOrIgnore) {}
	}
	
	

}
