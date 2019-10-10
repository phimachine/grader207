import java.io.*;
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
    private boolean verbose;

    ArrayList<File> javaFiles;
    //    private File currentFile;
//    Reporter reporter;
    private ArrayList<RequiredInputOutput> compilationRequirements;
    private ArrayList<RequiredInputOutput> runRequirements;
    private ArrayList<File> manualGrade;

    public Grader() {
        this(true, false);
    }

    public Grader(boolean auto, boolean verbose) {
        this(auto, null, "./submissions", "./temp", "./reports", verbose);
    }

    public Grader(boolean auto, String requireClassName, String submissionsPathString,
                  String tempPathString, String reportPathString, boolean verbose)  {
        this.auto = auto;
        this.requireClassName = requireClassName;
        this.submissionsPathString = submissionsPathString;
        this.tempPathString = tempPathString;
        this.reportPathString = reportPathString;
        this.graderPathString = System.getProperty("user.dir");
        this.javaFiles = new ArrayList<>();
        this.runRequirements = new ArrayList<>();
        this.compilationRequirements = new ArrayList<>();
        this.verbose=verbose;
        this.manualGrade=new ArrayList<>();
        if (auto) {
            try {
                checkDirectoryStructure();
            } catch (IOException e) {
                System.out.println("Directory structure not correct. we need ./temp, ./report");
                e.printStackTrace();
                System.exit(-23);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GradingInterface g = new GradingInterface();
        g.manualRegrade();
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

    public void addCompilationRequirement(RequiredInputOutput rio){
        compilationRequirements.add(rio);
    }

    public void addRunRequirement(RequiredInputOutput rio){
        runRequirements.add(rio);
    }

    public void startGrading() {
        // The main program
        Scanner stdin = new Scanner(System.in);
        System.out.println("Grading Begins");
        if (this.requireClassName == null) {
            String guessedName=inferClassName();
            System.out.println("I'm guess that the correct class name is "+guessedName);
            System.out.println("Is it correct? [Y/n]");
            String answer=stdin.next();
            if (answer.toLowerCase().equals("n")){
                System.out.println("No changes made");
                System.exit(1);
            }
        }
        if (javaFiles.size() == 0) {
            System.out.println("There are no java files?");
        }

        for (File file : javaFiles
        ) {
            System.out.println("Grading " + file.getName());
            gradeStudent(file);
        }
        System.out.println("Auto grading finished finished");
        // currently not supporting manual grading.
//        if (manualGrade.size() != 0) {
//            System.out.println("There are some files that need manual inspections");
//            try {
//                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("manualGrade.arrayList"));
//                out.writeObject(manualGrade);
//                out.flush();
//                out.close();
//                ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream("manualGrade.arrayList.backup"));
//                out2.writeObject(manualGrade);
//                out2.flush();
//                out2.close();
//            } catch (FileNotFoundException e) {
//                System.out.println("Regrade array cannot be saved");
//                e.printStackTrace();
//            } catch (IOException e) {
//                System.out.println("Regrade array cannot be saved");
//                e.printStackTrace();
//            }
//            System.out.println("The regrade list has been saved to ./manualGrade.arrayList so that you can load them later.");
//            System.out.println("There are some files that need manual inspections");
//            System.out.println("Do you want to grade them right now? [Y/n]");
//            String answer=stdin.next();
//            if (!answer.toLowerCase().equals("n")){
//                startManualRegrade();
//            }
//        }
//        stdin.close();
//        System.exit(0);
    }

    private void gradeStudent(File currentFile){
        File tempFile = null;
        int mistakes=0;
        // copy files to temp to compile and run
        try {
            tempFile = copySubmissionTemp(currentFile);
        } catch (IOException e) {
            System.out.println("Grader cannot create temp file. Grader's fault");
            e.printStackTrace();
            System.exit(-85);
        }
        String studentID = parseStudentID(currentFile);
        Reporter reporter = new Reporter(studentID, reportPathFile, verbose);

//        // report writer
//        File reportFile = new File(reportPathFile, studentID+"_report.txt");
//        BufferedWriter reportWriter=new BufferedWriter(new FileWriter(reportFile));
//        String printprepend=studentID+"||   ";

        /////////////// CLASS NAME CHECK //////////////
        if (!parseClassName(currentFile).equals(requireClassName)) {
            reporter.divider("WRONG CLASS NAME", "!");
        }
        //////////////// Compilation //////////////////
//        String compilationCommand = javaCommand +studentJavaFile.getAbsolutePath();
        // start the process
        ProcessBuilder builder = new ProcessBuilder("javac", tempFile.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process compilation = null;
        try {
            compilation = builder.start();
        } catch (IOException e) {
            System.out.println("Grader cannot start the process. Grader's fault.");
            e.printStackTrace();
            System.exit(-42);
        }
        try {
            gradeCompilation(compilation, reporter);
            compilation.destroy();
        } catch (IOException e) {
            mistakes=999;
            reporter.write("Student "+studentID+" has IO exception. Need regrading");
            e.printStackTrace();
        } catch (InterruptedException e) {
            mistakes=999;
            reporter.write("Student "+studentID+" program is interrupted. Why?");
            e.printStackTrace();
        }
        reporter.newline(3);
        //////////////////////// Main program /////////////////////////
        // start the process
//        String runCommand = javaCommand +studentJavaFile.getAbsolutePath();
        for(RequiredInputOutput requirement: runRequirements){
            ProcessBuilder builder2 = new ProcessBuilder("java", parseClassName(currentFile));
            builder2.directory(tempPathFile);
            builder2.redirectErrorStream(true);
            Process run = null;
            try {
                run = builder2.start();
            } catch (IOException e) {
                mistakes=999;
                System.out.println("Grader cannot start the process. It might be grader's fault.");
                e.printStackTrace();
                System.exit(-42);
            }
            // report the output
            try{
                mistakes+=gradeRun(run, reporter, requirement);
                run.destroy();
            } catch (InterruptedException e) {
                mistakes=999;
                reporter.write("Student "+studentID+" has IO exception. Need regrading");
                e.printStackTrace();
            } catch (IOException e) {
                mistakes=999;
                reporter.write("Student "+studentID+" program is interrupted. Why?");
                e.printStackTrace();
            }
            reporter.newline(3);
        }
        // Get a copy of the original code
        reporter.writeOriginalCode(tempFile);
        reporter.close();

        // if no mistakes are made, change file name
        if (mistakes == 0) {
            reporter.mark("correct");
        }else if(mistakes>900){
            reporter.mark("fatal");
            manualGrade.add(currentFile);
        }else{
            reporter.mark("regrade");
        }
    }

    private void gradeCompilation(Process compilationProcess, Reporter reporter) throws IOException, InterruptedException {
        reporter.divider("COMPILATION");
        try {
            if (compilationRequirements != null) {
                for (RequiredInputOutput requirement : compilationRequirements) {
                    gradeOneUnit(compilationProcess, requirement, reporter);
                }
            } else {
                gradeOneUnit(compilationProcess, null, reporter);
            }
            compilationProcess.waitFor();
        } catch (InterruptedException |IOException e) {
            reporter.reportException(e, "Student program interrupted");
            throw e;
        }

        reporter.divider("Exit value compilation: " + compilationProcess.exitValue());
    }

    private int gradeRun(Process runProcess, Reporter reporter, RequiredInputOutput requirement) throws IOException, InterruptedException {
        int mistakes=0;
        if (runRequirements != null){
            try {
                mistakes += gradeOneUnit(runProcess, requirement, reporter);
            } catch (InterruptedException | IOException e) {
                reporter.reportException(e, "Student program interrupted");
                throw e;
            }
        }

        reporter.divider("Exit value run: " + runProcess.exitValue());
        return mistakes;
    }

    public int gradeOneUnit(Process pro, RequiredInputOutput requirement, Reporter reporter) throws InterruptedException, IOException {
        // one unit is defined by one requiredInputOutput object
        // this function does not handle any exception. it rethrows them.
        int unitMistakes=0;
        // first input into the program
        reporter.divider("stdin");
        OutputStream stdin = pro.getOutputStream();
        if (requirement.customInputs!=null){
            requirement.injectCustomInput(stdin, reporter);
        }
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
                outputs.add(line);
            }
            // grade what comes out
            if (requirement.judgment!=null){
                unitMistakes+=requirement.judge(outputs, reporter);
            }
            pro.waitFor();
            stdin.close();
            in.close();
        } catch (IOException e) {
            reporter.reportException(e, "Grader cannot access streams of the student program.");
            e.printStackTrace();
            throw e;
        }
        pro.waitFor();
        return unitMistakes;
    }

    public void startManualRegrade() {
        ArrayList<File> manualGrade = null;
        try {
            ObjectInputStream in = null;
            in = new ObjectInputStream(new FileInputStream("manualGrade.arrayList"));
            manualGrade=(ArrayList<File>) in.readObject();
            in.close();
            if (manualGrade.size()==0){
                System.out.println("Empty manual grade loaded. Abort. No changes made");
                System.exit(-99);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner stdin = new Scanner(System.in);

        Reporter reporter = new Reporter("regrade_log", reportPathFile, true);
        for (int i = 0; i < manualGrade.size(); i++) {
            File originalFile = manualGrade.get(i);
            String studentID = parseStudentID(originalFile);
            reporter.write(studentID);
            reporter.write(originalFile.getName());

            // compilation
            try {
                File tempFile = copySubmissionTemp(originalFile);
                // compile again
                ProcessBuilder builder = new ProcessBuilder("javac", tempFile.getAbsolutePath());
                builder.redirectErrorStream(true);
                Process compilation = null;
                compilation = builder.start();
                compilation.waitFor();
                compilation.destroy();
            } catch (IOException | InterruptedException e) {
                reporter.reportException(e, "Grader cannot compile.");
                e.printStackTrace();
            }
            cont(stdin, manualGrade, i);

            // run
            try {
                ProcessBuilder builder2 = new ProcessBuilder("java", parseClassName(originalFile));
                System.out.println("Running program");
                builder2.directory(tempPathFile);
                builder2.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                builder2.redirectError(ProcessBuilder.Redirect.INHERIT);
                builder2.redirectInput(ProcessBuilder.Redirect.INHERIT);
                Process p = builder2.start();
                p.waitFor();
                p.destroy();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            cont(stdin, manualGrade, i);
        }
    }

    private void cont(Scanner stdin, ArrayList<File> manualGrade, int index){
        System.out.println("Code? [c] Next? [n] Another run? [r] ");
        String ret=stdin.next().toLowerCase();
        if (!ret.equals("c")){
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("manualGrade.arrayList"));
                out.writeObject(manualGrade);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File copySubmissionTemp(File toBeCopied) throws IOException {
        // because there is only one file to be graded
        // so it returns, otherwise I don't know
        File copyTo = new File(tempPathFile, parseClassName(toBeCopied) + ".java");
        FileUtils.copyFile(toBeCopied, copyTo);
        return copyTo;
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
            System.out.println("What is the class name? I cannot infer it");
            System.exit(-86);
            return null;
        }
    }
}