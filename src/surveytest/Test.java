package surveytest;

import java.io.IOException;

public class Test extends Survey {
	
	private static final long serialVersionUID = -2143101362808732352L;
	private boolean isTaken;
	
	public Test(String t) {
		super(t);
		isTaken = false;
	}
	
	public void addQuestion(Question q) {
		super.addQuestion(q);
	}
	
	@Override
	public void display() { //displays this test
		System.out.println("Displaying " + title);
		System.out.println();
		int questionNum = 0;
		for(Question q : questions) {
			questionNum++;
			System.out.print("Question " + questionNum + ") ");
			q.display();
			q.displayAnswer();
			System.out.println();
		}
	}
	
	@Override
	public String getTitle() {
		return super.getTitle();
	}
	
	public void modify() throws IOException {
		System.out.println("What question do you wish to modify? (Enter question number)");
		int questionNumber = QuestionCreator.promptPositiveInt(numberOfQuestions);
		int counter = 0;
		for(Question qu : questions) {
			counter++; //go to the nth question (the one the user chose)
			if(questionNumber == counter) {
				qu.modify(); 
				qu.modifyAnswer();
				break;
			}
		}
	}
	
	public void take() throws IOException {
		super.take();
		isTaken = true;
	}
	
	public void tabulate() {
		super.tabulate();
	}
	
	//to be done in future homework assignment..... //returns score
	public String grade() {
		System.out.println("Grading " + title + "...");
		int score = 0;
		int maxScore = 0;
		for(Question q : questions) {
			if(q.grade() == 1) {
				score += 10;
				maxScore += 10;
			}
			else if(q.grade() == -1) {
				maxScore += 10;
			}
		}
		return (score + "/" + maxScore);
	}
	
	public boolean hasBeenTaken() {
		return isTaken;
	}
	
	public void updateNumQuestions() { //after loading a previously existing test
		super.updateNumQuestions();
	}
}
