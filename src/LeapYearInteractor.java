import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class LeapYearInteractor implements Interactor, Judge {
    static Random random = new Random();
    int max = 99999;
    int year;

    public LeapYearInteractor(int type){
        if (type==0){
            // not divisible by 4
            year = random.nextInt(max);
            while (year% 4==0){
                year = random.nextInt(max);
            }
        }else if (type==1){
            // divisible by 4 but not 100
            year = random.nextInt(max);
            while (year % 25==0){
                year = random.nextInt(max);
            }
            year = year *4;
        }else if (type==2){
            // divisible by 100 but not 400
            year =  random.nextInt(max);
            while (year % 4 == 0){
                year =  random.nextInt(max);
            }
            year = year * 100;
        }else {
            year =  random.nextInt(max);
            year = 400 * year;
        }

        if (type==0 || type == 2){
            if (isLeapYear()){
                System.out.println("This is not right");
                System.exit(-1);
            }
        }else{
            if (!isLeapYear()){
                System.out.println("This is not right");
                System.exit(-1);
            }
        }
    }

    @Override
    public String interact(String currentOutput) {
        currentOutput = currentOutput.toLowerCase();
        String input = null;
        if (!currentOutput.equals("") && currentOutput!=null){

        }
        if (currentOutput.contains("enter")){
            input=""+year+"\n";
        }
        return input;
    }

    @Override
    public void prepare(File tempPathFile) throws IOException {
        return;
    }

    public boolean isLeapYear(){
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else if (year % 4 == 0) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter) throws StudentFatalMistake {
        int mistakes = 1;

        ArrayList<String> lines=new ArrayList<>();

        for (String output :
                outputs) {
            String[] splitted = output.split("\n");
            for (int i = 0; i < splitted.length; i++) {
                lines.add(splitted[i]);
            }
        }

        boolean isLeapYear=isLeapYear();
        boolean studentClassification=true;

        for (String line :
                lines) {
            line=line.toLowerCase();
            if (line.contains("not")){
                studentClassification=false;
            }
        }

        if (studentClassification!=isLeapYear){
            mistakes=1;
        }else{
            mistakes=0;
        }
        reporter.divider("entered year:"+year);
        reporter.divider("correct classification:"+isLeapYear);
        reporter.divider("student classification:"+studentClassification);
        return mistakes;
    }
}
