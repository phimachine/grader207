import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class EmailInteractor implements Interactor, Judge {

    Random random = new Random();
    int mistakeType;

    String input;
    String username;
    String domain;

    /***
     *
     * @param mistakeType integer from [0, 10]
     */
    public EmailInteractor(int mistakeType) {
        this.mistakeType = mistakeType;

        // mistake types:
        // 0, then it is correct.
        // 1, then it lacks .
        // 2, then it lacks @
        // 3, (rule 1) the first character is @
        // 4, (rule 2) no string between @ and .
        // 5, . before @
        // 6, last char is .

        // generate username and domain basis
        String domain1, domain2, domain3;
        username = RandomStringUtils.randomAlphanumeric(10);
        domain1 = RandomStringUtils.randomAlphanumeric(5);
        domain2 = RandomStringUtils.randomAlphanumeric(5);
        domain3 = RandomStringUtils.randomAlphanumeric(5);
        domain = domain1 + "." + domain2 + "." + domain3;

        if (mistakeType == 0) {
            input = username + "@" + domain;
        } else if (mistakeType == 1) {
            input = username + "@" + domain1 + domain2;
        } else if (mistakeType == 2) {
            input = username + domain;
        } else if (mistakeType == 3) {
            input = "@" + username + "@" + domain;
        } else if (mistakeType == 4) {
            input = username + "@" + "." + domain2 + "." + domain3;
        } else if (mistakeType == 5) {
            input = domain1 + "." + domain2 + "." + domain3 + "@" + username;
        } else {
            input = username + "@" + domain1 + "." + domain2 + "." + domain3 + ".";
        }

    }

    public static void main(String[] args) {
        EmailInteractor emailGuy = new EmailInteractor(1);
        System.out.println(emailGuy.input);

    }

    @Override
    public String interact(String currentOutput) {
        if (currentOutput != null) {
            return input + "\n";
        } else {
            return null;
        }
    }

    @Override
    public void prepare(File tempPathFile) throws IOException {

    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter) throws StudentFatalMistake {
        ArrayList<String> newOutputs = new ArrayList<>();
        int mistakes;
        if (mistakeType == 0) {
            mistakes = 2;
        } else {
            mistakes = 1;
        }


        for (String output :
                outputs) {
            String[] splitted = output.split("\\s+");
            for (String split :
                    splitted) {
                if (mistakeType == 0) {
                    if (split.equals(username)) {
                        mistakes -= 1;
                    } else if (split.equals(domain)) {
                        mistakes -= 1;
                    }
                } else {
                    if (split.toLowerCase().equals("invalid")) {
                        mistakes = 0;
                    }
                }
            }
        }

        if (mistakes != 0){
            if (mistakeType==0){
                reporter.divider("VALID EMAIL", ""+mistakes);
                reporter.divider(input, ""+mistakes);
                reporter.divider("username: "+username, ""+mistakes);
                reporter.divider("domain: "+domain, ""+mistakes);
            }else{
                reporter.divider("INVALID EMAIL", ""+mistakes);
                reporter.divider(input, ""+mistakes);
            }
        }
        return mistakes;
    }
}
