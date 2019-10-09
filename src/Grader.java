import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Grader {
    private final boolean auto;
    private final String graderPathString;
    private final String submissionsPathString;
    private File submissionsPathFile;
    private final String tempPathString;
    private File tempPathFile;
    private final String reportPathString;
    private File reportPathFile;
    private String requireClassName;

    ArrayList<File> javaFiles;
//    private File currentFile;
//    Reporter reporter;
    private RequiredInputOutput[] requirements;
    private RequiredInputOutput[] compilationRequirements;
    private RequiredInputOutput[] runRequirements;

    // TODO double check all streams and properly close them. A lot of files here. I don't want to make the mistake.

    public Grader() throws IOException {
        this(true);
    }

    public Grader(boolean auto) throws IOException {
        this(auto, null, "./submissions", "./temp", "./reports");
    }

    public Grader(boolean auto, String requireClassName, String submissionsPathString, String tempPathString, String reportPathString) throws IOException {
        this.auto = auto;
        this.requireClassName = requireClassName;
        this.submissionsPathString = submissionsPathString;
        this.tempPathString = tempPathString;
        this.reportPathString = reportPathString;
        this.graderPathString = System.getProperty("user.dir");
        this.javaFiles = new ArrayList<>();
        if (auto) {
            checkDirectoryStructure();
        }
    }

    public void checkDirectoryStructure() throws IOException {
        // makes sure the directory has submission files and temp directory
        submissionsPathFile = new File(submissionsPathString);
        tempPathFile = new File(tempPathString);
        reportPathFile = new File(reportPathString);
        if (tempPathFile.isDirectory()) {
            File[] tempFiles = tempPathFile.listFiles();
            if (tempFiles.length != 0) {
                // found temp files that should not be here
                Scanner stdin = new Scanner(System.in);
                System.out.println("Warning: Temp files found, e.g.: " + tempFiles[0].getName());
                System.out.println("Do you want to remove them? [y/N]");
                String answer = stdin.next();
                if (answer.toLowerCase().equals("y")) {
                    FileUtils.cleanDirectory(tempPathFile);
                    System.out.println("Temp files removed");
                } else {
                    System.out.println("No files removed. Program terminating. temp directory needs to be clean");
                    System.exit(1);
                }
            }
        } else {
            tempPathFile.mkdir();
            System.out.println("New temp directory created.");
        }
        // submissions directory
        if (submissionsPathFile.isDirectory()) {
            // at least one java file should exist
            // report how many files there are
            File[] files = submissionsPathFile.listFiles();
            System.out.println("There are " + files.length + " files");
            for (File file : files
            ) {
                String ext = FilenameUtils.getExtension(file.getName());
                if (ext.equals("java")) {
                    javaFiles.add(file);
                }
            }
            System.out.println("There are " + javaFiles.size() + " java files");
            if (javaFiles.size() != files.length) {
                System.out.println("WARNING: " + (files.length - javaFiles.size()) + " files are not .java files");
                System.out.println("I don't know how to deal with this. I quit.");
                throw new FileNotFoundException("Non java files found");
            }
        } else {
            System.out.println("./submissions folder does not exist. Move it into root parallel to src.");
            System.exit(-1);
        }
        // reports directory
        if (reportPathFile.isDirectory()) {

        } else {
            reportPathFile.mkdir();
            System.out.println("New reports directory created.");
        }
    }



    public void startGrading() throws Exception {
        // The main program
        System.out.println("Grading Begins");
        if (this.requireClassName == null) {
            inferClassName();
        }
        if (javaFiles.size() == 0) {
            System.out.println("There are no java files?");
        }
        if (requirements != null) {
            System.out.println("The custom inputs are");
            for (RequiredInputOutput requirement : requirements
            ) {
                requirement.print();
            }
        } else {
            System.out.println("No inputs set");
        }

        for (File file : javaFiles
        ) {
            System.out.println("Grading " + file.getName());
            gradeStudent(file);
        }
        System.out.println("Grading finished");
        System.exit(0);
    }

    private void gradeStudent(File currentFile) throws Exception {
        File tempFile = copySubmissionTemp(currentFile);
        String studentID = parseStudentID(currentFile);
        Reporter reporter = new Reporter(studentID, reportPathFile);

//        // report writer
//        File reportFile = new File(reportPathFile, studentID+"_report.txt");
//        BufferedWriter reportWriter=new BufferedWriter(new FileWriter(reportFile));
//        String printprepend=studentID+"||   ";

        /////////////// CLASS NAME CHECK //////////////
        if (parseClassName(currentFile) != requireClassName) {
            reporter.divider("WRONG CLASS NAME");
        }
        //////////////// COMPILATION //////////////////
//        String compilationCommand = javaCommand +studentJavaFile.getAbsolutePath();
        // start the process
        ProcessBuilder builder = new ProcessBuilder("javac", tempFile.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process compilation = builder.start();
        // report the output
        gradeCompilation(compilation, reporter);
//        reportWriter.write("Grader*******************COMPILATION******************Grader\n");
//        reportWriter.write("Grader***********************stdin************************Grader\n");
//        writeToReport(printprepend + " stdout:", compilation.getInputStream(), reportWriter);
//        reportWriter.write("Grader***********************stderr***********************Grader\n");
//        writeToReport(printprepend + " stderr:", compilation.getErrorStream(), reportWriter);
//        // we output to the stdin of the process
//        reportWriter.write("Grader********************Exit value**********************Grader\n");
//        System.out.println(printprepend + "program exited with: " + compilation.exitValue());
//        reportWriter.flush();

        //////////////////////// IO /////////////////////////
        // start the process
//        String runCommand = javaCommand +studentJavaFile.getAbsolutePath();
        ProcessBuilder builder2 = new ProcessBuilder("java", parseClassName(currentFile));
        builder2.redirectErrorStream(true);
        Process run = builder2.start();
        // report the output
        gradeRun(run, reporter);
//        reportWriter.write("Grader*******************IO******************Grader\n");
//
//        reportWriter.write("Grader***********************stdin************************Grader\n");
//        run.waitFor();
//        writeToReport(printprepend + " stdout:", run.getInputStream(), reportWriter);
//        run.waitFor();
//        reportWriter.write("Grader***********************stderr***********************Grader\n");
//        writeToReport(printprepend + " stderr:", run.getErrorStream(), reportWriter);
//
//
//        // we output to the stdin of the process
//        run.waitFor();
//        OutputStream stdin = run.getOutputStream();
//        injectCustomInput(stdin);
//        run.waitFor();
//        reportWriter.write("Grader********************Exit value**********************Grader\n");
//        System.out.println(printprepend + " exitValue() " + run.exitValue());
//        reportWriter.flush();

        /////////////////////// WRAP UP //////////////////////
        // write the code itself, so I can take a look and do not have to open the file again
//        reportWriter.write("Grader********************Original code**********************Grader\n");
//        int lineNumber = 1;
//        BufferedReader studentReader = new BufferedReader(new FileReader(tempFile));
//        String line;
//        while ((line = studentReader.readLine()) != null) {
//            printprepend = lineNumber + "||   ";
//            reportWriter.write(printprepend + line + "\n");
//            lineNumber++;
//        }
//        reportWriter.write("Grader********************FINISHED**********************Grader\n");
//        reportWriter.write("Grader***********" + studentID + "****************Grader\n");
        reporter.close();
    }

    private void gradeCompilation(Process compilationProcess, Reporter reporter) throws IOException {
        reporter.divider("COMPILATION");

        for (RequiredInputOutput requirement : compilationRequirements) {
            try {
                gradeOneUnit(compilationProcess, requirement, reporter);
            } catch (InterruptedException e) {
                reporter.reportException(e, "Student program interrupted");
            }
        }
        reporter.divider("Exit value of compilation");
        reporter.divider("" + compilationProcess.exitValue());

//
//        reporter.divider("stdin");
//        // grader does not write most of the grading results.
//        writeToReport(printprepend + " stdout:", compilationProcess.getInputStream(), reportWriter);
//        reporter.divider("stderr");
//        writeToReport(printprepend + " stderr:", compilationProcess.getErrorStream(), reportWriter);
//        reporter.divider("Exit value");
//        System.out.println(printprepend + "program exited with: " + compilationProcess.exitValue());
//
//        reportWriter.write("Grader*******************COMPILATION******************Grader\n");
//        reportWriter.write("Grader***********************stdin************************Grader\n");
//        writeToReport(printprepend + " stdout:", compilationProcess.getInputStream(), reportWriter);
//        reportWriter.write("Grader***********************stderr***********************Grader\n");
//        writeToReport(printprepend + " stderr:", compilationProcess.getErrorStream(), reportWriter);
//        // we output to the stdin of the process
//        reportWriter.write("Grader********************Exit value**********************Grader\n");
//        System.out.println(printprepend + "program exited with: " + compilationProcess.exitValue());
//        reportWriter.flush();
    }

    private void gradeRun(Process runProcess, Reporter reporter) {
        reporter.divider("RUN");

        for (RequiredInputOutput requirement : runRequirements) {
            try {
                gradeOneUnit(runProcess, requirement, reporter);
            } catch (InterruptedException e) {
                reporter.reportException(e, "Student program interrupted");
            }
        }
        reporter.divider("Exit value of program");
        reporter.divider("" + runProcess.exitValue());
    }


    public void gradeOneUnit(Process pro, RequiredInputOutput requirement, Reporter reporter) throws InterruptedException {
        // one unit is defined by one requiredInputOutput object
        reporter.divider("one grading unit starts");

        // first input into the program
        reporter.divider("stdin");
        pro.waitFor();
        OutputStream stdin = pro.getOutputStream();
        requirement.injectCustomInput(stdin, reporter);
        pro.waitFor();

        // read what comes out
        reporter.divider("stdout");
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        pro.waitFor();
        ArrayList<String> outputs = new ArrayList<>();
        try {
            while ((line = in.readLine()) != null) {
                reporter.writeln(line);
            }
            // grade what comes out
            outputs.add(line);
            requirement.judge(outputs, reporter);
            stdin.close();
            in.close();
        } catch (IOException e) {
            reporter.reportException(e, "Grader cannot access streams of the student program. Grader's fault.");
            e.printStackTrace();
            System.exit(-87);
        }
        reporter.divider("one grading unit finished");
    }
//
//    private void injectCustomInput(OutputStream stdin) throws IOException {
//        if (customInputs!=null){
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
//            for (String input:customInputs){
//                writer.write(input);
//            }
//            writer.flush();
//            writer.close();
//        }
//    }

    private File copySubmissionTemp(File toBeCopied) throws IOException {
        // because there is only one file to be graded
        // so it returns, otherwise I don't know
        File copyTo = new File(tempPathFile, parseClassName(toBeCopied) + ".java");
        FileUtils.copyFile(toBeCopied, copyTo);
        return copyTo;
    }

    private void writeToReport(String printprepend, InputStream ins, BufferedWriter reportWriter) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            reportWriter.write(printprepend + " " + line + "\n");
        }
    }

    private String parseStudentID(File javaFile) {
        // anapusrujana_53652_8944872_Operations.java
        String fname = javaFile.getName();
        String[] parts = fname.split("_");
        String studentID = parts[0];

        return studentID;
    }

    private String parseClassName(File file) {
        // anapusrujana_53652_8944872_Operations.java
        String fname = file.getName();
        String[] parts = fname.split("_");
        String className = parts[parts.length - 1].split("\\.")[0];
        return className;
    }

    private String inferClassName() {
        File f1 = javaFiles.get(2);
        File f2 = javaFiles.get(4);
        String s1 = parseClassName(f1);
        if (parseClassName(f2).equals(s1)) {
            requireClassName = s1;
            return s1.split("\\.")[0];
        } else {
            throw new InvalidParameterException("What is the class name? I cannot infer it");
        }
    }
}