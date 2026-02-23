package clase4;

import java.util.Scanner;

public class Ejercicio41a {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // PEDIR EL ANGULO
        System.out.println("dijite el ANGULO");
        int ANGULO = sc.nextInt();
        // condicional
        if (ANGULO == 90) {
            System.out.println("el ANGULO es un ANGULO recto");

        } else {
            System.out.println("el ANGULO no es un ANGULO recto");
        }

        sc.close();
    }
}
