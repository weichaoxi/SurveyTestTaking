package surveytest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrueFalse extends MultipleChoice {
	
	private static final long serialVersionUID = -6860022303616941869L;

	public TrueFalse(String q) { //true/false question always has the same 2 choices
		super(q);
		choices.put(1, new Choice("True"));
		choices.put(2, new Choice("False"));
		numAnswers = 1;
	}
	
	public TrueFalse(String q, Answer a) {
		super(q);
		choices.put(1, new Choice("True"));
		choices.put(2, new Choice("False"));
		numAnswers = 1;
		answer = a;
	}
	
	public void display() {
		super.display();
	}
	
	public void setUpTabulation() {
		super.setUpTabulation();
	}
	
	public void modify() throws IOException { 
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Do you wish to modify the prompt? Enter y/n");
		String choice = QuestionCreator.promptYesOrNo(); //this will always return y or n
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("The current prompt is:");
			super.display();
			System.out.println("Enter a new prompt:");
			String newPrompt = b.readLine();
			question.setQuestion(newPrompt);
			System.out.println("New prompt set!");
			setUpTabulation(); //reset current tabulation after modifying prompt (as its a different question now)
		}
	}
	
	public void modifyAnswer() throws IOException {
		System.out.println("Do you wish to modify the correct answer?");
		String choice = QuestionCreator.promptYesOrNo();
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("What choice do you wish to make the new correct answer? Enter 1 for true/2 for false");
			int	newAnswer = QuestionCreator.promptPositiveInt(choices.size());
			answer = new Answer();
			answer.addAnswer(String.valueOf(newAnswer));
			System.out.println("Answer successfully changed!");
		}
	}
	
	public void take() throws IOException {
		super.take();
	}
	
	public int grade() {
		return super.grade();
	}
	
	public void tabulate() {
		super.tabulate();
	}
	
	protected int tabulateChoice(int choice) {
		return super.tabulateChoice(choice);
	}
		
}
