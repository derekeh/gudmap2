package tests;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@RequestScoped
public class FindUserGroups implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;
	
	private List<UserGroups> userList=new ArrayList<UserGroups>();
	private UserGroups usergroups;
	
	public List<UserGroups> getUserGroups() {
		
		try
		{
			usergroups=em.find(UserGroups.class, 2L);
			userList.add(usergroups);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
}
