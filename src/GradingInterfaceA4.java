import java.io.IOException;

public class GradingInterfaceA4 {

    public static void main(String[] args) throws IOException {
        GradingInterfaceA4.exampleAutoGrade();
//        GradingInterfaceA4.gradeManually();
//        GradingInterfaceA4.gradeManually();
    }

    public static void exampleAutoGrade() {
        // an example of how to use the grader program
        Grader grader = new Grader(true, true);
        grader.startFrom("saksonkevin");
        grader.setGradeHowMany(1);
//        grader.cnt=1;

        // single
        addTaxGradingBlock(grader, new TaxFiler(12,2,0));
        addTaxGradingBlock(grader, new TaxFiler(110,1,0));
        addTaxGradingBlock(grader, new TaxFiler(1102,1,0));
        addTaxGradingBlock(grader, new TaxFiler(11003,2,0));
        addTaxGradingBlock(grader, new TaxFiler(21020,1,0));
        addTaxGradingBlock(grader, new TaxFiler(70000,2,0));
        addTaxGradingBlock(grader, new TaxFiler(70000,5,0));
        addTaxGradingBlock(grader, new TaxFiler(110030,7,0));
        addTaxGradingBlock(grader, new TaxFiler(110030,0,0));
        addTaxGradingBlock(grader, new TaxFiler(14542,5,0));
        addTaxGradingBlock(grader, new TaxFiler(21450,6,0));
        addTaxGradingBlock(grader, new TaxFiler(51900,4,0));


        // married
        addTaxGradingBlock(grader, new TaxFiler(12,120,2,0));
        addTaxGradingBlock(grader, new TaxFiler(6,12000,1,0));
        addTaxGradingBlock(grader, new TaxFiler(0,35000,2,0));
        addTaxGradingBlock(grader, new TaxFiler(0,80000,3,0));
        addTaxGradingBlock(grader, new TaxFiler(6,86500,4,0));
        addTaxGradingBlock(grader, new TaxFiler(6,86500,5,0));
        addTaxGradingBlock(grader, new TaxFiler(6,86500,6,0));
        addTaxGradingBlock(grader, new TaxFiler(15452,82250,5,0));
        addTaxGradingBlock(grader, new TaxFiler(12,5,8,0));
        addTaxGradingBlock(grader, new TaxFiler(150508,21552,0,0));
        addTaxGradingBlock(grader, new TaxFiler(1255,502,3,0));
        addTaxGradingBlock(grader, new TaxFiler(1772,215,4,0));
        addTaxGradingBlock(grader, new TaxFiler(120,215,3,0));
        addTaxGradingBlock(grader, new TaxFiler(120,2150505,4,0));

        //invalid inputs

        // single
        addTaxGradingBlock(grader, new TaxFiler(12,8,1));
        addTaxGradingBlock(grader, new TaxFiler(110,1,2));
        addTaxGradingBlock(grader, new TaxFiler(70000,7,1));
        addTaxGradingBlock(grader, new TaxFiler(110030,7,2));
        addTaxGradingBlock(grader, new TaxFiler(110030,0,1));
        addTaxGradingBlock(grader, new TaxFiler(1102,1,2));


        // married
        addTaxGradingBlock(grader, new TaxFiler(12,21552,8,1));
        addTaxGradingBlock(grader, new TaxFiler(14542,21552,5,2));
        addTaxGradingBlock(grader, new TaxFiler(15452,82250,0,1));
        addTaxGradingBlock(grader, new TaxFiler(12,5,8,1));
        addTaxGradingBlock(grader, new TaxFiler(150508,21552,0,2));
        addTaxGradingBlock(grader, new TaxFiler(1255,502,3,1));
        addTaxGradingBlock(grader, new TaxFiler(1772,215,4,2));

        grader.startGrading();
    }


    public static void gradeManually() {
        // this grades the files that are not autograded.
        Grader grader = new Grader();
        grader.startFrom("zhoubinghong");
        grader.startManualRegrade();
    }

    public static void miniGrade() throws IOException {
        Grader grader = new Grader(true, true);
        grader.startManualRegrade();
    }


    public static void addTaxGradingBlock(Grader grader, TaxFiler taxer){
//        Method judgment=taxer.getJudgment();
        RequiredInputOutput rio = new RequiredInputOutput(null, taxer, taxer);
        grader.addRunRequirement(rio);
    }
}