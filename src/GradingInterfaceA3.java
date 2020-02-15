import java.io.IOException;

public class GradingInterfaceA3 {
    private static int testers=20;

    public static void main(String[] args) throws IOException {
        GradingInterfaceA3.autoGrade();
    }

    public static void autoGrade() {
        Grader grader = new Grader(true, true);

        for (int i = 0; i < testers; i++) {
            LeapYearInteractor li= new LeapYearInteractor(i%4);
            RequiredInputOutput rio = new RequiredInputOutput(null, li, li);
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

