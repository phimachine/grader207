import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EmptyUser implements Interactor, Judge {

    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter){
        for (String output:outputs
             ) {
            if (!output.equals("") && output!=null){
                if (output.contains("name is")){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public String interact(String currentOutput) {
        return "";
    }

    @Override
    public void prepare(File tempPathFile) throws IOException {
        return;
    }
}
