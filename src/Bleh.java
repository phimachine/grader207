import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bleh {

    public static void main(String[] args) {
        int j = 0;
        System.out.println(j++);
        System.out.println(j);
        ArrayList<Integer> list = new
         ArrayList<>();
        for (int i = 0; i <10; i++) {
            list.add(i);
        }
        ArrayList<Integer> newList = new ArrayList<>();
//
//        for (int i :
//                newList) {
//            System.out.println(i);
//        }
        ArrayList<Integer> nnL=dumbAdd(newList);
        for (int i :
                nnL) {
            System.out.println(i);
        }
    }

    public static ArrayList<Integer> dumbAdd(ArrayList<Integer> data) {// If the input array list is [4, 3, -2, 9], the returned array list is [9, -2, 3, 4]
        // Your code goes below
        ArrayList<Integer> reverse = new ArrayList<Integer>();
        int i;
        for (i = 0; i < data.size(); i++) reverse.add(data.get(i));
        System.out.println(reverse);
        return reverse;

    }// end reverse
}