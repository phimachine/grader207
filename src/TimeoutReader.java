import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;

class TimeoutReader{
    ExecutorService executor;
    long timeout;

    public TimeoutReader(long timeout) {
        this.timeout = timeout;
        // for every student, time out reader is initialized, and the static field needs to be reset, or else it
        // will be on the next input.
        ReadTask2.futureRead="";
    }

    public TimeoutReader() {
        // be generous with this parameter.
        // java programs are slow to boot up.
        this(1000);
    }

    public int readUntilTimeout(InputStream inputStream) throws TimeoutException {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int readByte = 1;
        // Read data with timeout
        Callable<Integer> readTask = () -> inputStream.read();
        while (readByte >= 0) {
            Future<Integer> future = executor.submit(readTask);
            try {
                readByte = future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (NoSuchElementException e){
                System.out.println("here");
                e.printStackTrace();
            }
            if (readByte >= 0)
                return readByte;
        }
        return 0;
    }
//    public String[] readyRead(BufferedReader br) {
//        String readString="";
//        String ret[]=new String[2];
//
//        do {
//            // Read data with timeout
//            ReadTask2 readTask = new ReadTask2(br);
//            executor  = Executors.newFixedThreadPool(2);
//            // an executor submitted into the future is able to grab the input and run. lol.
//            Future<String> future = executor.submit(readTask);
//            try {
//                String thisStr=future.get(timeout, TimeUnit.MILLISECONDS);
//                if (thisStr == null){
//                    ret[0]=readString;
//                    ret[1]="true";
//                    future.cancel(true);
//                    executor.shutdown();
//                    return ret;
//                }else{
//                    readString += thisStr;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }catch (ExecutionException e) {
//                if (e.getCause() instanceof NoSuchElementException || e.getCause() instanceof IndexOutOfBoundsException){
//                    // this happens when the output stream is closed reaching EOF
//                    ret[0]=readString;
//                    ret[1]="true";
//                    future.cancel(true);
//                    executor.shutdown();
//                    return ret;
//                }
//                // this happens when the reader reads before the student program returns a result
//                e.printStackTrace();
//                future.cancel(true);
//                executor.shutdown();
//                ret[0]=null;
//                ret[1]="false";
//                return ret;
//            } catch (TimeoutException e){
//                // if student does not enter a new line, scanner would not be able to process this unfinished stream
//                ret[0]=readString;
//                ret[1]="false";
//                future.cancel(true);
//                executor.shutdown();
//                return ret;
//            }
//        }while(true);
//    }

    public String[] readUntilTimeout(BufferedReader br) {
        String readString;
        if (ReadTask2.futureRead==null){
            readString="";
        }else{
            readString=ReadTask2.futureRead;
        }
        String ret[]=new String[2];
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        do {
            // Read data with timeout
            ReadTask2 readTask = new ReadTask2(br);
            executor  = Executors.newFixedThreadPool(2);
            // after the executor
            Future<String> future = executor.submit(readTask);
            try {
                String thisStr=future.get(timeout, TimeUnit.MILLISECONDS);
                if (thisStr == null){
                    ret[0]=readString;
                    ret[1]="true";
                    future.cancel(true);
                    executor.shutdown();
                    return ret;
                }else{
                    readString += thisStr;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (ExecutionException e) {
                if (e.getCause() instanceof NoSuchElementException || e.getCause() instanceof IndexOutOfBoundsException){
                    // this happens when the output stream is closed reaching EOF
                    ret[0]=readString;
                    ret[1]="true";
                    // what happens when the current output finishes?
                    // a submitted future will not cancel, thus it will grab the next input and it will not be here anymore
                    future.cancel(true);
                    executor.shutdown();
                    return ret;
                }
                // this happens when the reader reads before the student program returns a result
                e.printStackTrace();
                future.cancel(true);
                executor.shutdown();
                ret[0]=null;
                ret[1]="false";
                return ret;
            } catch (TimeoutException e){
                // if student does not enter a new line, scanner would not be able to process this unfinished stream
                ret[0]=readString;
                ret[1]="false";
                future.cancel(true);
                executor.shutdown();
                return ret;
            }
        }while(true);
    }

    public String[] readUntilTimeout(Scanner scan) {
        String readString = "";
        // Read data with timeout
//        Callable<String> readTask = () -> scan.next();
        ReadTask readTask = new ReadTask(scan);
        String ret[]=new String[2];
        executor  = Executors.newFixedThreadPool(2);

        // why are we getting last?
//        if (readTask.getLast()!=null){
//            readString+=(readTask.getLast()+"\n");
//        }
        while (readString != null) {
            Future<String> future = executor.submit(readTask);
            try {
                readString = readString+future.get(timeout, TimeUnit.MILLISECONDS)+"\n";
            } catch (InterruptedException e) {
                future.cancel(true);
                executor.shutdown();
                e.printStackTrace();
                System.exit(-1);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof NoSuchElementException || e.getCause() instanceof IndexOutOfBoundsException){
                    // this happens when the output stream is closed reaching EOF
                    ret[0]=readString;
                    ret[1]="true";
                    future.cancel(true);
                    executor.shutdown();
                    readTask.resetLast();
                    return ret;
                }
                // this happens when the reader reads before the student program returns a result
                e.printStackTrace();
                future.cancel(true);
                executor.shutdown();
                ret[0]=null;
                ret[1]="false";
                return ret;
            } catch (TimeoutException e){
                ret[0]=readString;
                ret[1]="false";
                future.cancel(true);
                executor.shutdown();
                return ret;
            }
        }
        ret[0]=readString;
        ret[1]="false";
        executor.shutdown();
        return ret;
    }
}

// after time out interrupts a future, the future will grab the input stream word and return to no one
// this class stores the returned value
class ReadTask implements Callable<String>{
    static String last;
    private volatile boolean done=false;
    Scanner scan;

    public ReadTask(Scanner scan) {
        this.scan = scan;
    }

    public synchronized String call(){
        String ret=scan.nextLine();
        last=ret;
        return ret;
    }

    public String getLast(){
        return last;
    }

    public void resetLast(){
        last="";
    }
}

class ReadTask2 implements Callable<String>{
    static String futureRead="";
    private volatile boolean done=false;
    BufferedReader br;

    public ReadTask2(BufferedReader br) {
        this.br = br;
    }

    public synchronized String call(){
        int ret = 0;
        try {
            ret = br.read();
            if (ret!=-1){
                futureRead=""+(char) ret;
                return ""+(char) ret;
            }else{
                futureRead=null;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // should not be reachable
        System.exit(-1);
        return "";
    }
}