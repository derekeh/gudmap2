package tests;

public class Usertest {
	
	private int oid;
	private String forename;
	private String surname;
	private String email;
	
	public void setOid(int oid){
		this.oid=oid;
	}
	
	public int getOid() {
		return oid;
	}
	
	public void setForename(String forename){
		this.forename=forename;
	}
	
	public String getForename(){
		return forename;
	}
	
	public void setSurname(String surname){
		this.surname=surname;
	}
	
	public String getSurname(){
		return surname;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public String getEmail(){
		return email;
	}
}
