import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class TaxFiler implements Interactor, Judge {

    boolean single;
    double singleIncome;
    double myIncome;
    double spouseIncome;
    int dependents;

    public TaxFiler(double singleIncome, int dependents) {
        this.singleIncome = singleIncome;
        this.dependents = dependents;
        this.single = true;
    }

    public TaxFiler(double myIncome, double spouseIncome, int dependents) {
        this.myIncome = myIncome;
        this.spouseIncome = spouseIncome;
        this.dependents = dependents;
        this.single=false;
    }

    @Override
    public String interact(String output) {
        // the TaxFiler gets a bunch of questions and answer them, well
        output=output.toLowerCase();
        List<String> words=Arrays.asList(output.split("\\W"));
        String input=null;

        if (words.contains("status")&& words.contains("income") && words.contains("tax")){
            input=null;
        } else if (words.contains("marital")||words.contains("status")){
            if (words.contains("s")) {
                input = single ? "s" : "m";
            }else{
                input = single ? "single" : "married";
            }
        }else if(output.contains("dependent")){
            input=""+dependents;
        }else if (single){
            if(output.contains("income")){
                input =""+singleIncome;
            }
        }else{
            if(output.contains("spouse")){
                input = ""+spouseIncome;
            }else if (output.contains("income")){
                input = ""+myIncome;
            }
        }
        if (input !=null) {
            return input +"\n";
        }else{
            return null;
        }
    }

    public TaxReport getExpectedReport(){
        TaxReport tr;
        if (single){
            tr=TaxReturn.computeTax(single, singleIncome, 0, 0, dependents);
        }else{
            tr=TaxReturn.computeTax(single, 0, myIncome, spouseIncome, dependents);
        }
        return tr;
    }

//    public Method getJudgment(){
//        try{
//            return TaxFiler.class.getMethod("judgeReport", ArrayList.class, ArrayList.class, ArrayList.class);
//        } catch (NoSuchMethodException e){
//            e.printStackTrace();
//            System.exit(-55);
//            return null;
//        }
//    }

    public int judgeReport(ArrayList<String> inputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                           Reporter reporter) {
        int mistakes=0;
        int outputStartsAt = inputMarkers.get(inputMarkers.size() - 1);
        String output=" ";
        for (int i = outputStartsAt; i < outputs.size(); i++) {
            output=output+" "+outputs.get(i);
        }
        output=output.toLowerCase();
        InputStream is = new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8));
        Scanner scan= new Scanner(is);

        TaxReport dueTax = getExpectedReport();

        boolean missingDependent = true;
        boolean missingIncome = true;
        boolean missingStatus = true;
        boolean missingTax = true;
        boolean done=false;
        while (!done){
            // try string
            String word;
            if (scan.hasNext()){
                word = scan.next();
                System.out.println(word);
                try{
                    int integer=Integer.parseInt(word);
                    if (integer==dependents){
                        missingDependent=false;
                    }
                }catch (NumberFormatException e) {
                }

                try{
                    String tempword=word.replace(",","");
                    double db=Double.parseDouble(tempword);
                    if (db== dueTax.getAGI()){
                        missingIncome=false;
                    }
                    if (db==dueTax.getTax()){
                        missingTax=false;
                    }
                }catch (NumberFormatException e) {
                }

                if (single){
                    if (word.equals("single")){
                        missingStatus=false;
                    }
                }
                if (!single && word.equals("married")){
                    missingStatus=false;
                }
            }else{
                done=true;
            }
        }

        mistakes+=missingDependent? 1:0;
        mistakes+=missingIncome? 1:0;
        mistakes+=missingStatus? 1:0;
        mistakes+=missingTax? 1:0;

        reporter.divider("MISTAKES", "" + mistakes);
        reporter.divider("correct dependent "+ dueTax.dependents, "" + mistakes);
        reporter.divider("correct income "+dueTax.AGI, "" + mistakes);
        reporter.divider("correct single "+dueTax.single, "" + mistakes);
        reporter.divider("correct tax "+dueTax.getTax(), "" + mistakes);

        reporter.divider("missing dependent "+missingDependent, "" + mistakes);
        reporter.divider("missing income "+missingIncome, "" + mistakes);
        reporter.divider("missing status "+missingStatus, "" + mistakes);
        reporter.divider("missing tax "+missingTax, "" + mistakes);



        if (mistakes>0){
            is = new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8));
            scan= new Scanner(is);
            done=false;
            while (!done){
                // try string
                String word;
                if (scan.hasNext()){
                    word = scan.next();
                    System.out.println(word);
                    try{
                        int integer=Integer.parseInt(word);
                        if (integer==dependents){
                            missingDependent=false;
                        }
                    }catch (NumberFormatException e) {
                    }

                    try{
                        String tempword=word.replace(",","");
                        double db=Double.parseDouble(tempword);
                        if (db== dueTax.getAGI()){
                            missingIncome=false;
                        }
                        if (db==dueTax.getTax()){
                            missingTax=false;
                        }
                    }catch (NumberFormatException e) {
                    }

                    if (single){
                        if (word.equals("single")){
                            missingStatus=false;
                        }
                    }
                    if (!single && word.equals("married")){
                        missingStatus=false;
                    }
                }else{
                    done=true;
                }
            }

        }
        return mistakes;
    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                        Reporter reporter) {
        return judgeReport(customInputs, outputs, inputMarkers, reporter);
    }
}
