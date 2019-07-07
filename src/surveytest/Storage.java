package surveytest;
import java.io.Serializable;
import java.util.ArrayList;

//stores all surveys/tests created/loaded in the current main method session
public class Storage implements Serializable {
	
	private static final long serialVersionUID = 6027012106395745296L;
	private static ArrayList<Survey> surveys = new ArrayList<>();
	private static ArrayList<Test> tests = new ArrayList<>();
	
	public static void initiate() { //put this in Driver? starts the list etc
		
	}
	
	public static void addSurvey(Survey s) {
		surveys.add(s);
	}
	
	public static ArrayList<Survey> getSurveys() {
		return surveys;
	}
	
	public static Survey getSurvey(int index) {
		return surveys.get(index);
	}
	
	public static void addTest(Test t) {
		tests.add(t);
	}
	
	public static ArrayList<Test> getTests() {
		return tests;
	}
	
	public static Test getTest(int index) {
		return tests.get(index);
	}
	
	public static int listSurveys() { //list all surveys 1).. 2).. etc per line,returns number of surveys
		int counter = 0;
		for(Survey su : Storage.getSurveys()) {
			counter++;
			System.out.println(counter + ") " + su.getTitle());
		}
		return counter;
	}
	
	public static int listTests() {
		int counter = 0;
		for(Test t : Storage.getTests()) {
			counter++;
			System.out.println(counter + ") " + t.getTitle());
		}
		return counter;
	}
	
}
