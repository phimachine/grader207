import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class NewArrayGrader implements Interactor, Judge {
    //jibberish appended!
    public ArrayList<Integer> array;
    public double avg;
    public ArrayList<Integer> sortedUp;
    public ArrayList<Integer> reverse;
    public int max;
    public int min;
    public int maxIdx;
    public int minIdx;


    public double intDivisionWrong;
    private final int len = 20;
    Random random = new Random();
    boolean jibberish;

    public NewArrayGrader() {
        array = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < len; i++) {
            array.add(random.nextInt(500));
            sum += array.get(i);
        }
        avg = (double) sum / len;
        intDivisionWrong = sum / len;
        avg = ArrayOperations.average(array);
        max = ArrayOperations.largest(array);
        min = ArrayOperations.smallest(array);
        maxIdx = ArrayOperations.getMaxIndex(array);
        minIdx = ArrayOperations.getMinIndex(array);
        reverse = ArrayOperations.reverse((ArrayList<Integer>) array.clone());
        sortedUp = (ArrayList<Integer>) array.clone();
        ArrayOperations.sortUp(sortedUp);
    }

    public int judgeReport(ArrayList<String> inputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers,
                           Reporter reporter) throws StudentFatalMistake {
        ArrayList<String> newOutputs = new ArrayList<>();
        for (String output :
                outputs) {
            String[] splitted = output.split("\\r?\\n");
            for (String line : splitted
            ) {
                newOutputs.add(line);
            }
        }

        int mistakes = 50;

        for (String line : newOutputs) {
            if (line.contains("average")) {
                if (lineFindNumber(line, avg)) {
                    mistakes -= 2;
                } else if (lineFindNumber(line, intDivisionWrong)) {
                    mistakes -= 1;
                    reporter.divider("correct avg:" + avg);
                } else {
                    reporter.divider("correct avg:" + avg);
                }

            } else if (line.contains("The smallest")) {
                if (lineFindNumber(line, min)) {
                    mistakes -= 2;
                }else{
                    reporter.divider("correct min:" + min);
                }
            } else if (line.contains("index of the smallest")) {
                if (lineFindNumber(line, minIdx)) {
                    mistakes -= 2;
                }else{
                    reporter.divider("correct min idx:" + minIdx);
                }

            } else if (line.contains("The largest element")) {
                if (lineFindNumber(line, max)) {
                    mistakes -= 2;
                }else{
                    reporter.divider("correct max:" + max);
                }

            } else if (line.contains("index of the largest")) {
                if (lineFindNumber(line, maxIdx)) {
                    mistakes -= 2;
                }else{
                    reporter.divider("correct max idx:" + maxIdx);
                }

            } else if (line.contains("sorted array in non-decreasing")) {
                String[] studentAnswer = line.split(":");
                String arrayAnswer = studentAnswer[1];
                String[] words = arrayAnswer.split("\\s");
                ArrayList<Integer> numbers = new ArrayList<>();
                for (String word : words) {
                    try {
                        numbers.add(Integer.parseInt(word));
                    } catch (NumberFormatException ignored) {

                    }
                }

                int goal = mistakes-20;

                if (numbers.size() == sortedUp.size()) {
                    for (int i = 0; i < numbers.size(); i++) {
                        if (numbers.get(i).equals(sortedUp.get(i))) {
                            mistakes -= 1;
                        }
                    }
                }
                if (mistakes!=goal) {
                    reporter.divider("sorted up wrong");
                    reporter.writeln("correct:" +
                            sortedUp.stream().map(Object::toString)
                                    .collect(Collectors.joining(" ")));
                }


            } else if (line.contains("reverse of the")) {
                String[] studentAnswer = line.split(":");
                String arrayAnswer = studentAnswer[1];
                String[] words = arrayAnswer.split("\\s");
                ArrayList<Integer> numbers = new ArrayList<>();
                for (String word : words) {
                    try {
                        numbers.add(Integer.parseInt(word));
                    } catch (NumberFormatException ignored) {

                    }
                }
                int goal = mistakes-20;
                if (numbers.size() == reverse.size()) {
                    for (int i = 0; i < numbers.size(); i++) {
                        if (numbers.get(i).equals(reverse.get(i))) {
                            mistakes -= 1;
                        }
                    }
                }
                if (mistakes!=goal) {
                    reporter.divider("reverse wrong");
                    reporter.writeln("correct:" +
                            reverse.stream().map(Object::toString)
                                    .collect(Collectors.joining(" ")));
                }
            }
        }

        reporter.divider("MISTAKES", "" + mistakes);
        return mistakes;
    }

    private boolean lineFindNumber(String line, double number) {
        String[] words = line.split("\\s");
        boolean ret=false;
        for (String word : words) {
            try {
                double num = Double.parseDouble(word);
                if (num == number) {
                    ret = true;
                }
            } catch (NumberFormatException ignored) {
                ;
            }
        }
        return ret;
    }

    public void prepare(File tempPathFile) throws IOException {
    }

    @Override
    public String interact(String currentOutput) {
        if (currentOutput != null) {
            if (currentOutput.contains("ending")){
                if (!jibberish) {
                    String input = " ";
                    for (int n :
                            array) {
                        input += n;
                        input += " ";
                    }

                    return input + " Q \n";
                } else {
                    return "Q";
                }
            }else{
                return null;
            }


        } else {
            return null;
        }
    }

    @Override
    public int judgment(ArrayList<String> customInputs, ArrayList<String> outputs, ArrayList<Integer> inputMarkers, Reporter reporter) throws StudentFatalMistake {
        return judgeReport(customInputs, outputs, inputMarkers, reporter);
    }
}


