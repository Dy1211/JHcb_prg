package parcial1;
import java.util.Scanner;

public class Cajero {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int CLAVE = 1234;
        int SALDOINICIAL = 800000;
        int claveUsuario, retiro, nuevoSaldo;

        System.out.print("Digite la claveUsuario: ");
        claveUsuario = sc.nextInt();

        if (CLAVE == claveUsuario) {

            System.out.println("Bienvenido al cajero de la Uniajc");
            System.out.print("Digite la cantidad a retirar: ");
            retiro = sc.nextInt();

            // Si el retiro es mayor a 200000 se suma el costo de transacción
            if (retiro > 200000) {
                retiro = retiro + 2000;
            }

            if (retiro <= SALDOINICIAL) {
                nuevoSaldo = SALDOINICIAL - retiro;
                System.out.println("Su saldo actual es: " + nuevoSaldo);
                System.out.println("Muchas Gracias!!!!");
            } else {
                System.out.println("El valor solicitado más el costo de la transacción es mayor al saldo.");
            }

        } else {
            System.out.println("Su clave no es correcta... intenta nuevamente...");
        }

        sc.close();
    }
}


