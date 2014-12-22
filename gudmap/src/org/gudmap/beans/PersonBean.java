package org.gudmap.beans;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;



import org.gudmap.models.submission.PersonModel;
import org.gudmap.assemblers.PersonAssembler;


@Named
@RequestScoped
public class PersonBean  {
	
    private PersonModel[] people;
    private PersonAssembler personAssembler;
    private String id;
    private String personId;
    
    public PersonBean () {
    	
    	personAssembler = new PersonAssembler();
    	
		FacesContext facesContext = FacesContext.getCurrentInstance();
        this.id = facesContext.getExternalContext().getRequestParameterMap().get("id");
		
		
		if (null != id)
		    people = personAssembler.getPeopleBySubmissionId(id);
		
		if (null == people || 0 == people.length) {
		    this.id = facesContext.getExternalContext().getRequestParameterMap().get("personId");
		    PersonModel person = null;
		    if (null != id)
		    	person = personAssembler.getPersonById(id);
		    
		    if (null == person)
		    	people = null;
		    else {
				people = new PersonModel[1];
				people[0] = person;
		    }
		}
    }
    
    public PersonModel[] getPeople() {
    	
    	/*personAssembler = new PersonAssembler();
    	
    	if (null != id)
		    people = personAssembler.getPeopleBySubmissionId(id);
		
		if (null == people || 0 == people.length) {
		    id = personId;
		    PersonModel person = null;
		    if (null != id)
		    	person = personAssembler.getPersonById(id);
		    
		    if (null == person)
		    	people = null;
		    else {
				people = new PersonModel[1];
				people[0] = person;
		    }
		}*/
    	return people;
    }
    
    public void setId(String id){
    	this.id=id;
    }
    
    public String getId() {
    	return id;
    }
    
    public void setPersonId(String personId){
    	this.personId=personId;
    }
    
    public String getPersonId() {
    	return personId;
    }

}