class ArrayOperations { // main method

    static void sortUp(ArrayList<Integer> data) {

        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = i + 1; j < data.size(); j++) {
                if (data.get(j) < data.get(i)) {
                    int temp = data.get(i);
                    data.set(i, data.get(j));
                    data.set(j, temp);
                }// end for if
            }// end for loop
        } // end for loop

    }// end for sortUp

    static ArrayList<Integer> reverse(ArrayList<Integer> data) {
        int size = 0;
        for (int k = 0; k < data.size(); k++) {
            size++;
        } // end for loop

        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = data.get(i);
        } // end for loop
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;

                } // end for if
            } // end for loop
        } // end for loop
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int num : arr) {
            list.add(num);
        }
        return list;  // returning the list
    }// end for reverse

    static int getMaxIndex(ArrayList<Integer> data) {
        int size = 0;
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            size++;
        }// end for first for loop
        for (int i = 0; i < size; i++) {
            if (data.get(index) < data.get(i)) {
                index = i;
            }// end for if
        }// end for loop
        return index;
    }//end for getMaxIndex

    static int largest(ArrayList<Integer> data) {

        int largest = data.get(0);
        for (int num : data) {
            if (largest < num) {
                largest = num;
            }// end for if
        }// end for loop
        return largest;
    } // end for largest

    static int getMinIndex(ArrayList<Integer> data) {
        int size = 0;
        int index = 0;
        for (int j = 0; j < data.size(); j++) {
            size++;
        } // end for loop
        for (int i = 0; i < size; i++) {
            if (data.get(index) > data.get(i))
                index = i;
        }// end for loop
        return index;
    } // end for getMinIndex

    static int smallest(ArrayList<Integer> data) {
        int smallest = data.get(0);
        for (int num : data) {
            if (smallest > num)
                smallest = num;
        }// end for if

        return smallest;
    }// end for smallest

    static double average(ArrayList<Integer> data) {
        double total = 0;
        double average = 0;
        for (int i = 0; i < data.size(); i++) {
            total += data.get(i);
            average = total / data.size();
        } // end for loop
        return average;
    }// end for average

    static void printArray(ArrayList<Integer> data) {
        for (int num : data) {
            System.out.print(num + " ");
        }
    }// end for printArray

    // . . . Other methods to follow here
    // . . .
    // . . .
    // . . .
    // . . .

}// end class ArrayOperations
