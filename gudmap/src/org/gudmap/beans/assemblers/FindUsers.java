package org.gudmap.beans.assemblers;

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

import org.gudmap.beans.entities.Users;

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
