package clase7;
import java.util.Scanner;

public class ejercicio3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        int num = 1;
        int suma = 0;

        while (num != 0) {
            System.out.print("Ingrese un número entero (0 para terminar): ");
            num = sc.nextInt();
            
            suma = suma + num;
        }

        System.out.println("La suma total es: " + suma);
        
        sc.close();
    }
}

