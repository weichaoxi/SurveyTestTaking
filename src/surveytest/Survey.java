package surveytest;
import java.util.*;
import java.io.*;

public class Survey implements Serializable {
	
	private static final long serialVersionUID = 4413393039964095914L;
	protected ArrayList<Question> questions;
	protected String title; //Survey 1 etc.. name of survey
	private static Scanner sc; //scanner for ints
	private static Scanner st; //scanner for strings
	private static BufferedReader br;
	protected int numberOfQuestions = 0;
	
	public Survey(String t) {
		title = t;
		numberOfQuestions = 0;
		questions = new ArrayList<>();
	}
	
	public void addQuestion(Question q) {
		questions.add(q);
		numberOfQuestions++;
	}
	
	public void display() { //displays this survey
		System.out.println("Displaying " + title);
		System.out.println();
		int questionNum = 0;
		for(Question q : questions) {
			questionNum++;
			System.out.print("Question " + questionNum + ") ");
			q.display();
			System.out.println();
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public void modify() throws IOException { //modifies a question in this survey
		System.out.println("What question do you wish to modify? (Enter question number)");
		int questionNumber = QuestionCreator.promptPositiveInt(numberOfQuestions);
		int counter = 0;
		for(Question qu : questions) {
			counter++; //go to the nth question (the one the user chose)
			if(questionNumber == counter) {
				qu.modify(); 
				break;
			}
		}
		//would you like to modify another question?
	}
	
	//serialize the parameter survey/test to a file
	//file name is "type-title.ser" where type is Survey or Test (class names)
	public static void serialize(Survey s) throws IOException {
		OutputStream outStream = new FileOutputStream(s.getClass().getSimpleName() + "-" + s.getTitle() + ".ser"); //naming the file
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(outStream);
        fileObjectOut.writeObject(s);
        fileObjectOut.close();
        outStream.close();
	}
	
	public static Survey load(String file) throws IOException, ClassNotFoundException { //deserialize and load
		InputStream inStream = new FileInputStream(file);
		ObjectInputStream fileObjectIn = new ObjectInputStream(inStream);
		if(file.startsWith("Survey")) {
			Survey survey = (Survey) fileObjectIn.readObject();
			survey.updateNumQuestions();
			fileObjectIn.close();
			inStream.close();
			return survey;
		}
		else if(file.startsWith("Test")) {
			Test test = (Test) fileObjectIn.readObject();
			test.updateNumQuestions();
			fileObjectIn.close();
			inStream.close();
			return test;
		}
		fileObjectIn.close();
		inStream.close();
		return null;
	}
	
	public void updateNumQuestions() { //after loading a previously existing test
		int count = 0;
		for (Question q : questions) {
			count++;
		}
		numberOfQuestions = count;
	}
	
	public void take() throws IOException { //take this survey, after its taken, survey is serialized with the user answers
		System.out.println("Taking " + title);
		int counter = 0;
		for(Question q : questions) {
			counter++;
			System.out.print("Question " + counter + ") ");
			q.take();
		}
		serialize(this); //save the survey/test (which contains the user responses) after it has been taken
		System.out.println(title + " has been completed!");
	}
	
	public void tabulate() { //tabulate this survey, displaying number of responses to each question
		System.out.println("Tabulating " + title);
		int qcounter = 0;
		for(Question q : questions) {
			qcounter++;
			System.out.println("Question " + qcounter + ") ");
			q.tabulate();
			System.out.println();
		}
		System.out.println();
		System.out.println(title + " has been tabulated!");
	}
	
	//@param what the user chose in 1st menu, Survey or Test
	public static void displayOptions(String type) throws IOException, ClassNotFoundException { //put survey param here in case ?
		System.out.println("1) Create a new " + type);
		System.out.println("2) Display a " + type);
		System.out.println("3) Load a " + type);
		System.out.println("4) Save a " + type);
		System.out.println("5) Modify an existing " + type);
		System.out.println("6) Take a " + type);
		System.out.println("7) Tabulate a " + type);
		if(type.equals("Survey")) {
			System.out.println("8) Quit");
		}
		if(type.equals("Test")) {
			System.out.println("8) Grade a " + type);
			System.out.println("9) Quit");
		}
		sc = new Scanner(System.in);
		st = new Scanner(System.in);
		br = new BufferedReader(new InputStreamReader(System.in));
		int input = 0;
		try {
			input = sc.nextInt();
			if(input < 1 || input > 9) {
				System.out.println("Please choose one of the above choices.");
				displayOptions(type);
				return;
			}
		}
		catch(Exception e) {
			System.out.println("Please choose one of the above choices.");
			displayOptions(type);
			return;
		}
		
		runOption(input, type);
	}
	
	public static void runOption(int input, String type) throws IOException, ClassNotFoundException {
		switch(input) {
		case 1: System.out.println("Name of your new " + type + "?"); 
				String name = st.nextLine();
				Survey s = null;
				if(type.equals("Survey")) {
					s = new Survey(name); 
				}
				else if(type.equals("Test")) {
					s = new Test(name); //look at this later, 
				}
				
				System.out.println("How many questions do you want " + s.title + " to have?");
				int numQuestions;
				sc.nextLine();
				numQuestions = QuestionCreator.promptPositiveInt();
				
				for(int i = 1; 1 <= numQuestions; i++) {
					s.addQuestion(QuestionCreator.createQuestion(type));
					if(i == numQuestions) break;
				}
				System.out.println("New " + type + " named " + s.title + " has been created!");
				
				if(type.equals("Survey")) {
					Storage.addSurvey(s);
				}
				else if(type.equals("Test")) {
					Storage.addTest((Test) s);
				}
				
				Driver.main(new String[0]); //go back to main menu after making survey
				return;
				
				
		case 2: if(type.equals("Survey")) {
					if(Storage.getSurveys().isEmpty()) {
						System.out.println("No " + type + "s" + " have been created or loaded yet!");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a survey to display");
					int counter = Storage.listSurveys();
					int choice = QuestionCreator.promptPositiveInt(counter);
					if(choice > Storage.getSurveys().size() || choice < 1) {
						System.out.println("Please enter an appropriate survey to display by number.");
						runOption(input, type);
					}
					System.out.println("Displaying " + Storage.getSurvey(choice-1).getTitle());
					Storage.getSurvey(choice-1).display(); //as index begins with 0
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
					
				} //problem is checking arraylist to see if a survey in list is a survey or test
			
				else if(type.equals("Test")) {
					if(Storage.getTests().isEmpty()) {
						System.out.println("No " + type + "s" + " have been created or loaded yet!");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a test to display");
					int counter = Storage.listTests();
					int choice = QuestionCreator.promptPositiveInt(counter);
					if(choice > Storage.getTests().size() || choice < 1) {
						System.out.println("Please enter an appropriate test to display by number.");
						runOption(input, type);
					}
					System.out.println("Displaying " + Storage.getTest(choice-1).getTitle());
					Storage.getTest(choice-1).display(); //as index begins with 0
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
				
		case 3: if(type.equals("Survey")) {
					File dir = new File("."); //this should be SurveyTestTaking, the project folder that contains src folder
					File[] foundFiles = dir.listFiles(new FilenameFilter() {
					    public boolean accept(File dir, String name) {
					        return name.startsWith("Survey-"); //find all files that begin with survey
					    }
					});

					if(foundFiles.length == 0) {
						System.out.println("There are no surveys to load!");
						displayOptions(type);
						return;
					}
					
					int num = 0;
					System.out.println("Choose a " + type + " to load");
					for (File file : foundFiles) {
						num++;
					    System.out.println(num + ") " + load(file.getName()).getTitle());
					}    
					int choice = QuestionCreator.promptPositiveInt(num); //pick a survey to load
					int search = 0;
					for (File file : foundFiles) {
						search++;
						if(search == choice) {
							Survey survey = load(file.getName());
							Storage.addSurvey(survey);
							System.out.println(survey.getTitle() + " successfully loaded!");
						}
					}   
					System.out.println("Returning to main menu......");
					Driver.main(new String[0]);
					return;
				}
				
				else if(type.equals("Test")) {
					File dir = new File("."); //this should be SurveyTestTaking, the project folder that contains src folder
					File[] foundFiles = dir.listFiles(new FilenameFilter() {
					    public boolean accept(File dir, String name) {
					        return name.startsWith("Test-"); //find all files that begin with test
					    }
					});
					if(foundFiles.length == 0) {
						System.out.println("There are no tests to load!");
						displayOptions(type);
						return;
					}	
					
					int num = 0;
					System.out.println("Choose a " + type + " to load");
					for (File file : foundFiles) {
						num++;
					    System.out.println(num + ") " + load(file.getName()).getTitle());
					}    
					int choice = QuestionCreator.promptPositiveInt(num); //pick a test to load
					int search = 0;
					for (File file : foundFiles) {
						search++;
						if(search == choice) {
							Test test = (Test) load(file.getName());
							Storage.addTest(test);
							System.out.println(test.getTitle() + " successfully loaded!");
						}
					}   
					System.out.println("Returning to main menu......");
					Driver.main(new String[0]);
					return;
				}
		
		case 4: if(type.equals("Survey")) { //serialize chosen to a file
					if(Storage.getSurveys().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to save! (create or load one)");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a survey to save");
					int counter = Storage.listSurveys();
					int choice = QuestionCreator.promptPositiveInt(counter);
					serialize(Storage.getSurvey(choice-1));
					System.out.println(Storage.getSurvey(choice-1).getTitle() + " has been saved!");
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
				else if(type.equals("Test")) {
					if(Storage.getTests().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to save! (create or load one)");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a test to save");
					int counter = Storage.listTests();
					int choice = QuestionCreator.promptPositiveInt(counter);
					serialize(Storage.getTest(choice-1));
					System.out.println(Storage.getTest(choice-1).getTitle() + " has been saved!");
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
		//modify existing
		case 5: if(type.equals("Survey")) {
					if(Storage.getSurveys().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to modify! (create or load one)");
						displayOptions(type);
						return;
					}
					boolean surveyFound = false;
					System.out.println("Enter the name of the " + type + " you wish to modify");
					String surveyName = br.readLine();
					
					for(Survey survey : Storage.getSurveys()) {
						if(surveyName.equals(survey.getTitle())) {
							surveyFound = true;
							survey.modify();
							break;
						}
					}
					
					if(!surveyFound) {
						System.out.println("No " + type + " with that name exists. Please enter again.");
						runOption(5, type);
						return;
					}
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
			
				
				else if(type.equals("Test")) {
					if(Storage.getTests().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to modify! (create or load one)");
						displayOptions(type);
						return;
					}
					boolean testFound = false;
					System.out.println("Enter the name of the " + type + " you wish to modify");
					String testName = br.readLine();
					
					for(Test test : Storage.getTests()) {
						if(testName.equals(test.getTitle())) {
							testFound = true;
							test.modify();
							break;
						}
					}
					
					if(!testFound) {
						System.out.println("No " + type + " with that name exists. Please enter again.");
						runOption(5, type);
						return;
					}
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
		
		//take
		case 6: if(type.equals("Survey")) { //serialize chosen to a file
					if(Storage.getSurveys().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to take! (create or load one)");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a survey to take");
					int counter = Storage.listSurveys();
					int choice = QuestionCreator.promptPositiveInt(counter);
					Storage.getSurvey(choice-1).take();
					
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
				else if(type.equals("Test")) {
					if(Storage.getTests().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to take! (create or load one)");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a test to take");
					int counter = Storage.listTests();
					int choice = QuestionCreator.promptPositiveInt(counter);
					Storage.getTest(choice-1).take();
				
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
			
		//tabulate
		case 7: if(type.equals("Survey")) {
					if(Storage.getSurveys().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to tabulate!");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a survey to tabulate");
					int counter = Storage.listSurveys();
					int choice = QuestionCreator.promptPositiveInt(counter);
					Storage.getSurvey(choice-1).tabulate();
					
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
				else if(type.equals("Test")) {
					if(Storage.getTests().isEmpty()) {
						System.out.println("No " + type + "s" + " exist to tabulate!");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a test to tabulate");
					int counter = Storage.listTests();
					int choice = QuestionCreator.promptPositiveInt(counter);
					Storage.getTest(choice-1).tabulate();
				
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
			 
		case 8: if(type.equals("Survey")) { //quit for survey, grade for test
					sc.close();
					st.close();
					br.close();
					System.out.println("Quitting...");
					System.exit(0);
				}
				else if(type.equals("Test")) {
					if(Storage.getTests().isEmpty()) { //add isTaken variable?
						System.out.println("No " + type + "s" + " exist to grade! (take one first)");
						displayOptions(type);
						return;
					}
					System.out.println("Choose a test to grade");
					int counter = Storage.listTests();
					int choice = QuestionCreator.promptPositiveInt(counter);
					System.out.println("You scored " + Storage.getTest(choice-1).grade() + " on " + Storage.getTest(choice-1).getTitle() + " without factoring essay questions");
					
					System.out.println("Returning to main menu...");
					Driver.main(new String[0]);
					return;
				}
			
		
		case 9: if(type.equals("Test")) {
					sc.close();
					st.close();
					br.close();
					System.out.println("Quitting...");
					System.exit(0);
				}
				else {
					System.out.println("Please choose one of the above options.");
					displayOptions(type);
				}
		}
	}
	
	
}
