package tests;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean (name="ugview")
@RequestScoped
public class FindUserGroupView implements Serializable {
	private static final long serialVersionUID = 1L;

/*	@PersistenceContext
	private EntityManager em;*/
	
	//resource injection
		@Resource(name="jdbc/Gudmap_jdbcResource")
		private DataSource ds;
		
	public FindUserGroupView(){}
	
	//connect to DB and get User list
			public List<UserGroupView> getView() throws SQLException{
		 
				if(ds==null)
					throw new SQLException("Can't get data source");
		 
				//get database connection
				Connection con = ds.getConnection();
		 
				if(con==null)
					throw new SQLException("Can't get database connection");
				
				PreparedStatement ps 
					= con.prepareStatement(
					   "SELECT ug_forename,ug_group FROM usergroupview"); 
		 
				//get customer data from database
				ResultSet result =  ps.executeQuery();
				
				List<UserGroupView> viewList = new ArrayList<UserGroupView>();
				
				while(result.next()){
				UserGroupView ugv = new UserGroupView();
				ugv.setForename(result.getString("ug_forename"));
				ugv.setGroup(result.getString("ug_group"));
				
				//store all data into a List
				viewList.add(ugv);
				}
				con.close();
				ps.close();
				result.close();
				return viewList;
			}
	
	/*public List<UserGroupView> getView() {
		
		try
		{
			Query query = em.createQuery("SELECT u FROM UserGroupView u WHERE u.forename LIKE :name");
			query.setParameter("name", "%ere%");
			viewList=query.getResultList();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return viewList;
	}*/
}
