import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RequiredInputOutput {
    // this object has a one-to-one correspondence with a process
    // so two of these for one file, one for compilation and one for runtime

    final ArrayList<String> customInputs;
    final Judge judge;
    final Interactor interactor;
    final boolean interactive;

    public RequiredInputOutput(ArrayList<String> customInputs) {
        this(customInputs, null);
    }

    public RequiredInputOutput(ArrayList<String> customInputs, Judge judge) {
        this(customInputs, judge, null);
    }

    public RequiredInputOutput(ArrayList<String> customInputs, Judge judge, Interactor interactor) {
        this.customInputs = customInputs;
        this.judge = judge;
        this.interactor = interactor;
        this.interactive = customInputs == null ? true : false;
    }

//    public String timeOutRead(Process pro, Reporter reporter, ArrayList<String> outputs, BufferedReader stdout){
//        String line="";
//        String eof="false";
//        String ret[];
//        while (stdout.ready())
//        TimeoutReader tr=new TimeoutReader();
//        InputStream is=pro.getInputStream();
//        Scanner scan =  new Scanner(is);
//        ret = tr.read(scan);
//        line=ret[0];
//        eof=ret[1];
//        reporter.writeln(line);
//        outputs.add(line);
//    }

    public void interactiveInject(Process pro, BufferedReader reader, BufferedWriter writer, Reporter reporter,
                                  ArrayList<String> outputs, ArrayList<Integer> inputMarkers) throws StudentFatalMistake {
        try {

            String line;
            String eof;
            String ret[];
            TimeoutReader tr = new TimeoutReader();
//            Scanner scan =  new Scanner(reader);
            ret = tr.readUntilTimeout(reader);
            line = ret[0];
            eof = ret[1];
//            reporter.writeln(line);
//            outputs.add(line);
            try {
//                Thread.sleep(100);
                while (line != null || !(eof.equals("true"))) {
                    if (line.equals("") || line.charAt(line.length()-1)=='\n'){
                        reporter.write(line);
                    }else{
                        reporter.writeln(line);
                    }
                    outputs.add(line);
                    String inject=null;
                    if (!eof.equals("true")) {
                        inject = interactor.interact(line);
                    }
                    if (inject != null) {
                        writer.write(inject);
                        reporter.write(inject);
                        inputMarkers.add(outputs.size());
                        // gotta remember to flush all buffered writer
                        try {
                            writer.flush();
                        } catch (IOException e) {
                            reporter.reportException(e, "Student program closes unexpectedly", true);
                            throw new StudentFatalMistake("Student program closes unexpectedly");
                        }
                    } else {
                        Thread.sleep(100);
                    }
                    // wait for the student program to compute.
                    // if reader reads first, we will have problem.

                    if (eof.equals("true")) {
                        break;
                    } else {
                        Thread.sleep(100);
                        ret = tr.readUntilTimeout(reader);
                        line = ret[0];
                        eof = ret[1];
                    }
                }
            } catch (IOException e) {
                reporter.reportException(e, "Grader cannot access stdout of the student program.");
                e.printStackTrace();
                throw e;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                reporter.divider("Program closed itself");
            }
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }
    }

    public void injectCustomInput(Process pro, BufferedReader reader, BufferedWriter writer, Reporter reporter,
                                  ArrayList<String> outputs, ArrayList<Integer> inputMarkers) {
        try {
            if (customInputs != null) {
                for (String input : customInputs) {
                    writer.write(input);
                    inputMarkers.add(outputs.size());
                    reporter.write(input);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        reporter.writeln(line);
                        outputs.add(line);
                    }
                }
                try {
                    writer.close();
                } catch (IOException e) {
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
        int mistakes = 0;
        try {
            mistakes = judge.judgment(customInputs, outputs, inputMarkers, currentReporter);
        } catch (StudentFatalMistake studentFatalMistake) {
            mistakes = 999;
        }
        return mistakes;
    }


    public void print() {
        System.out.println("Custom inputs");
        for (String input : customInputs) {
            System.out.println(input);
        }
    }

    public void prepare(File tempPathFile) throws IOException {
        if (interactive) {
            interactor.prepare(tempPathFile);
        }
    }
}



