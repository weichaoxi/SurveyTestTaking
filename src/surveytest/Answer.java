package surveytest;
import java.util.ArrayList;
import java.io.Serializable;

/*
 * In its own class for adapability purposes
 */
public class Answer implements Serializable {
	
	private static final long serialVersionUID = 1838362141094620366L;
	private ArrayList<String> answers = new ArrayList<>(); //some questions can have more than 1 answer
	
	public Answer() {
		answers = new ArrayList<>();
	}
	
	public Answer(String a) {
		answers.add(a);
	}
	
	public String getOneAnswer() {
		return answers.get(0);
	}
	
	public ArrayList<String> getAnswers() {
		return answers;
	}
	
	public String printAnswer() {
		String a = "The answer(s) is: ";
		for(String ans : answers) {
			a += ans + ", ";
		}
		return a;
	}
	
	public void addAnswer(String a) {
		answers.add(a);
	}
	
	public int getAnswerLength() { //return number of answers user needs to enter
		return answers.size();
	}
}
