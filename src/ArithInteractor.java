import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ArithInteractor implements Interactor, Judge {
    Random random = new Random();
    int max=100;
    int[] ints = new int[5];

    public ArithInteractor(){
        for (int i = 0; i < ints.length; i++) {
            ints[i]=random.nextInt(max);
        }
    }


    @Override
    public String interact(String currentOutput) {
        currentOutput = currentOutput.toLowerCase();
        String input = null;
        if (currentOutput.contains("enter")){
            input="";
            for (int i = 0; i <ints.length; i++) {
                input += ints[i]+" ";
            }
            input+="\n";
        }
        return input;
    }

    @Override
    public void prepare(File tempPathFile) throws IOException {

    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter) throws StudentFatalMistake {
        int mistakes=3;
        double[] corrects= correctAnswer();
        double sum=corrects[0];
        double prod=corrects[1];
        double average = corrects[2];

        ArrayList<String> lines=new ArrayList<>();

        for (String output :
                outputs) {
            String[] splitted = output.split("\n");
            for (int i = 0; i < splitted.length; i++) {
                lines.add(splitted[i]);
            }
        }

        for (String line :
                lines) {
            line=line.toLowerCase();
            String[] words= line.split("\\s");
            double num;
            for (String word :
                    words) {
                try {
                    num=Double.parseDouble(word);
                    if (line.contains("sum")){
                        if (num==sum){
                            mistakes-=1;
                        }else{
                            System.out.println("Stop here");
                        }
                    }else if (line.contains("average")){
                        if (num==average){
                            mistakes-=1;
                        }else{
                            System.out.println("Stop here");
                        }
                    }else if (line.contains("product")){
                        if (num==prod){
                            mistakes-=1;
                        }else {
                            System.out.println("Stop here");
                        }
                    }

                }catch(NumberFormatException e){
                    ;
                }
            }

        }
        reporter.divider("correct sum: "+sum);
        reporter.divider("correct product: "+prod);
        reporter.divider("correct average: "+average);


        return mistakes;
    }

    public double[] correctAnswer(){
        int sum=0;
        int product=1;
        for (int i = 0; i < ints.length; i++) {
            sum+=ints[i];
            product*=ints[i];
        }
        return new double[]{sum, product, sum / 5.0};
    }
}
