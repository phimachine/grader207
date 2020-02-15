import java.io.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

public class Reporter {
    // Reporter writes reports.
    // Grader does not grade the program itself.
    // One reporter per student

    private String studentID;
    private int excaLength = 30;
    private BufferedWriter reportWriter;
    private File reportPathFile;
    private File reportFile;
    boolean verbose;

    public Reporter(String studentID, File reportPathFile, boolean verbose) {
        this.studentID = studentID;
        reportFile = new File(reportPathFile, this.studentID + "_report.txt");
        this.reportPathFile = reportPathFile;
        try {
            reportWriter = new BufferedWriter(new FileWriter(reportFile));
        } catch (IOException e){
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-85);
        }
        this.verbose=verbose;
    }

    public void divider(String message) {
        divider(message, "#");
    }

    public void divider(String message, String marker) {
        int leftLength = excaLength - message.length() / 2;
        int rightLength = excaLength - (message.length() - message.length() / 2);

        String line = "Grader" + marker.repeat(leftLength) +"    "+ message +"    "+marker.repeat(rightLength) + "Grader";

        writeln(line);
//        if (verbose){
//            System.out.println(line);
//        }
    }
    public void writeln(String line) {
        write(line+"\n");
    }

    public void write(String line) {
        try {
            reportWriter.write(studentID + "|||" + line);
            reportWriter.flush();
            if (verbose){
                System.out.print(line);
            }
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }

    }
    public void newline(int n){
        try {
            reportWriter.write("\n".repeat(n));
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }
    }

    public void reportException(Exception e, String graderMessage, boolean silent) {
        if(!silent){
            System.out.println(graderMessage);
            e.printStackTrace();
        }
        writeln(graderMessage);
        writeln(ExceptionUtils.getStackTrace(e));
    }

    public void reportException(Exception e, String graderMessage) {
        boolean silent=false;
        if(!silent){
            System.out.println(graderMessage);
            e.printStackTrace();
        }
        writeln(graderMessage);
        writeln(ExceptionUtils.getStackTrace(e));
    }

    public void writeOriginalCode(File studentTempFile) {
        divider("Original code");
        int lineNumber = 1;
        try {
            BufferedReader studentReader = new BufferedReader(new FileReader(studentTempFile));
            String line;
            while ((line = studentReader.readLine()) != null) {
                String lineNumberString="" + lineNumber;
                writeln(lineNumberString+" ".repeat(3-lineNumberString.length()) + "|" + line);
                lineNumber++;
            }
            divider("FINISHED");
            divider(studentID);
            studentReader.close();
        } catch (FileNotFoundException e) {
            reportException(e, "Cannot write original code");
            System.exit(-89);
        } catch (IOException e) {
            reportException(e, "Cannot write original code");
            System.exit(-89);
        }
    }

    public void close(){
        try {
            reportWriter.close();
        } catch (IOException e) {
            reportException(e, "Cannot close reporter file stream");
            System.exit(-89);
        }
    }

    public void mark(String marker) {
        try{
            FileUtils.copyFile(reportFile,new File(reportPathFile, this.studentID + "_"+marker+".txt"));
        } catch (IOException e) {
            reportException(e, "Cannot access report directory");
            e.printStackTrace();
            System.exit(-89);
        }
        FileUtils.deleteQuietly(reportFile);


    }


}
