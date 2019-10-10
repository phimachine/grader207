import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RequiredInputOutput {
    // this object has a one-to-one correspondence with a process
    // so two of these for one file, one for compilation and one for runtime

    final ArrayList<String> customInputs;
    final Method judgment;

    public RequiredInputOutput(ArrayList<String> customInputs) {
        this(customInputs, null);
    }

    public RequiredInputOutput(ArrayList<String> customInputs, Method judgment) {
        this.customInputs=customInputs;
        this.judgment=judgment;
    }

    public void injectCustomInput(OutputStream stream, Reporter reporter) {
        try {
            if (customInputs != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
                for (String input : customInputs) {
                    writer.write(input);
                    reporter.write(input);
                }
                try {
                    writer.close();
                } catch (IOException e){
                    reporter.divider("Program closed itself");
                }
            }
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }
    }

    public int judge(ArrayList<String> outputs, Reporter currentReporter) {
        try{
            int mistakes=(int) judgment.invoke(GradingInterface.class, outputs);
            if (mistakes!=0){
                System.out.println("here");
            }
            currentReporter.divider("MISTAKES",""+mistakes);
            return mistakes;
        } catch (IllegalAccessException e) {
            currentReporter.reportException(e, "Grader's judgment function experienced error");
            return 999;
        } catch (InvocationTargetException e) {
            currentReporter.reportException(e, "Grader's judgment function experienced error");
            return 999;
        }
    }


    public void print() {
        System.out.println("Custom inputs");
        for(String input: customInputs){
            System.out.println(input);
        }
    }
}



