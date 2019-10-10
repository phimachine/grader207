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

    public void injectCustomInput(Process pro, OutputStream stdin, BufferedReader stdout, Reporter reporter,
                                  ArrayList<String> outputs, ArrayList<Integer> inputMarkers) {
        try {
            if (customInputs != null) {
                BufferedWriter stdinWriter = new BufferedWriter(new OutputStreamWriter(stdin));
                for (String input : customInputs) {
                    stdinWriter.write(input);
                    inputMarkers.add(outputs.size());
                    reporter.write(input);
                    try {
                        String line;
                        if (stdout.ready()){
                            while ((line = stdout.readLine()) != null) {
                                reporter.writeln(line);
                                outputs.add(line);
                            }
                        }
                    } catch (IOException e) {
                        reporter.reportException(e, "Grader cannot access stdout of the student program.");
                        e.printStackTrace();
                        throw e;
                    }
                }
                try {
                    stdinWriter.close();
                } catch (IOException e){
                    reporter.divider("Program closed itself");
                }
            }
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
//        } catch(InterruptedException e){
//            System.out.println("Report writer cannot write. Grader's fault.");
//            e.printStackTrace();
//            System.exit(-88);
        }
    }

    public int judge(ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter currentReporter) {
        // outputs are just outputs
        // inputMarkers, for example, [1,4,6] means there are one output before the first input, 4 outputs before the second...
        try{
            int mistakes=(int) judgment.invoke(GradingInterface.class, outputs, inputMarkers);
            if (mistakes!=0){
                System.out.println("here");
            }
            currentReporter.divider("MISTAKES",""+mistakes);
            return mistakes;
        } catch (IllegalAccessException e) {
            currentReporter.reportException(e, "The judgment function is not defined correctly.");
            return 999;
        } catch (InvocationTargetException e) {
            currentReporter.reportException(e, "The judgment function is not defined correctly.");
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



