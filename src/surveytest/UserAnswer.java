package surveytest;
import java.io.Serializable;
import java.util.ArrayList;

//for future hw assignment when user takes a test
public class UserAnswer implements Serializable {
	
	private static final long serialVersionUID = 5967789778534183826L;
	private ArrayList<String> userAnswers; //stores response from 1 current test taking session
	//private ArrayList<String> allAnswers = new ArrayList<>(); //stores responses from all times question is answered (for tabulation)
	private String oneAnswer; //for questions that take just 1 answer
	
	public UserAnswer() {
		userAnswers = new ArrayList<>();
	}
	
	public UserAnswer(String a) {
		oneAnswer = a; //for questions that take 1 answer
		//allAnswers.add(a);
	}
	
	public void addAnswer(String ans) { //add the answer for a question that can take multiple answers
		userAnswers.add(ans);
		//allAnswers.add(ans);
	}
	
	public void addOneAnswer(String ans) { //add the answer for a question that takes a single answer
		oneAnswer = ans;
		//allAnswers.add(ans);
		
	}
	
	public String getOneAnswer() {
		return oneAnswer;
	}
	
	public ArrayList<String> getUserAnswer() {
		return userAnswers;
	}
	
	//public ArrayList<String> getAllAnswers() {
		//return allAnswers;
	//}
}
