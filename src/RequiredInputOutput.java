import java.io.*;
import java.util.ArrayList;

public class RequiredInputOutput {
    // this object has a one-to-one correspondence with a process
    // so two of these for one file, one for compilation and one for runtime

    Reporter reporter;
    Process process;
    ArrayList<String> customInputs;

    public RequiredInputOutput(Reporter reporter, Process process) {
        this.reporter = reporter;
    }


    public void gradeStderr(InputStream stderr) throws IOException {
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(stderr));
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            reportWriter.write(printprepend + " " + line + "\n");
        }
    }

    public void injectCustomInput(OutputStream stream, Reporter reporter) {
        try {
            if (customInputs != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
                for (String input : customInputs) {
                    writer.write(input);
                    reporter.write(input);
                }
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }
    }

    public void judge(ArrayList<String> outputs, Reporter currentReporter) {
    }
    
    public void setCustomInputs(ArrayList<String> customInputs) {
        this.customInputs = customInputs;
    }
}

public class Inputs {

}

public class Judgment {

}