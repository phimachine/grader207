import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GradingInterfaceWS9 {

    public static void main(String[] args) throws IOException {
        GradingInterfaceWS9.exampleAutoGrade();
    }

    public static void exampleAutoGrade() {
        // an example of how to use the grader program
        Grader grader = new Grader(true, true);
//        grader.startFrom("anapusrujana");
        addGrading(grader,false);
        addGrading(grader, false);
        addGrading(grader, false);
        addGrading(grader, true);
        addGrading(grader, true);
        grader.startGrading();
    }


    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startFrom("willmanryan");
        grader.startManualRegrade();
    }

    public static void miniGrade() throws IOException {
        Grader grader = new Grader(true, true);
        grader.startManualRegrade();
    }

    public static void addGrading(Grader grader, boolean jibberish){
        ArrayGrader ag= new ArrayGrader(jibberish);
        RequiredInputOutput rio = new RequiredInputOutput(null, ag, ag);
        grader.addRunRequirement(rio);
    }
}