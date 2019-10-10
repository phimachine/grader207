import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class GradingInterface {

    public static void main(String[] args) throws IOException {
        GradingInterface.exampleAutoGrade();
    }

    public static void gradeManually(){
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startManualRegrade();
    }

    public static void exampleAutoGrade() {
        // an example of how to use the grader program
        Grader grader = new Grader(true, true);

        ArrayList<String> inputs1 = new ArrayList<>();
        inputs1.add("2\n");
        inputs1.add("3\n");
        inputs1.add("4\n");
        RequiredInputOutput rio1 = new RequiredInputOutput(inputs1, getMethod("judgment1"));
        grader.addRunRequirement(rio1);

        ArrayList<String> inputs2 = new ArrayList<>();
        inputs2.add("-2\n");
        inputs2.add("-3\n");
        inputs2.add("-4\n");
        RequiredInputOutput rio2 = new RequiredInputOutput(inputs2 , getMethod("judgment2"));
        grader.addRunRequirement(rio2);

        ArrayList<String> inputs3 = new ArrayList<>();
        inputs3.add("0\n");
        inputs3.add("0\n");
        inputs3.add("1\n");
        RequiredInputOutput rio3 = new RequiredInputOutput(inputs3, getMethod("judgment3"));
        grader.addRunRequirement(rio3);

        ArrayList<String> inputs4 = new ArrayList<>();
        inputs3.add("-2\n");
        inputs3.add("3\n");
        inputs3.add("-1000\n");
        RequiredInputOutput rio4 = new RequiredInputOutput(inputs3, getMethod("judgment4"));
        grader.addRunRequirement(rio3);

        grader.startGrading();
    }

    public static void miniGrade() throws IOException {
        Grader grader = new Grader(true, true);
        grader.startManualRegrade();
    }

    public static Method getMethod(String s){
        try{
            return GradingInterface.class.getMethod(s, ArrayList.class, ArrayList.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("No such method in grader interface judgments, grader fault");
            System.exit(-84);
            return null;
        }
    }

    public static int judgment1(ArrayList<String> outputs, ArrayList<Integer> inputMarkers){
        // write sufficient condition for a correct assignment
        int mistakes=0;
        int outputStartsAt=inputMarkers.get(inputMarkers.size()-1);
        boolean hasEnter=false;
        for (String s:outputs.subList(0, outputStartsAt+1)){
            if (s.toLowerCase().contains("enter")){
                hasEnter=true;
            }
        }
        mistakes += hasEnter? 0: 1;
        mistakes += (outputs.get(outputStartsAt+0).contains("9")? 0:1);
        mistakes += (outputs.get(outputStartsAt+1).contains("24")? 0:1);
        mistakes += (outputs.get(outputStartsAt+2).contains("4")? 0:1);
        mistakes += (outputs.get(outputStartsAt+3).contains("2")? 0:1);

        return mistakes;
    }

    public static int judgment2(ArrayList<String> outputs, ArrayList<Integer> inputMarkers){
        int mistakes=0;
        int outputStartsAt=inputMarkers.get(inputMarkers.size()-1);
        boolean hasEnter=false;
        for (String s:outputs.subList(0, outputStartsAt+1)){
            if (s.toLowerCase().contains("enter")){
                hasEnter=true;
            }
        }
        mistakes += hasEnter? 0: 1;
        mistakes += (outputs.get(outputStartsAt+0).contains("-9")? 0:1);
        mistakes += (outputs.get(outputStartsAt+1).contains("-24")? 0:1);
        mistakes += (outputs.get(outputStartsAt+2).contains("-2")? 0:1);
        mistakes += (outputs.get(outputStartsAt+3).contains("-4")? 0:1);

        return mistakes;
    }

    public static int judgment3(ArrayList<String> outputs, ArrayList<Integer> inputMarkers){
        int mistakes=0;
        int outputStartsAt=inputMarkers.get(inputMarkers.size()-1);
        boolean hasEnter=false;
        for (String s:outputs.subList(0, outputStartsAt+1)){
            if (s.toLowerCase().contains("enter")){
                hasEnter=true;
            }
        }
        mistakes += hasEnter? 0: 1;
        mistakes += (outputs.get(outputStartsAt+0).contains("1")? 0:1);
        mistakes += (outputs.get(outputStartsAt+1).contains("0")? 0:1);
        mistakes += (outputs.get(outputStartsAt+2).contains("1")? 0:1);
        mistakes += (outputs.get(outputStartsAt+3).contains("0")? 0:1);

        return mistakes;
    }

    public static int judgment4(ArrayList<String> outputs, ArrayList<Integer> inputMarkers){
        int mistakes=0;
        int outputStartsAt=inputMarkers.get(inputMarkers.size()-1);
        boolean hasEnter=false;
        for (String s:outputs.subList(0, outputStartsAt+1)){
            if (s.toLowerCase().contains("enter")){
                hasEnter=true;
            }
        }
        mistakes += hasEnter? 0: 1;
        mistakes += (outputs.get(outputStartsAt+0).contains("-999")? 0:1);
        mistakes += (outputs.get(outputStartsAt+1).contains("6000")? 0:1);
        mistakes += (outputs.get(outputStartsAt+2).contains("3")? 0:1);
        mistakes += (outputs.get(outputStartsAt+3).contains("-1000")? 0:1);

        return mistakes;
    }
}