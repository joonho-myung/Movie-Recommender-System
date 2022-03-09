import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class userBasedRecommender {

	public static HashMap<String, Double> recommend = new HashMap<String, Double>();
	public static HashMap<Integer, Double> userSimilarity = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> simForWrite = new HashMap<Integer, Double>();
	public static String movies[] = new String[250];
	public static HashMap<String, Integer> currMovieRate = new HashMap<String, Integer>();
	public static HashMap<String, Double> expectedRating = new HashMap<String, Double>();
	public static int rating[][] = new int [30][250];
	
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	
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
	 *  Create user rating, users similarity files
	 */
	public static void createFiles() {
		
		try {
			File file = new File("usersRating.txt");
			if(file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			}
			else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			File file2 = new File("usersSimilarity.txt");
			if(file2.createNewFile()) {
				System.out.println("File created: " + file2.getName());
			}
			else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		// Create 30 user ratings on 250 movies
		try {
			FileWriter writer = new FileWriter("usersRating.txt");
			
			for(int i = 1; i < 31; i++) {
				writer.write("User " + i + "\n\n");
				for(int k = 0; k < 250; k++) {
					writer.write(movies[k] + ": ");
					writer.write(rating[i-1][k] + "\n");
				}
			
				writer.write("\n\n\n");
			}
		
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		
		try {
			FileWriter writer2 = new FileWriter("usersSimilarity.txt");
			
			writer2.write("User Similarity" + "\n\n");
			
			for(int i = 1; i < 31; i++) {
				writer2.write("User " + i + "   " +simForWrite.get(i) + "\n");
			}
			
			writer2.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	
	/*
	 * Movie recommender system based on user data
	 */
	public static HashMap<String, Double> recommender(HashMap<String, Integer> movieList, HashMap<Integer, String> userRating) {
		readMovie();
		
		ArrayList<Integer> ratingbyUser = new ArrayList<Integer>();
		ArrayList<Integer> ratingbyMe = new ArrayList<Integer>();	

		rating = new int [30][250];		
		
		for(int k = 1; k < 31; k++) {
			currMovieRate = new HashMap<String, Integer>();
			ratingbyUser = new ArrayList<Integer>();
			ratingbyMe = new ArrayList<Integer>();
			
			
			for(int b = 0; b < 250; b++) {
				rating[k-1][b] = (userRating.get(k).charAt(b)) - '0';
			}
		
			for(int i = 0; i < 250; i++) {
				currMovieRate.put(movies[i], rating[k-1][i]);	
			}
			
			for(String s : movieList.keySet()) {
				
				if(currMovieRate.containsKey(s)) {
					ratingbyUser.add(currMovieRate.get(s));
					ratingbyMe.add(movieList.get(s));
				}
			}
			
			double myAverage = 0;
			double userAverage = 0;
				
			for(int j = 0; j < ratingbyUser.size(); j++) {
				userAverage = userAverage + ratingbyUser.get(j);
			}
			userAverage = (userAverage/ratingbyUser.size());
			userAverage = (double) Math.round(userAverage * 100.0) / 100.0;
			
			for(int h = 0; h < ratingbyMe.size(); h++) {
				myAverage = myAverage + ratingbyMe.get(h);
			}
			myAverage = (myAverage/ratingbyMe.size());
			myAverage = (double) Math.round(myAverage * 100.0) / 100.0;
			

			double numerator = 0;
			double denominator = 0;
			double denominatorMe = 0;
			double denominatorUser = 0;
			double similarity = 0;
			
			for(int a = 0; a < ratingbyUser.size(); a++) {
				numerator = numerator + (((double) ratingbyUser.get(a) - userAverage) * ((double) ratingbyMe.get(a) - myAverage));
			}
			for(int b = 0; b < ratingbyUser.size(); b++) {
				denominatorUser = denominatorUser + Math.pow(((double) ratingbyUser.get(b) - userAverage), 2);
				denominatorMe = denominatorMe + Math.pow(((double) ratingbyMe.get(b) - myAverage), 2);
			}
			
			denominator = Math.sqrt(denominatorUser * denominatorMe);
			
			similarity = numerator/denominator;
			
			similarity = (double) Math.round(similarity * 1000d) / 1000d;
			
			simForWrite.put(k, similarity);
			if(similarity > 0.5) {
				userSimilarity.put(k, similarity);
			}
		}
		
		
		for(int k = 0; k < 250; k++) {
			String movieName = "";
			double sim = 0;
			int rat = 0;
			double total = 0;
			double numerator = 0;
			double denominator = 0;
			
			movieName = movies[k];
			
			if(!movieList.containsKey(movieName)) {
				
				for(int a = 1; a < 31; a++) {

					if(userSimilarity.containsKey(a)) {
						sim = userSimilarity.get(a);
						rat = rating[a-1][k];
						
						numerator = numerator + (sim * rat);
						denominator = denominator + sim;
					}						
				}
				
				total = numerator / denominator;
				total = (double) Math.round(total * 1000d) / 1000d;
				
				//recommend calculated similar movie rating which is over 3.5
				if(total > 3.5) {
					expectedRating.put(movieName, total);
				}	
			}
		}
		
		
		//create data with recommended data
		createFiles();
		
		return expectedRating;
	}
	
	
}






