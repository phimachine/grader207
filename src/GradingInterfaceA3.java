import java.io.IOException;
import java.util.Scanner;

public class GradingInterfaceA3 {
    private static int testers=20;

    public static void main(String[] args) throws IOException {
        GradingInterfaceA3.autoGrade();
    }

    public static void autoGrade() {
        Grader grader = new Grader(true, true);
        grader.startFrom("bertramjackson");
        for (int i = 0; i < testers; i++) {
            LeapYearInteractor li= new LeapYearInteractor(i%4);
            RequiredInputOutput rio = new RequiredInputOutput(null, li, li);
            grader.addRunRequirement(rio);
        }

        grader.startGrading();

        String status;
        Scanner scan = new Scanner(System.in);
        status=scan.next();
        if (!status.equals("single") && !status.equals("married")){
            System.out.println("Invalid input");
            return;
        }

        // if this line is reachable, then status has to be single or married
        System.out.println(status);
    }

    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startManualRegrade();
    }

}

