import java.io.IOException;
import java.util.ArrayList;

public class GradingInterface {
    public static void main(String[] args) throws Exception {
        Grader grader = new Grader();
        ArrayList<String> inputs = new ArrayList<>();
        grader.setCustomInputs(inputs);
        grader.startGrading();
    }
}
