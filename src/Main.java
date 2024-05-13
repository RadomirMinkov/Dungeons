import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Double> arr = new ArrayList<>();
        arr.add(10.2);
        arr.add(2.1);
        arr.add(10.2);
        arr.remove(10.2);
        System.out.println(arr.toString());
    }
}