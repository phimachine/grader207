package worksheet9;
import java.util.*;
import java.io.*;

public class InputFromFile {
	public static void main(String[] args) throws FileNotFoundException {
		double avg = fileAverage("data.txt");
		System.out.println("Average is: "+ avg);
	}

	public static double fileAverage(String fileName) throws FileNotFoundException {
	File f = new File (fileName);
	Scanner fileIn =  new Scanner(f);
	ArrayList<Integer>scores = new ArrayList <Integer>();
	int sum = 0;
	int counter = 0;
	while(fileIn.hasNextInt());
	{ int num = fileIn.nextInt();	
	sum = sum+num;
	counter++;
	}
	double avg = (double)(sum)/counter;
	return avg;
	
	}
	}

	