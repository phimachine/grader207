import java.io.BufferedReader;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;

class TimeoutReader{
    ExecutorService executor;
    long timeout;

    public TimeoutReader(long timeout) {
        this.timeout = timeout;
    }

    public TimeoutReader() {
        this(100);
    }

    public int read(InputStream inputStream) throws TimeoutException {
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
            }
            if (readByte >= 0)
                return readByte;
        }
        return 0;
    }

    public String read(BufferedReader br) throws TimeoutException {
        String readString = "";
        // Read data with timeout
        Callable<String> readTask = () -> br.readLine();
        Future<String> future = executor.submit(readTask);
        try {
            readString = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return readString;
    }

    public String[] read(Scanner scan) {
        String readString = "";
        // Read data with timeout
//        Callable<String> readTask = () -> scan.next();
        ReadTask readTask = new ReadTask(scan);
        String ret[]=new String[2];
        executor  = Executors.newFixedThreadPool(2);
        if (readTask.getLast()!=null){
            readString+=(readTask.getLast()+" ");
        }
        while (readString != null) {
            Future<String> future = executor.submit(readTask);
            try {
                readString = readString+future.get(timeout, TimeUnit.MILLISECONDS)+" ";
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
        String ret=scan.next();
        last=ret;
        return ret;
    }

    public String getLast(){
        return last;
    }
}