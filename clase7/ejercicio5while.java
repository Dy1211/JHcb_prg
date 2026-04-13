package clase7;

public class ejercicio5while {
    public static void main(String[] args) {

        int i = 1;

        while (i <= 10) { // tablas del 1 al 10
            System.out.println("Tabla del " + i);

            int j = 1;

            while (j <= 10) { // multiplicar del 1 al 10
                System.out.println(i + " x " + j + " = " + (i * j));
                j++;
            }

            System.out.println(); // espacio entre tablas
            i++;
        }
    }
}