import java.io.IOException;

public class GradingInterfaceA6 {
    // valid email grader

    public static void main(String[] args) throws IOException {
        GradingInterfaceA6.exampleAutoGrade();
//        GradingInterfaceA5.gradeManually();
    }

    public static void exampleAutoGrade() {
        // an example of how to use the grader program
        Grader grader = new Grader(true, true);
        grader.startFrom("rileydaniel" );
        grader.cnt=1;
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);
        addGrading(grader);


        grader.startGrading();
    }


    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startFrom("idrismuhammad");
        grader.startManualRegrade();
    }

    public static void miniGrade() throws IOException {
        Grader grader = new Grader(true, true);
        grader.startManualRegrade();
    }

    public static void addGrading(Grader grader) {
        NewArrayGrader ag = new NewArrayGrader();
        RequiredInputOutput rio = new RequiredInputOutput(null, ag, ag);
        grader.addRunRequirement(rio);
    }
}
