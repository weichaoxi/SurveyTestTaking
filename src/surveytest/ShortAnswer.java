package surveytest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShortAnswer extends Essay {

	private static final long serialVersionUID = 7197548213037755738L;
	private static final int MAX_ANSWER_LENGTH = 250;
	
	public ShortAnswer(String prompt) {
		super(prompt);
	}
	
	public void display() {
		super.display();
	}
	
	public void addAnswer(String ans) {
		answer.addAnswer(ans);
	}
	
	public void displayAnswer() {
		System.out.println(answer.printAnswer());
	}
	
	public void modify() throws IOException {
		super.modify();
	}
	
	public void modifyAnswer() throws IOException {
		System.out.println("Do you wish to modify the correct answer?");
		String choice = QuestionCreator.promptYesOrNo();
		if(choice.equalsIgnoreCase("y")) {
			BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the new correct answer.");
			String newAnswer = b.readLine();
			answer = new Answer(newAnswer);
			System.out.println("Answer successfully changed!");
		}
	}
	
	public void take() throws IOException { //do the question, also store answer for tabulation
		display();
		System.out.println("Enter your response:");
		BufferedReader a = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		String response = null;
		while(!done) {
			response = a.readLine();
			if(response.length() <= MAX_ANSWER_LENGTH) done = true;
			else {
				System.out.println("Your answer needs to be at most " + MAX_ANSWER_LENGTH + " characters.");
			}
		}
		useranswer = new UserAnswer(response);
		boolean answerExists = false;
		for(UserAnswer ua : allAnswers.keySet()) {
			if(ua.getOneAnswer().equals(useranswer.getOneAnswer())) {
				answerExists = true;
				allAnswers.put(ua, allAnswers.get(ua) + 1); //increment count if an answer already appears
			}
		}
		
		if(!answerExists) {
			allAnswers.put(useranswer, 1);
		}
	}
	
	public int grade() {
		return answer.getOneAnswer().equals(useranswer.getOneAnswer()) ? 1 : -1;
	}
	
	public void tabulate() {
		System.out.println(question.getContent());
		System.out.println();
		for(UserAnswer u : allAnswers.keySet()) {
			System.out.println(u.getOneAnswer() + ": " + allAnswers.get(u));
		}
	}
}
