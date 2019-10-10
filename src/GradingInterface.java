import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class GradingInterface {

    public static void main(String[] args) throws IOException {
        GradingInterface.manualRegrade();
    }

    public static void exampleGrade(String[] args) {
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

        grader.startGrading();
    }

    public static void manualRegrade() throws IOException {
        Grader grader = new Grader(true, true);
        grader.startManualRegrade();
    }

    public static Method getMethod(String s){
        try{
            return GradingInterface.class.getMethod(s, ArrayList.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("No such method in grader interface judgments, grader fault");
            System.exit(-84);
            return null;
        }
    }

    public static int judgment1(ArrayList<String> outputs){
        // write sufficient condition for a correct assignment
        int mistakes= outputs.size()==4? 0:1;
        mistakes += (outputs.get(0).contains("9")? 0:1);
        mistakes += (outputs.get(1).contains("24")? 0:1);
        mistakes += (outputs.get(2).contains("4")? 0:1);
        mistakes += (outputs.get(3).contains("2")? 0:1);

        return mistakes;
    }

    public static int judgment2(ArrayList<String> outputs){
        int mistakes= outputs.size()==4? 0:1;
        mistakes += (outputs.get(0).contains("-9")? 0:1);
        mistakes += (outputs.get(1).contains("-24")? 0:1);
        mistakes += (outputs.get(2).contains("-2")? 0:1);
        mistakes += (outputs.get(3).contains("-4")? 0:1);
        return mistakes;
    }

    public static int judgment3(ArrayList<String> outputs){
        int mistakes= outputs.size()==4? 0:1;
        mistakes += (outputs.get(0).contains("1")? 0:1);
        mistakes += (outputs.get(1).contains("0")? 0:1);
        mistakes += (outputs.get(2).contains("1")? 0:1);
        mistakes += (outputs.get(3).contains("0")? 0:1);
        return mistakes;
    }
}