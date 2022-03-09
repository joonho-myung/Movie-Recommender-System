import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class generateUserData {

	
	public static HashMap<Integer, String> userRating = new HashMap<Integer, String>();
	public static String movies[] = new String[250];
	public static int upperbound = 6;
	public static ArrayList<HashMap<String, Integer>> user = new ArrayList<HashMap<String, Integer>>();
	public static HashMap<Integer, ArrayList<HashMap<String, Integer>>> userData = new HashMap<Integer, ArrayList<HashMap<String, Integer>>>();
	

	/*
	 *  Read movie names from the file
	 */
	private static void readMovie() {
		
		try {
			File file = new File("IMDbTopMovies.txt");
			Scanner scanner = new Scanner(file);
			
			int counter = 0;
			
			while (scanner.hasNextLine()) {
				String name = scanner.nextLine();
				movies[counter] = name;
				counter++;
			}
		}	catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}

	}

	
	/*
	 * Create 30 user data on 250 movies
	 */
	public static HashMap<Integer, String> rating(){
		readMovie();
		
		
		int userNumber = 1;
		String userRate = "";

		for(int i = 1; i < 31; i++) {
			userRate = "";

			for(int j = 0; j < 250; j++) {
				Random rand = new Random();	
				int random = rand.nextInt(upperbound);
				userRate = userRate + random;	
			}
			userRating.put(userNumber, userRate);
			
			
			userNumber++;
		}
		
		return userRating;
	}

	
}
