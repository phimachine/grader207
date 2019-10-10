import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GraderTest {
    @Test
    public void parseClassName(){
        String cn=Grader.parseClassName(new File("asdfasf_42238_8948869_Operations-1.java"));
        System.out.println(cn);
    }
}