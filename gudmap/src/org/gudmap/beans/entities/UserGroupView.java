package org.gudmap.beans.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*public class UserGroupView {}
*/
/*@Entity
@Table (name="usergroupview")*/
public class UserGroupView {
	//@Column (name="ug_forename")
	private String forename;
	
	//@Column (name="ug_group")
	private String group;
	
	public String getForename(){
		return forename; 
	}
	public void setForename(String forename){
		this.forename=forename;
	}
	
	public String getGroup(){
		return group; 
	}
	public void setGroup(String group){
		this.group=group;
	}
	
	

}
