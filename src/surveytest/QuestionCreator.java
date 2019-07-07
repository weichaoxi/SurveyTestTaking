package surveytest;
import java.io.*;

public class QuestionCreator { //this is the class that implements the creation of various questions
	private static BufferedReader b;
	
	//bring up the adding question prompt, @param type Test, Survey, etc
	public static Question createQuestion(String type) throws IOException {
		System.out.println("1) Add a new T/F question");
		System.out.println("2) Add a new multiple choice question");
		System.out.println("3) Add a new short answer question");
		System.out.println("4) Add a new essay question");
		System.out.println("5) Add a new ranking question");
		System.out.println("6) Add a new matching question");
		int input;
		
		b = new BufferedReader(new InputStreamReader(System.in));
		input = promptPositiveInt();
		if((input) < 1 || (input) > 6) {
			System.out.println("Please choose one of the above choices.");
			return createQuestion(type); //incorrect input
		}
		return questionCreate(input, type);
	}
	
	//@param type: survey or test
	public static Question questionCreate(int input, String type) throws IOException {
		String prompt = "";
		String answer = null;
		switch(input) {
		case 1: System.out.println("Enter the prompt for your true/false question:");
				try {
					prompt = b.readLine();
					if(type.equals("Test")) {
						System.out.println("Enter correct choice (1 True, 2 False)"); //user puts answer if making a test question
						boolean done = false;
						while(!done) {
							answer = b.readLine();
							if(answer.equals("1") || answer.equals("2")) done = true;
							else System.out.println("For the correct choice, please enter 1 for true or 2 for false");
						}
					}
				}
				catch (IOException e) {
					System.out.println("IOException");
					return questionCreate(1, type);
				}
				TrueFalse tf = new TrueFalse(prompt, new Answer(answer)); //let constructor do the work
				tf.setUpTabulation();
				 
				return tf;
		
		case 2: System.out.println("Enter the prompt for your multiple choice question:");
				prompt = b.readLine();
				
				
				System.out.println("Enter the number of choices for your multiple choice question:");
				String numChoices = "";
				try {
					
					numChoices = b.readLine(); //read number of choices
					
					if(Integer.parseInt(numChoices) < 1) {
						System.out.println("Please enter 1+ choices (though logically 2 or more!).");
						return questionCreate(2, type);
					}
				}
				catch(Exception e) {
					System.out.println("Please enter 1+ choices (though logically 2 or more!).");
					return questionCreate(2, type); //let user retry making MC question if he enters wrong
				}
				
				
				MultipleChoice mc = new MultipleChoice(prompt); //add prompt to mc's questionContent
				for(int i = 1; i <= Integer.parseInt(numChoices); i++) {
					System.out.println("Enter choice " + i + ".");
					String choice = b.readLine();
					if(type.equals("Test")) {
						boolean done = false;
						System.out.println("Is this choice an answer to this question? Enter y/n");
						String yn = null;
						while(!done) {
							yn = b.readLine();
							if(!yn.equalsIgnoreCase("y") && !yn.equalsIgnoreCase("n")) {
								System.out.println("Please enter y or n");
							}
							else done = true;
						}
						if(yn.equalsIgnoreCase("y")) {
							mc.addAnswer(i);
							mc.incrementNumAnswers();
						}
						
					}
					mc.addChoice(i, choice);
				}
				if(type.equals("Survey")) {
					System.out.println("How many answers to this question do you want users to give?");
					int numAns = promptPositiveInt(mc.getNumChoices());
					mc.setNumAnswers(numAns);
				}
				mc.setUpTabulation();
				return mc;
				
		case 3: System.out.println("Enter the prompt for your short answer question:");
				prompt = b.readLine();
				ShortAnswer sa = new ShortAnswer(prompt);
				if(type.equals("Test")) {
					System.out.println("Enter the answer to your short answer question:");
					String ans = b.readLine();
					sa.addAnswer(ans); 
				}
				return sa;
		
		case 4: System.out.println("Enter the prompt for your essay question:");
				prompt = b.readLine();
				System.out.println("How many responses do you want your essay to have?");
				int numResponses = promptPositiveInt();
				Essay e = new Essay(prompt, numResponses);
				return e; //essays don't have answers, so don't prompt for one
		
		case 5:	System.out.println("Enter the instructions for your ranking question.");
				prompt = b.readLine();
				System.out.println("How many items will you be ranking for your ranking question?");
				int numItems = promptPositiveInt();
				Ranking r = new Ranking(prompt);
				for(int i = 1; i <= numItems; i++) {
					System.out.println("Enter item #" + i);
					String key = b.readLine();
					if(type.equals("Test")) {
						System.out.println("Enter the ranking that corresponds to item #" + i + ("(from 1 to " + numItems + ")"));
						int rnk = promptPositiveInt(numItems);
						String rank = String.valueOf(rnk);
						r.addPair(key, rank);
					}
					r.addKey(key);
					r.addValue(String.valueOf(i));
				}
				r.shuffle();
				return r;
		
		case 6: System.out.println("Enter the instructions for your matching question.");
				prompt = b.readLine();
				System.out.println("How many key-value pairs will you have for your matching question?");
				int numPairs = promptPositiveInt();
				Matching m = new Matching(prompt);
				for(int i = 1; i <= numPairs; i++) { //prompt user for key value pairs
					System.out.println("Enter key #" + i);
					String key = b.readLine();
					m.addKey(key);
					System.out.println("Enter value #" + i + "(that corresponds to that key)");
					String value = b.readLine();
					m.addValue(value);
					if(type.equals("Test")) { //if making a test, it will automatically put that key,value pair 
						m.addPair(key, value); //in the answers map
					}
				}
				m.shuffle(); //randomize the order so that order is random when question is presented
				return m;
		}
		
		return null; //needs a default return value, code will never reach this
	}
	
	public static int promptPositiveInt() { //prompts user for positive integer
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String i = br.readLine();
			int input = Integer.parseInt(i);
			if(input < 1) {
				System.out.println("Please enter a positive integer.");
				return promptPositiveInt();
			}
		
			return input;
		}
		catch(NumberFormatException e) {
			System.out.println("Please enter an integer.");
			return promptPositiveInt();
		}
		catch(Exception e) {
			System.out.println("Please enter again. Enter an integer.");
			return 0; //return promptPositiveInt();
		}
	}
	
	public static int promptPositiveInt(int max) { //prompts user for positive integer between 1 and @param max inclusive
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String i = br.readLine();
			int input = Integer.parseInt(i);
			if(input < 1) {
				System.out.println("Please enter a positive integer.");
				return promptPositiveInt();
			}
			if(input > max) {
				System.out.println("Please enter a positive integer less than or equal to " + max);
				return promptPositiveInt();
			}
		
			return input;
		}
		catch(NumberFormatException e) {
			System.out.println("Please enter an integer.");
			return promptPositiveInt();
		}
		catch(Exception e) {
			System.out.println("Please enter again. Enter an integer.");
			return 0; //return promptPositiveInt();
		}
	}
	
	public static String promptYesOrNo() throws IOException { //returns y or n to represent yes or no, respectively
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		String y = null;
		while (!done) {
			y = br.readLine();
			if(y.equalsIgnoreCase("y") || y.equalsIgnoreCase("n")) {
				done = true;
			}
			else {
				System.out.println("Please enter y or n");
			}
		}
		return y;
	}
}
