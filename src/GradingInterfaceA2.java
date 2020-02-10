import java.io.IOException;

public class GradingInterfaceA2 {
    private static int testers=10;

    public static void main(String[] args) throws IOException {
        GradingInterfaceA2.autoGrade();
    }

    public static void autoGrade() {
        Grader grader = new Grader(true, true);

        for (int i = 0; i < testers; i++) {
            ArithInteractor fi= new ArithInteractor();
            RequiredInputOutput rio = new RequiredInputOutput(null, fi, fi);
            grader.addRunRequirement(rio);
        }

        grader.startGrading();
    }

    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startManualRegrade();
    }

}

