package surveytest;

import java.io.Serializable;

public class QuestionContent implements Serializable {
	/*
	 * represents the question itself, as well as any
	 * answer choices
	 */
	
	private String content; //the actual text/content of the question
	
	
	public QuestionContent(String q) {
		content = q;
	}
	
	public void setQuestion(String q) {
		content = q;
	}
	
	//public void addChoice(Choice c) {
	//	choices.add(c);
	//}
	
	public String getContent() {
		return content;
	}
}
