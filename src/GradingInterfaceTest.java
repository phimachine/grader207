import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GradingInterfaceTest {

    @org.junit.jupiter.api.Test
    void manualRegrade() throws IOException {
        GradingInterface g = new GradingInterface();
        ByteArrayInputStream in = new ByteArrayInputStream("y".getBytes());
        System.setIn(in);
        g.manualRegrade();
        System.setIn(System.in);
    }
}