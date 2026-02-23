package clase4;

import java.util.Scanner;

public class ejercicio41b {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("digite la temperatura");
        int temp = sc.nextInt();
        if (temp >=100) {
            System.out.println("punto ebullicion");
        }else{
            System.out.println("no esta en el punto de ebullicion");
        }
        sc.close();
    }
    
}
