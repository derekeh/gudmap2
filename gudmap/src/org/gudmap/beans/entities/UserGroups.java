package org.gudmap.beans.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*public class UserGroups {
}*/

@Entity
@Table (name="ref_users2")
public class UserGroups {
	@Id
	@Column (name="usr_oid")
	private Long oid;
	
	@Column (name="usr_forename")
	private String forename;
	
	@Column (name="usr_surname")
	private String surname;
	
	@Column (name="usr_email")
	private String email;
	
	/*private int usr_group_fk;*/
	
	@ManyToOne
	@JoinColumn (name="usr_group_fk")
	private Groups groups;
	
	public Groups getGroups(){
		return groups;
	}
	
	public void setGroups (Groups groups){
		this.groups=groups;
	}
	
	public Long getOid(){
		return oid; 
	}
	public void setOid(Long oid){
		this.oid=oid;
	}
	
	public String getForename(){
		return forename; 
	}
	public void setForename(String forename){
		this.forename=forename;
	}
	
	public String getSurname(){
		return surname; 
	}
	public void setSurname(String surname){
		this.surname=surname;
	}
	
	public String getEmail(){
		return email; 
	}
	public void setEmail(String email){
		this.email=email;
	}
	
	/*public int getUsr_group_fk(){
		return usr_group_fk; 
	}
	public void setUsr_group_fk(int usr_group_fk){
		this.usr_group_fk=usr_group_fk;
	}*/

}
