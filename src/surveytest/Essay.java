package surveytest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Essay extends Question { //no "correct" answer

	private int numAnswers;

	public Essay(String prompt) {
		super(prompt);
	}
	
	public Essay(String prompt, int numResponses) {
		super(prompt);
		numAnswers = numResponses;
	}
	
	public void display() {
		super.display();
	}
	
	public void displayAnswer() {
		return; //nothing to display
	}
	
	public void modify() throws IOException {
		super.modify();
	}
	
	public void modifyAnswer() throws IOException {
		return; //essays don't have "answers"
	}
	
	public void take() throws IOException {
		display();
		System.out.println("Give " + numAnswers + " responses.");
		String response = "";
		BufferedReader answer = new BufferedReader(new InputStreamReader(System.in));
		useranswer = new UserAnswer(); //resets current useranswer 
		for(int i = 1; i <= numAnswers; i++) {
			System.out.println("Answer " + i + ":");
			response = answer.readLine();
			useranswer.addAnswer(response);
		}
		allAnswers.put(useranswer, 0); //value doesnt matter here
	}
	
	public int grade() {
		return 0; //essays aren't graded
	}
	
	public void tabulate() {
		System.out.println(question.getContent());
		System.out.println();
		for(UserAnswer u : allAnswers.keySet()) {
			for(String ans : u.getUserAnswer()) {
				System.out.println(ans);
			}
			System.out.println("--------------------"); //make it clear about responses from different survey/test sessions
		}
	}
}
