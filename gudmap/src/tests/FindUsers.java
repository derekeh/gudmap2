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

/*EXAMPLE OF USING JPA and JPA QUERY LANGUAGE FOR RETRIEVING AN ENTITY <Users> . THE 'Users' in the query MUST be the entity class name*/
@ManagedBean
@RequestScoped
public class FindUsers implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;
	
	private List<Users> userList=null;
	
	public List<Users> getUsers() {
		
		try
		{
			Query query = em.createQuery("SELECT u FROM Users u WHERE u.forename LIKE :name");
			query.setParameter("name", "%ere%");
			userList=query.getResultList();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
}
