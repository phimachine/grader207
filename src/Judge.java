import java.util.ArrayList;

public interface Judge {

    // Returns the number of mistakes the student has made
    int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                 Reporter reporter) throws StudentFatalMistake;
}
