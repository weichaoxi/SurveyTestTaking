package surveytest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Matching extends Question {

	protected ArrayList<String> keys; //the left side of a matching problem
	private ArrayList<String> values; //the right side of a matching problem
	protected HashMap<String, String> answer;
	protected HashMap<String, String> userAnswer;
	protected ArrayList<HashMap<String, String>> allAnswers = new ArrayList<>();
	
	private static final long serialVersionUID = -7830371001783389076L;
	
	public Matching(String problemDescription) { //@param "Match each city with state" etc.. 
		super(problemDescription);
		keys = new ArrayList<>();
		values = new ArrayList<>();
		answer = new HashMap<>();
		userAnswer = new HashMap<>();
	}
	
	@Override
	public void display() {
		System.out.println(question.getContent());
		char alphabet = 'A';
		for(int i = 0; i < keys.size(); i++) { //keys and values have the same size
			System.out.printf("%-30.30s  %-30.30s%n", alphabet + ") " + keys.get(i), i+1 + ") " + values.get(i));
			alphabet++;
		}
	}
	
	@Override
	public void displayAnswer() {
		System.out.println("The correct pairings are:");
		for(String key : answer.keySet()) {
			System.out.printf("%-30.30s  %-30.30s%n", key, answer.get(key));
		}
	}

	public void addKey(String s) {
		keys.add(s);
	}
	
	public void addValue(String s) {
		values.add(s);
	}
	
	
	public void addPair(String key, String value) { //add key value pair to answer map
		answer.put(key, value);
	}
	
	/*
	 * randomizes the order in the array lists, so that when question is displayed to a test taker
	 *the key-value pairs are presented in random order. Used after creating a new matching question
	 */
	public void shuffle() { 
		long seed = System.nanoTime();
		long seed2 = System.currentTimeMillis();
		Collections.shuffle(keys, new Random(seed));
		Collections.shuffle(values, new Random(seed2));
	}
	
	public void modify() throws IOException {
		super.modify();
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Do you want to modify a key-value pair?");
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
			System.out.println("Enter a value corresponding to that key (the item on the right side)");
			String newValue = b.readLine();
			String old = null;
			for(String r : keys) { //remove the old key from arraylist keys
				if(r.equals(k)) {
					old = r;
					keys.remove(r);
					break;
				}
			}
			String oldValue = answer.remove(old); //remove key-value pair from answer map
			values.remove(oldValue); //remove the old value from the arraylist values
			addPair(newKey, newValue);
			keys.add(newKey);
			values.add(newValue);
			shuffle();
			System.out.println("New key-value pair added!");
			allAnswers = new ArrayList<>(); //reset tabulation after modifying
		}
	}
	
	public void modifyAnswer() {
		return; //the new key/value pair added in the above function is automatically a new key/value answer
	}
	
	public void take() throws IOException {
		display();
		userAnswer = new HashMap<>();
		//BufferedReader a = new BufferedReader(new InputStreamReader(System.in));
		for(String key : keys) {
			System.out.println("Enter the correct match for " + key + " (enter number of the match)");
			int value = QuestionCreator.promptPositiveInt(values.size()); //user can't enter a number larger than the number of choices
			userAnswer.put(key, values.get(value-1));
		}
		allAnswers.add(userAnswer); //for tabulation
	}
	
	//if a key-value pair in useranswer is different from real answer, return -1 for wrong
	public int grade() {
		int g = 1;
		for(String key : answer.keySet()) {
			if(!answer.get(key).equals(userAnswer.get(key))) g = -1; 
		}
		return g;
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
