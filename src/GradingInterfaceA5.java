import java.io.IOException;

public class GradingInterfaceA5 {
    // valid email grader

    public static void main(String[] args) throws IOException {
        GradingInterfaceA5.exampleAutoGrade();
//        GradingInterfaceA5.gradeManually();
    }


    public static void exampleAutoGrade() {
        // an example of how to use the grader program
        Grader grader = new Grader(true, true);
        grader.startFrom("saksonkevin");
        grader.setGradeHowMany(1);

        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(1));
        addEmailTester(grader, new EmailInteractor(2));
        addEmailTester(grader, new EmailInteractor(3));
        addEmailTester(grader, new EmailInteractor(4));
        addEmailTester(grader, new EmailInteractor(5));
        addEmailTester(grader, new EmailInteractor(6));

        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(1));
        addEmailTester(grader, new EmailInteractor(2));
        addEmailTester(grader, new EmailInteractor(3));
        addEmailTester(grader, new EmailInteractor(4));
        addEmailTester(grader, new EmailInteractor(5));
        addEmailTester(grader, new EmailInteractor(6));

        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(1));
        addEmailTester(grader, new EmailInteractor(2));
        addEmailTester(grader, new EmailInteractor(3));
        addEmailTester(grader, new EmailInteractor(4));
        addEmailTester(grader, new EmailInteractor(5));
        addEmailTester(grader, new EmailInteractor(6));

        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(0));
        addEmailTester(grader, new EmailInteractor(0));

        grader.startGrading();
    }

    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startFrom("vuenancy");
        grader.startManualRegrade();
    }

    public static void miniGrade() throws IOException {
        Grader grader = new Grader(true, true);
        grader.startManualRegrade();
    }


    public static void addEmailTester(Grader grader, EmailInteractor emailGuy){
//        Method judgment=taxer.getJudgment();
        RequiredInputOutput rio = new RequiredInputOutput(null, emailGuy, emailGuy);
        grader.addRunRequirement(rio);
    }

}
