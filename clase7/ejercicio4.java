package clase7;
import java.util.Scanner;
public class ejercicio4 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double numero;
        double suma = 0;
        int contador = 0;

        System.out.println("Ingrese números positivos (un número no positivos para terminar el proceso):");
        numero = sc.nextDouble();

        while (numero > 0) {
            suma = suma + numero;
            contador++;

            System.out.println("Ingrese otro número:");
            numero = sc.nextDouble();
        }

        if (contador > 0) {
            double media = suma / contador;
            System.out.println("La media es: " + media);
        } else {
            System.out.println("No se ingresaron números positivos.");
        }

        sc.close();
    }
}