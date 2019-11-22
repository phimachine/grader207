import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface Interactor{
    String interact(String currentOutput);

    void prepare(File tempPathFile) throws IOException;
}
