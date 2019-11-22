import java.util.*;
import java.io.*;

/**
 * This class reads a file and computes average of all the numbers in the file.
 * Make sure you have the file "data.txt" saved in the same project as this class.
 * For this, you need to import the file (data.txt) into your eclipse project.
 * Check out instructions in the worksheet description pdf for importing a file into eclipse.
 *
 * The file data.txt will have the following structure,
 * 		2 4 5 6 8 10 65 Q
 * DO NOT CHANGE THE NAME OF THE INPUT FILE. Keep it same as data.txt
 *
 * @author spal
 *
 */
public class InputFromFile {

    public static void main(String[] args) throws FileNotFoundException {

        double avg = fileAverage("data.txt");
        System.out.println("Average is: "+ avg);

    } // end of main

    /**
     * This method takes a file name (type String) as input.
     * It must throw a FileNotFoundException - add it in the correct place in the
     * method header.
     *
     * The method should do the following,
     * 1. Create a file object
     * 2. Pass this file object as input to a scanner object to read it
     * 3. Read all numbers in the file and compute their average (you need
     * 		to do type checking here for the input, and stop when a non-integer
     * 		is encountered).
     * 4. Return average (type Double)
     *
     * @param filename - String file name that will be read
     * @return average of the numbers in the file
     * @throws FileNotFoundException
     */
    public static double fileAverage(String filename) throws FileNotFoundException {
        File f = new File(filename);
        Scanner fileIn = new Scanner(f);
        ArrayList<Integer> scores = new ArrayList<Integer>();
        int num = 0;
        while (fileIn.hasNextInt())
        {
            num = fileIn.nextInt();
            scores.add(num);
        }
        int total = 0;
        for(int i = 0; i < scores.size(); i++)
        {
            total += scores.get(i);
        }
        return total / scores.size();

    }
}
