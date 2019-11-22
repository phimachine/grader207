import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class GradingInterfaceA4 {

    public static void main(String[] args) throws IOException {
        GradingInterfaceA4.gradeManually();
    }

    public static void exampleAutoGrade() {
        // an example of how to use the grader program
        Grader grader = new Grader(true, true);
        grader.startFrom("reinierdavid");
//        grader.cnt=1;

        // single
        addTaxGradingBlock(grader, new TaxFiler(12,8,0));
        addTaxGradingBlock(grader, new TaxFiler(110,1,0));
        addTaxGradingBlock(grader, new TaxFiler(1102,1,0));
        addTaxGradingBlock(grader, new TaxFiler(11003,2,0));
        addTaxGradingBlock(grader, new TaxFiler(21020,0,0));
        addTaxGradingBlock(grader, new TaxFiler(70000,7,0));
        addTaxGradingBlock(grader, new TaxFiler(110030,7,0));
        addTaxGradingBlock(grader, new TaxFiler(110030,0,0));


        // married
        addTaxGradingBlock(grader, new TaxFiler(12,21552,8,0));
        addTaxGradingBlock(grader, new TaxFiler(14542,21552,5,0));
        addTaxGradingBlock(grader, new TaxFiler(15452,82250,0,0));
        addTaxGradingBlock(grader, new TaxFiler(12,5,8,0));
        addTaxGradingBlock(grader, new TaxFiler(150508,21552,0,0));
        addTaxGradingBlock(grader, new TaxFiler(1255,502,3,0));
        addTaxGradingBlock(grader, new TaxFiler(1772,215,4,0));
        addTaxGradingBlock(grader, new TaxFiler(120,215,3,0));
        addTaxGradingBlock(grader, new TaxFiler(120,2150505,2,0));

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
        grader.startFrom("willmanryan");
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