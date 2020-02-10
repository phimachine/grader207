import java.io.IOException;

public class GradingInterfaceA2 {

    public static void main(String[] args) throws IOException {
        GradingInterfaceA2.autoGrade();
    }

    public static void autoGrade() {
        Grader grader = new Grader(true, true);
        ArithInteractor fi= new ArithInteractor();
        RequiredInputOutput rio = new RequiredInputOutput(null, fi, fi);
        grader.addRunRequirement(rio);
        grader.startGrading();
    }

    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startManualRegrade();
    }

}

