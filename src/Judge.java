import java.util.ArrayList;

public interface Judge {

    int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                 Reporter reporter) throws StudentFatalMistake;
}
