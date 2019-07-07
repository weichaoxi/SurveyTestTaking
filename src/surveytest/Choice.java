package surveytest;

import java.io.Serializable;

public class Choice implements Serializable {
	
	private String option; //a possible answer in an MC question

	public Choice(String c) {
		option = c;
	}
	
	public void setChoice(String c) {
		option = c;
	}
	
	public String getChoice() {
		return option;
	}
}