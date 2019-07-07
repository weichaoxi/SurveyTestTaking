package surveytest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/*
 * ranking problems are essentially the same as matching problems except
 * that the values in (key, value) pairs are typically integers from 1 up
 * with a particular context (most of something, least of something etc)
 */
public class Ranking extends Matching {
	
	private static final long serialVersionUID = 6397583157723651522L;

	public Ranking(String problemDescription) {
		super(problemDescription);
	}
	
	public void shuffle() {
		long seed = System.nanoTime();
		Collections.shuffle(this.keys, new Random(seed));
	}
	
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
		}
	
		System.out.println("Do you want to modify a key?");
		String c = QuestionCreator.promptYesOrNo();
		if(c.equalsIgnoreCase("y")) {
			System.out.println("Enter the key of of the pair you want to change (left side, make sure case is exact)");
			String k = b.readLine();
			boolean done = false;
			while(!done) {
				if(keys.contains(k)) done = true;
				else {
					System.out.println("That key doesn't exist. Please enter again.");
					k = b.readLine();
				}
			}
			System.out.println("Enter a new key (the item on the left side)");
			String newKey = b.readLine();
			keys.remove(k);
			keys.add(newKey);
			shuffle();
			System.out.println("New key added!"); //since rankings (the right side) is always the same, just update key
			allAnswers = new ArrayList<>(); //reset tabulation after modifying
		}
	}
	
	public void modifyAnswer() { //after changing a key, the entire ranking may be different now, so have user change all 
		answer = new HashMap<>();
		for(String key : keys) {
			System.out.println("Enter a new ranking for " + key + " (a number from 1 to " + keys.size() + ")"); //check if numbers are already used?
			int rank = QuestionCreator.promptPositiveInt(keys.size());
			answer.put(key, String.valueOf(rank));
		}
		System.out.println("Answer successfully modified!");
	}
	
	public void take() throws IOException {
		super.take();
	}
	
	
	public int grade() {
		return super.grade();
	}
	
	public void tabulate() {
		System.out.println(question.getContent());
		ArrayList<HashMap<String, String>> uniqueAnswers = new ArrayList<>();
		for(HashMap<String, String> s : allAnswers) {
			if(!uniqueAnswers.contains(s)) uniqueAnswers.add(s);
		}
		for(HashMap<String, String> v : uniqueAnswers) {
			System.out.println(tabulateSpecific(v)); //print number of times an answer appears
			for(String key : v.keySet()) {
				System.out.println(key + " " + v.get(key));
			}
			System.out.println();
		}
	}
	
	protected int tabulateSpecific(HashMap<String, String> a) { //helper function to tabulate count of specific answer set
		int count = 0;
		for(HashMap<String, String> s : allAnswers) {
			if(s.equals(a)) count ++;
		}
		return count;
	}
}
