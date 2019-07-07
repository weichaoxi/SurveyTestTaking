package surveytest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MultipleChoice extends Question {
	
	private static final long serialVersionUID = 279648966275045245L;
	protected Map<Integer, Choice> choices; //1) answer A 2) answer B etc...
	protected int numAnswers; //number of answers user should give for this question
	
	public MultipleChoice(String q) {
		super(q);
		choices = new HashMap<>();
		numAnswers = 0;
	}
	
	public void display() { //this prints the question and all choices
		super.display();
		for(int key : choices.keySet()) {
			System.out.println(key + ") " + choices.get(key).getChoice());
		}
	}
	
	public void setNumAnswers(int a) {
		numAnswers = a;
	}
	
	public void incrementNumAnswers() {
		numAnswers++;
	}
	
	public void addChoice(int i, String choice) {
		Choice c = new Choice(choice);
		choices.put(i, c);
	}
	
	public void setUpTabulation() { //sets all tabulation counts to 0
		allAnswers = new HashMap<>();
		for(int i = 1; i <= choices.size(); i++) {
			UserAnswer n = new UserAnswer();
			n.addAnswer(String.valueOf(i));
			allAnswers.put(n, 0);
		}
	}
	
	public int getNumChoices() {
		return choices.size();
	}
	
	public void addAnswer(int i) {
		answer.addAnswer(String.valueOf(i));
	}
	
	public void modify() throws IOException {
		super.modify();
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Do you wish to modify choices? Enter y/n"); 
		String choice = QuestionCreator.promptYesOrNo();
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("Which choice do you want to modify? Enter the number of the choice.");
			for(int key : choices.keySet()) {
				System.out.println(key + ") " + choices.get(key).getChoice());
			}
			int choiceToModify = QuestionCreator.promptPositiveInt(choices.size()); //user picks a number up to the last choice
			System.out.println("Enter new value of choice " + choiceToModify);
			String newValue = b.readLine();
			choices.put(choiceToModify, new Choice(newValue));
			System.out.println("Choice " + choiceToModify + " updated!");
			setUpTabulation(); //reset tabulation after modifying
		}
	}
	
	public void modifyAnswer() throws IOException {
		System.out.println("Do you wish to modify the correct answer?");
		String choice = QuestionCreator.promptYesOrNo();
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("What choice do you wish to make the new correct answer? Enter the number of the choice.");
			int	newAnswer = QuestionCreator.promptPositiveInt(choices.size());
			answer = new Answer();
			answer.addAnswer(String.valueOf(newAnswer));
			System.out.println("Answer successfully changed!");
		}
	}
	
	public void take() throws IOException {
		display();
		useranswer = new UserAnswer();
		System.out.println("Please give " + numAnswers + " answer" + (numAnswers == 1 ? "" : "s"));
		for(int i = 1; i <= numAnswers; i++) {
			String ans = String.valueOf(QuestionCreator.promptPositiveInt(choices.size()));
			useranswer.addAnswer(ans);
			for(UserAnswer a : allAnswers.keySet()) {
				if(a.getUserAnswer().get(0).equals(ans)) {
					allAnswers.put(a, allAnswers.get(a) + 1);
				}
			}
		}
	}
	
	public int grade() {
		return (useranswer.getUserAnswer().containsAll(answer.getAnswers()) && answer.getAnswers().containsAll(useranswer.getUserAnswer()) ? 1 : -1);
	}
	
	public void tabulate() {
		System.out.println(question.getContent());
		for(int key : choices.keySet()) {
			System.out.println(choices.get(key).getChoice() + ": " + tabulateChoice(key));
		}
	}
	
	protected int tabulateChoice(int choice) { //helper function that counts the number of times a choice was chosen in all sessions
		int count = 0;
		for(UserAnswer ua : allAnswers.keySet()) {
			if(Integer.parseInt(ua.getUserAnswer().get(0)) == choice) {
				count = allAnswers.get(ua);
			}
		}
		return count;
	}
}
