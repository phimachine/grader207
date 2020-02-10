import java.io.IOException;
import java.util.ArrayList;

public class GradingInterfaceA1 {

    public static void main(String[] args) throws IOException {
        GradingInterfaceA1.autoGrade();
    }

    public static void autoGrade() {
        Grader grader = new Grader(true, true);
        EmptyUser eu= new EmptyUser();
        RequiredInputOutput rio = new RequiredInputOutput(null, eu, eu);
        grader.addRunRequirement(rio);
        grader.startGrading();
    }

    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startManualRegrade();
    }

}

