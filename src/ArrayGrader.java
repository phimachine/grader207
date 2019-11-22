import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ArrayGrader implements Interactor, Judge {
    //jibberish appended!
    public int [] array;
    public double correctVal;
    public double point5off;
    private final int len =20;
    Random random= new Random();
    boolean jibberish;

    public ArrayGrader(boolean jibberish) {
        array=new int[len];
        int sum=0;
        for (int i = 0; i < len; i++) {
            array[i]=random.nextInt(500);
            sum+=array[i];
        }
        correctVal=(double) sum/len;
        point5off=sum/len;
        this.jibberish=jibberish;
    }

    public int judgeReport(ArrayList<String> inputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                           Reporter reporter) throws StudentFatalMistake {
        String allOutputs="";
        for (String output :
                outputs) {
            allOutputs += output;
        }

        int mistakes=2;
        String[] words=allOutputs.split("\\s");
        for (String word :
                words) {
            try {
                double num = Double.parseDouble(word);
                if (num == correctVal) {
                    mistakes = 0;
                }else if (num==point5off){
                    mistakes = 1;
                }
            } catch (NumberFormatException e) {
                ;
            }
        }
        reporter.divider("MISTAKES", "" + mistakes);
        reporter.divider("correct:" +correctVal);
        return mistakes;
    }

    public void prepare(File tempPathFile) throws IOException {
        // write array to file
        File target = new File(tempPathFile,"data.txt");
        String data=" ";
        for (int n :
                array) {
            data+=n;
            data+=" ";
        }
        if (jibberish){
            data+=" jibberish 9";
        }

        FileWriter fileWriter = new FileWriter(target);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }

    @Override
    public String interact(String currentOutput) {
        return null;
    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter) throws StudentFatalMistake {
        return judgeReport(customInputs, outputs, inputMarkers, reporter);
    }
}
