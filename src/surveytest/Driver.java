package surveytest;
import java.io.*;
import java.util.*;

//@author Weichao Xi
public class Driver { 
	
	public static void main(String[] args) throws IOException, ClassNotFoundException { //main method, runs this when run
		int input = 0;
		System.out.println("1)Survey");
		System.out.println("2)Test");
		System.out.println("3)Quit");
	
		Scanner sc = new Scanner(System.in);
		try {
			input = sc.nextInt();
			if(input != 1 && input != 2 && input != 3) {
				System.out.println("Please choose 1, 2, or 3.");
				main(args); //break here?
				return;
			}
		}
		catch(Exception e) {
			System.out.println("Please choose 1 or 2 or 3.");
			main(args);
			return;
		}
		
		switch(input) {
		case 1: Survey.displayOptions("Survey"); //boolean to determine whether is test?
				return;
		case 2: Survey.displayOptions("Test");
				return;
		case 3: System.out.println("Quitting......");
				sc.close();
				System.exit(0);
		}
		
	}

}
