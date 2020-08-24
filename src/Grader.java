import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
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
    private boolean interactive=true;
    ArrayList<File> javaFiles;
    //    private File currentFile;
//    Reporter reporter;
    private ArrayList<RequiredInputOutput> compilationRequirements;
    private ArrayList<RequiredInputOutput> runRequirements;
    private ArrayList<File> manualGrade;
    private String startFromStudentID;
    public int cnt=9999;

    public Grader() {
        this(true, false);
    }

    public Grader(boolean auto, boolean verbose) {
        this(auto, null, "./submissions", "./temp", "./reports", verbose);
    }

    public Grader(boolean auto, String requireClassName, String submissionsPathString,
                  String tempPathString, String reportPathString, boolean verbose) {
        System.out.println("Grader help you construct test cases, find compilation problem, etc..");
        System.out.println("But it cannot do everything: e.g. give feedback on coding styles or redundant logic, etc..");
        System.out.println("It's necessary to check for all programs that do not have full score");

        this.auto = auto;
        this.requireClassName = requireClassName;
        this.submissionsPathString = submissionsPathString;
        this.tempPathString = tempPathString;
        this.reportPathString = reportPathString;
        this.graderPathString = System.getProperty("user.dir");
        this.javaFiles = new ArrayList<>();
        this.runRequirements = new ArrayList<>();
        this.compilationRequirements = new ArrayList<>();
        this.verbose = verbose;
        this.manualGrade = new ArrayList<>();
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
                String answer="";
                if (!auto){
                    answer = stdin.next();
                }
                if (auto||answer.toLowerCase().equals("y")) {
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


    public void addCompilationRequirement(RequiredInputOutput rio) {
        compilationRequirements.add(rio);
    }

    public void addRunRequirement(RequiredInputOutput rio) {
        runRequirements.add(rio);
    }

    public void startGrading() {
        // The main program
        Scanner stdin = new Scanner(System.in);
        System.out.println("Grading Begins");
        if (this.requireClassName == null) {
            String guessedName = inferClassName();
            System.out.println("I'm guess that the correct class name is " + guessedName);
            if (!auto){
                System.out.println("Is it correct? [Y/n]");
                String answer = stdin.next();
                if (answer.toLowerCase().equals("n")) {
                    System.out.println("No changes made");
                    System.exit(1);
                }
            }
        }
        if (javaFiles.size() == 0) {
            System.out.println("There are no java files?");
        }

        for (File file : javaFiles
        ) {
            System.out.println("Grading " + file.getName());
            if (cnt>0){
                gradeStudent(file);
            }
        }
        System.out.println("Auto grading finished");
    }

    public void startFrom(String studentID){
        this.startFromStudentID =studentID;
    }

    public void setGradeHowMany(int remaining){
        this.cnt=remaining;
    }


    private void gradeStudent(File currentFile)  {
        File tempFile = null;
        int mistakes = 0;

        try {

            FileUtils.cleanDirectory(tempPathFile);
            // copy files to temp to compile and run
            tempFile = copySubmissionTemp(currentFile);
        } catch (IOException e) {
            System.out.println("Grader cannot create temp file. Grader's fault");
            e.printStackTrace();
            System.exit(-85);
        }
        String studentID = parseStudentID(currentFile);
        if (this.startFromStudentID !=null){
            if (studentID.equals(this.startFromStudentID)){
                this.startFromStudentID =null;
            }else{
                return;
            }
        }
        cnt-=1;
        Reporter reporter = new Reporter(studentID, reportPathFile, verbose);

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
        } catch (InterruptedException e) {
            mistakes = 999;
            reporter.write("Student " + studentID + " program is interrupted. Why?");
            e.printStackTrace();
        }
        compilation.destroy();
        reporter.newline(3);
        //////////////////////// Main program /////////////////////////
        // start the process
//        String runCommand = javaCommand +studentJavaFile.getAbsolutePath();
        for (int i = 0; i < runRequirements.size(); i++) {
            RequiredInputOutput requirement = runRequirements.get(i);
            try {
                requirement.prepare(tempPathFile);
            } catch (IOException e) {
                mistakes = 999;
                System.out.println("Grader cannot write data files.");
                e.printStackTrace();
                System.exit(-41);
            }
            reporter.divider("New input set");
            ProcessBuilder builder2 = new ProcessBuilder("java", parseClassName(currentFile));
            builder2.directory(tempPathFile);
            builder2.redirectErrorStream(true);
//            builder2.redirectInput(ProcessBuilder.Redirect.PIPE);
//            builder2.redirectOutput(ProcessBuilder.Redirect.PIPE);

            Process pro = null;
            try {
                pro = builder2.start();
                OutputStream stdin = pro.getOutputStream(); // <- Eh?
                InputStream stdout = pro.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

                mistakes += gradeRun(pro, reader, writer, reporter, requirement);
                reader.close();
                writer.close();
                pro.destroy();
            }catch (NoSuchElementException e){
                System.out.println("What is happening?");
            } catch (IOException e) {
                mistakes = 999;
                System.out.println("Grader cannot start the process. It might be grader's fault.");
                e.printStackTrace();
                System.exit(-42);

            } catch (InterruptedException e) {
                mistakes = 999;
                reporter.write("Student " + studentID + " has IO exception. Need regrading");
                e.printStackTrace();
            }catch (StudentFatalMistake e) {
                mistakes = 999;
                reporter.write("Student " + studentID + " program is terminated. ");
                e.printStackTrace();
            }
            reporter.newline(3);
        }
        // Get a copy of the original code
        reporter.writeOriginalCode(tempFile);
        reporter.close();
        boolean deleted = tempFile.delete();
//        File here = new File(".");
//        System.out.println(here.getAbsolutePath());

        // if no mistakes are made, change file name
        if (mistakes == 0) {
            reporter.mark("correct");
        } else if (mistakes > 900) {
            reporter.mark("fatal");
            manualGrade.add(currentFile);
        } else {
            reporter.mark("" + mistakes);
        }
    }

    private void gradeCompilation(Process compilationProcess, Reporter reporter) throws InterruptedException {
        reporter.divider("COMPILATION");
//        try {
//            if (compilationRequirements != null) {
//                for (RequiredInputOutput requirement : compilationRequirements) {
//                    gradeOneUnit(compilationProcess, requirement, reporter);
//                }
//            } else {
//                gradeOneUnit(compilationProcess, null, reporter);
//            }
//            compilationProcess.waitFor();
//        } catch (InterruptedException | IOException e) {
//            reporter.reportException(e, "Student program interrupted");
//            throw e;
//        }
        compilationProcess.waitFor();
        reporter.divider("Exit value compilation: " + compilationProcess.exitValue());
    }

    private int gradeRun(Process runProcess, BufferedReader reader, BufferedWriter writer, Reporter reporter, RequiredInputOutput requirement) throws IOException, InterruptedException, StudentFatalMistake {
        int mistakes = 0;
        if (runRequirements != null) {
            try {
                if (interactive){
                    mistakes += gradeOneUnit(runProcess, reader, writer, requirement, reporter);
                }
            } catch (InterruptedException | IOException e) {
                reporter.reportException(e, "Student program interrupted");
                throw e;
            }
        }

        reporter.divider("Exit value run: " + runProcess.exitValue());
        return mistakes;
    }


    public int gradeOneUnit(Process pro,  BufferedReader reader, BufferedWriter writer, RequiredInputOutput requirement, Reporter reporter) throws InterruptedException, IOException, StudentFatalMistake {
        // one unit is defined by one requiredInputOutput object
        // this function does not handle any exception. it rethrows them.
        int unitMistakes = 0;
        ArrayList<Integer> inputMarkers = new ArrayList<>();

        // input into the program
        // first read what the program has to say
        // read what comes out
        String line;
        ArrayList<String> outputs = new ArrayList<>();

        if (requirement.customInputs != null) {
            reporter.divider("stdin");
            requirement.injectCustomInput(pro, reader, writer, reporter, outputs, inputMarkers);
            reporter.divider("stdout");
        }else {
            requirement.interactiveInject(pro, reader, writer, reporter, outputs, inputMarkers);
        }
        pro.waitFor();


        // read what comes out
        pro.waitFor();
        try {
            while ((line = reader.readLine()) != null) {
                reporter.writeln(line);
                outputs.add(line);
            }
            // grade what comes out
            if (requirement.judge != null) {
                unitMistakes += requirement.judge(outputs, inputMarkers, reporter);
            }
            pro.waitFor();
        } catch (IOException e) {
            reporter.reportException(e, "Grader cannot access streams of the student program.");
            e.printStackTrace();
            throw e;
        }
        pro.waitFor();
        return unitMistakes;
    }


    public int gradeOneUnit(Process pro,  BufferedReader reader, BufferedWriter writer, RequiredInputOutput requirement, Reporter reporter, boolean interactive) throws InterruptedException, IOException, StudentFatalMistake {
        if (interactive) {
            return gradeOneUnit(pro,   reader,  writer, requirement, reporter);
        }
        // one unit is defined by one requiredInputOutput object
        // this function does not handle any exception. it rethrows them.
        int unitMistakes = 0;
        ArrayList<Integer> inputMarkers = new ArrayList<>();
        // first read what the program has to say
        // read what comes out
        String line;
        ArrayList<String> outputs = new ArrayList<>();

//        // input into the program
        reporter.divider("stdin");
        OutputStream stdin = pro.getOutputStream();

        // read what comes out
        reporter.divider("stdout");
        pro.waitFor();
        try {
            while ((line = reader.readLine()) != null) {
                reporter.writeln(line);
                outputs.add(line);
            }
            // grade what comes out
            if (requirement.judge != null) {
                unitMistakes += requirement.judge(outputs, inputMarkers, reporter);
            }
            pro.waitFor();
        } catch (IOException e) {
            reporter.reportException(e, "Grader cannot access streams of the student program.");
            e.printStackTrace();
            throw e;
        }
        pro.waitFor();
        return unitMistakes;
    }

    public void miniRegrade(String original) {
        // very manual, no automation
        // compile and run business.

        File originalFile = new File(original);
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
            e.printStackTrace();
        }
        //
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
    }

    public void startManualRegrade() {
        // load the students that need to be regraded
        ArrayList<File> manualGrade = (ArrayList<File>) javaFiles.clone();
//        try {
//            ObjectInputStream in = null;
//            try {
//                in = new ObjectInputStream(new FileInputStream("manualGrade.arrayList"));
//                manualGrade = (ArrayList<File>) in.readObject();
//                in.close();
//                if (manualGrade.size() == 0) {
//                    System.out.println("Empty manual grade loaded. Abort. No changes made");
//                    System.exit(-99);
//                }
//            } catch (FileNotFoundException ignored) {
//                manualGrade= (ArrayList<File>) javaFiles.clone();
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        Scanner stdin = new Scanner(System.in);

        Reporter reporter = new Reporter("regrade_log", reportPathFile, true);
        for (int i = 0; i < manualGrade.size(); i++) {
            boolean done = false;
            boolean ask = true;

            while (!done) {
                File originalFile = manualGrade.get(i);
                String studentID = parseStudentID(originalFile);
                reporter.writeln(studentID);
                if (this.startFromStudentID !=null){
                    if (studentID.equals(this.startFromStudentID)){
                        this.startFromStudentID =null;
                    }else{
                        break;
                    }
                }
                if (ask){
                    System.out.println("Grade this student? [Y/n]");
                }
                if (!ask||!stdin.next().toLowerCase().equals("n")){
                    ask=true;
                    // if grade this
                    reporter.writeln(originalFile.getName());

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
                    System.out.println();
                    System.out.println("finished");
                    // what do I do next?
                    while (ask) {
                        System.out.println("Code? [c] Next? [n] Another run? [r] ");
                        String ret = stdin.next().toLowerCase();
                        if (ret.toLowerCase().equals("n")) {
//                            try {
//                                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("manualGrade.arrayList"));
//                                out.writeObject(new ArrayList<File>((manualGrade.subList(i + 1, manualGrade.size()))));
//                                out.flush();
//                                out.close();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            ask = false;
                            done = true;
                        }
                        if (ret.toLowerCase().equals("c")) {
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(originalFile));
                                String line = null;
                                while ((line = br.readLine()) != null) {
                                    System.out.println(line);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ask = true;
                        }
                        if (ret.toLowerCase().equals("r")) {
//                            try {
//                                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("manualGrade.arrayList"));
//                                out.writeObject(new ArrayList<File>(manualGrade.subList(i + 1, manualGrade.size())));
//                                out.flush();
//                                out.close();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            ask = false;
                            done = false;
                        }
                    }
                }else{
                    done=true;
                }

            }
        }
    }



    private File copySubmissionTemp(File toBeCopied) throws IOException {
        // copies data files

        // strips away package statement automatically
        // because there is only one file to be graded
        // so it returns, otherwise I don't know
        BufferedReader read = new BufferedReader(new FileReader(toBeCopied));
        FileUtils.cleanDirectory(tempPathFile);
        File tempfile = new File(tempPathFile, parseClassName(toBeCopied) + ".java");
        BufferedWriter write = new BufferedWriter(new FileWriter(tempfile));

        // remove package def
        String line;
        while ((line = read.readLine()) != null) {
            int robustCounter = 0;
            if (!line.contains("package") && robustCounter < 5) {
                write.write(line + "\n");
                robustCounter++;
            }
        }
//        File copyTo = new File(tempPathFile, parseClassName(toBeCopied) + ".java");
//        FileUtils.copyFile(toBeCopied, copyTo);
        write.flush();
        write.close();
        read.close();
        return tempfile;
    }

    private String parseStudentID(File javaFile) {
        // absdcsd_53652_8944872_Operations.java
        String fname = javaFile.getName();
        String[] parts = fname.split("_");
        String studentID = parts[0];

        return studentID;
    }

    public static String parseClassName(File file) {
        // abcddd_53652_8944872_Operations.java
        String fname = file.getName();
        String[] parts = fname.split("_");
        String className = parts[parts.length - 1].split("\\.")[0];
        className = className.split("-")[0];
        className = className.split("\\(")[0];
        return className;
    }

    private String inferClassName() {
        File f1 = javaFiles.get(24);
        File f2 = javaFiles.get(26);
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


    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }
}