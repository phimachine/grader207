import java.io.BufferedReader;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;

class TimeoutReader{
    ExecutorService executor = Executors.newFixedThreadPool(2);
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
        Callable<String> readTask = () -> scan.next();
        String ret[]=new String[2];
        while (readString != null) {
            Future<String> future = executor.submit(readTask);
            try {
                readString = readString+" "+future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof NoSuchElementException){
                    // this happens when the output stream is closed reaching EOF
                    ret[0]=readString;
                    ret[1]="true";
                    return ret;
                }
                // this happens when the reader reads before the student program returns a result
                e.printStackTrace();
                return null;
            } catch (TimeoutException e){
                ret[0]=readString;
                ret[1]="false";
                return ret;
            }
        }
        ret[0]=readString;
        ret[1]="false";
        return ret;
    }
}