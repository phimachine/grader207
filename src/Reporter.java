import java.io.*;

import org.apache.commons.lang.exception.ExceptionUtils;

public class Reporter {
    // Reporter writes reports.
    // Grader does not grade the program itself.
    // One reporter per student

    private String studentID;
    private int excaLength = 20;
    private BufferedWriter reportWriter;
    private File reportPathFile;
    private File reportFile;

    public Reporter(String studentID, File reportPathFile) throws IOException {
        this.studentID = studentID;
        reportFile = new File(reportPathFile, this.studentID + "_report.txt");
        this.reportPathFile = reportPathFile;
        reportWriter = new BufferedWriter(new FileWriter(reportFile));
    }


    public void newStudentReport(String currentStudentID) throws IOException {

    }

    public void divider(String message) {
        int leftLength = excaLength - message.length() / 2;
        int rightLength = excaLength - (message.length() - message.length() / 2);

        String line = "Grader" + "!".repeat(leftLength) + message + "!".repeat(rightLength) + "Grader\n";

        try {
            reportWriter.write(line);
            reportWriter.flush();
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }
    }

    public void writeln(String line) {
        try {
            reportWriter.write(studentID + "|||" + line + "\n");
            reportWriter.flush();
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }
    }

    public void write(String line) {
        try {
            reportWriter.write(studentID + "|||" + line);
            reportWriter.flush();
        } catch (IOException e) {
            System.out.println("Report writer cannot write. Grader's fault.");
            e.printStackTrace();
            System.exit(-88);
        }

    }

    void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    String getStudentID() {
        return studentID;
    }

    public void reportException(Exception e, String graderMessage) {
        System.out.println(graderMessage);
        write(graderMessage);
        e.printStackTrace();
        write(ExceptionUtils.getStackTrace(e));
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
