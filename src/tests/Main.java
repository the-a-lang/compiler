import org.alang.ALang;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final ALang aLang = new ALang();
        String a = "";
        for (char ch : "fork pitus".toCharArray()) {
            a += "\\u00" + Integer.toHexString(ch) ;
        }
        System.out.println(a);

        final String expression = "max(1 + 1)";
        System.out.println(expression);
        System.out.println(Arrays.toString(aLang.process(expression)));
        System.out.println("\u0C95".length());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String expr = scanner.nextLine();
            long start = System.nanoTime();
            Object[] res = aLang.process(expr);
            long end = System.nanoTime();
            System.out.println((end - start) / 1E6);
            System.out.println(Arrays.toString(res));
        }
    }
}
