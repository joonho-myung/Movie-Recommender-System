import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class userinterface {

	public static String movies[] = new String[250];	
	public static String filmBeingRated = "";
	public static int rate = 0;
	public static HashMap<String, Integer> movieList = new HashMap<String, Integer>();
	public static HashMap<Integer, String> userRating = new HashMap<Integer, String>();
	public static HashMap<String, Double> recommendation = new HashMap<String, Double>();
	
	
	public static void main(String[] args) {
		readMovie();
		frame();
	}
	
	
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
	 * Create user interface with JFrame
	 */
	public static void frame(){
		JFrame frame = new JFrame();
		
		JSpinner sp1 = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
		sp1.setBounds(340, 130, 28, 26);
		sp1.setEditor(new JSpinner.DefaultEditor(sp1));

		
		JList jl1 = new JList(movies);
		jl1.setBounds(20, 20, 250, 250);
		jl1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jl1.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) {
					filmBeingRated = (String) jl1.getSelectedValue();
				}
			}

		});
		
		JScrollPane sc1 = new JScrollPane(jl1);
		sc1.setBounds(20, 40, 300, 250);
		
		JTextArea ta1 = new JTextArea();
		ta1.setEditable(false);
		
		JScrollPane sc2 = new JScrollPane(ta1);
		sc2.setBounds(20, 340, 610, 200);
		
		JTextArea ta2 = new JTextArea();
		ta2.setEditable(false);
		
		JScrollPane sc3 = new JScrollPane(ta2);
		sc3.setBounds(400, 40, 370, 250);
		
		JTextArea ta3 = new JTextArea();
		ta3.setEditable(false);
		
		JScrollPane sc4 = new JScrollPane(ta3);
		sc4.setBounds(20, 580, 700, 25);
		
		JButton b1 = new JButton("RATE");
		b1.setBounds(645, 500, 130, 40);
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!filmBeingRated.equals("")) {
					movieList.put(filmBeingRated, (Integer) sp1.getValue());
					ta1.append(filmBeingRated + "    Rating: " +  (Integer) sp1.getValue() + "\n");
				}
			}
		});

		JButton b2 = new JButton("CLEAR");
		b2.setBounds(645, 450, 130, 40);
		b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ta1.setText("");
				ta2.setText("");
				movieList.clear();
				ta3.setText("  Cleared all the rating history");
			}
		});
		
		JButton b3 = new JButton("RECOMMEND");
		b3.setBounds(645, 400, 130, 40);
		b3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				recommendation = userBasedRecommender.recommender(movieList, userRating);
				
				for(String s: recommendation.keySet()) {
					ta2.append(" " + s + "\n");
				}
				
			}
		});
		
		JButton b4 = new JButton("GENERATE");
		b4.setBounds(645, 350, 130, 40);
		b4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userRating = generateUserData.rating();
				ta3.setText("  Generated User Rating");
			}
		});
		
		
		JLabel lb1 = new JLabel("Movie List");
		lb1.setBounds(135, 11, 100, 20);
		lb1.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		JLabel lb2 = new JLabel("My Rated Movies");
		lb2.setBounds(270, 310, 120, 20);
		lb2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		JLabel lb3 = new JLabel("Recommended Movies");
		lb3.setBounds(510, 11, 170, 20);
		lb3.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		JLabel lb4 = new JLabel("Prompt");
		lb4.setBounds(340, 555, 170, 20);
		lb4.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		frame.add(b1);
		frame.add(b2);
		frame.add(b3);
		frame.add(b4);
		frame.add(lb1);
		frame.add(lb2);
		frame.add(lb3);
		frame.add(lb4);
		frame.add(sp1);
		frame.add(sc1);
		frame.add(sc2);
		frame.add(sc3);
		frame.add(sc4);
		frame.setTitle("Movie Recommender Machine");
		frame.setBounds(700, 150, 800, 660);
		frame.setLayout(null);
		frame.setVisible(true);
			
	}


}
