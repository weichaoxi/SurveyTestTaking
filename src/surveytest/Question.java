package surveytest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;

/*
 * 
 */
public abstract class Question implements Serializable {
	
	private static final long serialVersionUID = 6402241644115292701L;
	protected QuestionContent question; //question text/content
	protected UserAnswer useranswer;
	protected Answer answer; //correct answer; only relevant in tests
	protected HashMap<UserAnswer, Integer> allAnswers; //answers from all survey/test sessions for tabulation
	
	public Question() {
		
	}
	
	public Question(String content) {
		question = new QuestionContent(content);
		answer = new Answer();
		useranswer = new UserAnswer();
		allAnswers = new HashMap<>();
	}
	
	public void setQuestion() {
		
	}
	
	public void display() { //displays question + options, does not display answer
		System.out.println(question.getContent());
	}
	
	public void displayAnswer() {
		System.out.println(answer.printAnswer());
	}
	
	public void setAnswer(Answer a) {
		answer = a;
	}
	
	public abstract void take() throws IOException; //the user does this question, enters his response
	
	//After modifying, it resets the tabulation (as its not the same question anymore)
	public void modify() throws IOException {
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Do you wish to modify the prompt? Enter y/n");
		String choice = QuestionCreator.promptYesOrNo(); //this will always return y or n
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("The current prompt is:");
			display();
			System.out.println("Enter a new prompt:");
			String newPrompt = b.readLine();
			question.setQuestion(newPrompt);
			System.out.println("New prompt set!");
			allAnswers = new HashMap<>(); //on changing the question reset the map of all answers
		}
	}
	
	public abstract void modifyAnswer() throws IOException; //modify answer to a question for tests
	
	public abstract int grade(); //grade this question, returns 1 if correct, -1 if incorrect, 0 if not graded
	
	public abstract void tabulate();
}
