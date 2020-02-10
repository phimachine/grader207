import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ArithInteractor implements Interactor, Judge {
    Random random = new Random();

    public ArithInteractor(){

    }


    @Override
    public String interact(String currentOutput) {
        currentOutput = currentOutput.toLowerCase();
        String input = null;
        if (currentOutput.contains("enter")){
            input ="1 1 2 3 5\n";
        }
        return input;
    }

    @Override
    public void prepare(File tempPathFile) throws IOException {

    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter) throws StudentFatalMistake {
        for (String output :
                outputs) {
            System.out.println(output);
        }
        return 0;
    }
}
