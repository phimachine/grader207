import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TaxFiler implements Interactor, Judge {

    boolean single;
    double singleIncome;
    double myIncome;
    double spouseIncome;
    int dependents;
    int drunk;
    Random random = new Random();
    int drunkat;


    public TaxFiler(double singleIncome, int dependents, int drunk) {
        this.singleIncome = singleIncome;
        this.dependents = dependents;
        this.single = true;
        this.drunk = drunk;
        if (drunk > 0) {
            drunkat = random.nextInt(3);
        } else {
            drunkat = -1;
        }
    }


    public TaxFiler(double myIncome, double spouseIncome, int dependents, int drunk) {
        this.myIncome = myIncome;
        this.spouseIncome = spouseIncome;
        this.dependents = dependents;
        this.single = false;
        this.drunk = drunk;
        if (drunk > 0) {
            drunkat = random.nextInt(3);
        } else {
            drunkat = -1;
        }
    }


    @Override
    public String interact(String output) {
        // the TaxFiler gets a bunch of questions and answer them, well
        output = output.toLowerCase();
        List<String> words = Arrays.asList(output.split("\\W"));
        String input = null;
//         if (drunk==0){
//             if (words.contains("status")&& words.contains("income") && words.contains("tax")){
//                 input=null;
//             } else if (words.contains("marital")||words.contains("status")){
//                 if (words.contains("s")) {
//                     input = single ? "s" : "m";
//                 }else{
//                     input = single ? "single" : "married";
//                 }
//             }else if(output.contains("dependent")||output.contains("depedents")){
//                 input=""+dependents;
//             }else if (single){
//                 if(output.contains("income")){
//                     input =""+singleIncome;
//                 }
//             }else{
//                 if(output.contains("spouse")){
//                     input = ""+spouseIncome;
//                 }else if (output.contains("income")){
//                     input = ""+myIncome;
//                 }
//             }
//             if (input !=null) {
//                 return input +"\n";
//             }else{
//                 return null;
//             }
//         }else{

        if (words.contains("status") && words.contains("income") && words.contains("tax")) {
            input = null;
        } else if (words.contains("marital") || words.contains("status")) {
            if (drunkat == 0) {
                input = "jibberish";
            } else {
                if (words.contains("s")) {
                    input = single ? "s" : "m";
                } else {
                    input = single ? "single" : "married";
                }
            }

        } else if (output.contains("dependent") || output.contains("depedents")) {
            if (drunkat == 1) {
                if (drunk == 1) {
                    input = "" + "-10";
                } else {
                    input = "jibberisha";
                }
            }else{
                 input=""+dependents;
            }
        } else if (single) {
            if (output.contains("income")) {
                if (drunkat == 2) {
                    if (drunk == 1) {
                        input = "-100";
                    } else {
                        input = "jibberish";
                    }
                } else {
                    input = "" + singleIncome;
                }
            }
        } else {
            if (output.contains("spouse")||output.contains("partner")) {
                if (drunkat == 2) {
                    if (drunk == 1) {
                        input = "-100";
                    } else {
                        input = "jibberish";
                    }
                } else {
                    input = "" + spouseIncome;
                }
            } else if (output.contains("income")) {
                if (drunkat == 2) {
                    if (drunk == 1) {
                        input = "-100";
                    } else {
                        input = "jibberish";
                    }
                } else {
                    input = "" + myIncome;
                }
            }
        }
        if (input != null) {
            return input + "\n";
        } else {
            return null;
        }
    }

    @Override
    public void prepare(File tempPathFile) throws IOException {
        return;
    }

    public TaxReport getExpectedReport() {
        TaxReport tr;
        if (single) {
            tr = TaxReturn2.computeTax(single, singleIncome, 0, 0, dependents);
        } else {
            tr = TaxReturn2.computeTax(single, 0, myIncome, spouseIncome, dependents);
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
                           Reporter reporter) throws StudentFatalMistake {
        boolean missingDependent = true;
        boolean missingIncome = true;
        boolean missingStatus = true;
        boolean missingTax = true;
        if (drunk == 0) {
            int mistakes = 0;
            int outputStartsAt;
            try {
                outputStartsAt = inputMarkers.get(inputMarkers.size() - 1);
            } catch (IndexOutOfBoundsException e) {
                throw new StudentFatalMistake("No output");
            }
            String output = "";
            for (int i = outputStartsAt; i < outputs.size(); i++) {
                String currentOutput = outputs.get(i);
                reporter.writeln(currentOutput);
                output = output + currentOutput+" ";
            }
            output = output.toLowerCase();
            InputStream is = new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8));
            Scanner scan = new Scanner(is);

            TaxReport dueTax = getExpectedReport();


            boolean done = false;
            while (!done) {
                // try string
                String word;
                if (scan.hasNext()) {
                    word = scan.next();
                    try {
                        int integer = Integer.parseInt(word);
                        if (integer == dependents) {
                            missingDependent = false;
                        }
                    } catch (NumberFormatException e) {
                    }

                    try {
                        String tempword = word.replace(",", "");
                        double db = Double.parseDouble(tempword);
                        if (db == dueTax.getAGI()) {
                            missingIncome = false;
                        }
                        if (db == dueTax.getTax()) {
                            missingTax = false;
                        }
                    } catch (NumberFormatException e) {
                    }

                    if (single) {
                        if (word.equals("single")) {
                            missingStatus = false;
                        }
                    }
                    if (!single && word.equals("married")) {
                        missingStatus = false;
                    }
                } else {
                    done = true;
                }
            }

            mistakes += missingDependent ? 1 : 0;
            mistakes += missingIncome ? 1 : 0;
            mistakes += missingStatus ? 1 : 0;
            mistakes += missingTax ? 1 : 0;

            reporter.divider("MISTAKES", "" + mistakes);
            reporter.divider("correct dependent " + dueTax.dependents, "" + mistakes);
            reporter.divider("correct income " + dueTax.AGI, "" + mistakes);
            reporter.divider("correct single " + dueTax.single, "" + mistakes);
            reporter.divider("correct tax " + dueTax.getTax(), "" + mistakes);

            reporter.divider("missing dependent " + missingDependent, "" + mistakes);
            reporter.divider("missing income " + missingIncome, "" + mistakes);
            reporter.divider("missing status " + missingStatus, "" + mistakes);
            reporter.divider("missing tax " + missingTax, "" + mistakes);

//        if (mistakes>0) {
//            judgeReport(inputs, outputs,  inputMarkers, reporter);
//        }
            return mistakes;
        } else {
            // grade drunk output
            int outputStartsAt;
            try {
                outputStartsAt = inputMarkers.get(inputMarkers.size() - 1);
            } catch (IndexOutOfBoundsException e) {
                throw new StudentFatalMistake("No output");
            }
            String output = "";
            for (int i = outputStartsAt; i < outputs.size(); i++) {
                String currentOutput = outputs.get(i);
                if(currentOutput!=""){
                    if (currentOutput.charAt(currentOutput.length()-1)=='\n'){
                        reporter.write(currentOutput);
                    }else{
                        reporter.write(currentOutput);
                    }
                    output = output + currentOutput+ " " ;
                }
            }
            output = output.toLowerCase();
            if (output.contains("invalid")) {
                reporter.divider("invalid input captured");
                return 0;
            } else {
                reporter.divider("invalid input failed", "1" );
                return 1;
            }
        }
    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                        Reporter reporter) throws StudentFatalMistake {
        return judgeReport(customInputs, outputs, inputMarkers, reporter);
    }


}
