package org.gudmap.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class TestBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Car>colors=null;
	private Car car1,car2,car3;
	private int[][] multi;
	private String[] testheaders = {"one","two","three","four","five","six","seven","eight","nine","ten"};
	
	public TestBean() {
		
		multi = new int[][]{
				  { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
				  { 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 },
				  { 31, 32, 33, 34, 35, 36, 37, 38, 39, 40 },
				  { 41, 42, 43, 44, 45, 46, 47, 48, 49, 50 },
				  { 51, 52, 53, 54, 55, 56, 57, 58, 59, 60 }
				};
		
		colors = new ArrayList<Car>();
		car1 = new Car();
		car1.setBody("body one");
		car1.setDoor("door one");
		car1.setWheels("wheels one");
		colors.add(car1);
		
		car2 = new Car();
		car2.setBody("body two");
		car2.setDoor("door two");
		car2.setWheels("wheels two");
		colors.add(car2);
		
		car3 = new Car();
		car3.setBody("body three");
		car3.setDoor("door three");
		car3.setWheels("wheels three");
		colors.add(car3);
		
	}
	
	public int[][] getMulti() {
		return multi;
	}
	
	public String[] getTestheaders() {
		return testheaders;
	}
	
	public ArrayList<Car> getColors() {
		return colors;
	}
	
	public class Car {
		private String door;
		private String body;
		private String wheels;
		
		public void setDoor(String door) {
			this.door=door;
		}
		
		public String getDoor() {
			return door;
		}
		
		public void setBody(String body) {
			this.body=body;
		}
		
		public String getBody() {
			return body;
		}
		
		public void setWheels(String wheels) {
			this.wheels=wheels;
		}
		
		public String getWheels() {
			return wheels;
		}
	}
}
