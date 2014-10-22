package tests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table (name="ref_groups")
public class Groups {
	@Id
	@Column (name="grp_oid")
	private Long oid;
	
	@Column (name="grp_name")
	private String groupname;
	
	@Column (name="grp_location")
	private String location;
	
	public Long getOid(){
		return oid; 
	}
	public void setOid(Long oid){
		this.oid=oid;
	}
	
	public String getGroupname(){
		return groupname; 
	}
	public void setGroupname(String groupname){
		this.groupname=groupname;
	}
	
	public String getLocation(){
		return location; 
	}
	public void setLocation(String location){
		this.location=location;
	}
}
