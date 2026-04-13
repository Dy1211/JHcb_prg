
 import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaMIO {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {

            try {
                System.out.println("\n=== SISTEMA MIO CALI ===");
                System.out.println("1. Cobrar tarifa");
                System.out.println("2. Control de ruta");
                System.out.println("3. Reporte del dia + alertas");
                System.out.println("0. Salir");
                System.out.print("Seleccione: ");
                opcion = sc.nextInt();

                switch (opcion) {

                    case 1:
                        // FASE 1
                        System.out.print("Hora (0-23): ");
                        int hora = sc.nextInt();

                        if (hora < 0 || hora > 23) {
                            System.out.println("Hora invalida");
                            break;
                        }

                        System.out.print("Tipo (1-4): ");
                        int tipo = sc.nextInt();

                        double tarifaBase = 0;
                        double descuento = 0;
                        String nombreTipo = "";

                        boolean esHoraValle = (hora >= 9 && hora <= 16);

                        switch (tipo) {
                            case 1:
                                nombreTipo = "Normal";
                                tarifaBase = 3000;
                                break;
                            case 2:
                                nombreTipo = "Estudiante";
                                tarifaBase = 1500;
                                if (esHoraValle) {
                                    descuento = tarifaBase * 0.10;
                                }
                                break;
                            case 3:
                                nombreTipo = "Adulto mayor";
                                tarifaBase = 0;
                                break;
                            case 4:
                                nombreTipo = "Discapacitado";
                                tarifaBase = 0;
                                break;
                            default:
                                System.out.println("Tipo invalido");
                                continue;
                        }

                        double total = tarifaBase - descuento;

                        System.out.println("\nTIQUETE");
                        System.out.println("Hora: " + hora);
                        System.out.println("Tipo: " + nombreTipo);
                        System.out.println("Total: $" + total);
                        break;

                    case 2:
                        // FASE 2
                        int pasajerosActuales = 0;
                        int parada = 1;

                        while (parada <= 25) {

                            System.out.println("\nPARADA " + parada);
                            System.out.print("Suben (-1 salir): ");
                            int suben = sc.nextInt();

                            if (suben == -1) break;

                            if (suben < 0) {
                                System.out.println("Valor invalido");
                                continue;
                            }

                            System.out.print("Bajan: ");
                            int bajan = sc.nextInt();

                            if (bajan > pasajerosActuales) {
                                bajan = pasajerosActuales;
                            }

                            if (pasajerosActuales + suben - bajan > 80) {
                                suben = 80 - pasajerosActuales + bajan;
                                if (suben < 0) suben = 0;
                            }

                            pasajerosActuales = pasajerosActuales + suben - bajan;

                            System.out.println("Pasajeros actuales: " + pasajerosActuales);

                            parada++;
                        }
                        break;

                    case 3:
                        // FASE 4 + 5
                        int rutasConAlerta = 0;

                        for (int ruta = 1; ruta <= 3; ruta++) {

                            String nombreRuta;
                            if (ruta == 1) nombreRuta = "T31";
                            else if (ruta == 2) nombreRuta = "A10";
                            else nombreRuta = "P22A";

                            System.out.println("\nRuta " + nombreRuta);

                            System.out.print("Pasajeros: ");
                            int pasajeros = sc.nextInt();

                            System.out.print("Vueltas: ");
                            int vueltas = sc.nextInt();

                            System.out.print("Porcentaje normal (0-100): ");
                            int pctNormal = sc.nextInt();

                            if (pctNormal < 0 || pctNormal > 100) {
                                System.out.println("Porcentaje invalido");
                                continue;
                            }

                            int pctEstudiante = 100 - pctNormal;

                            double recaudo = pasajeros *
                                    ((pctNormal / 100.0) * 3000 +
                                     (pctEstudiante / 100.0) * 1500);

                            boolean alertaDemanda = pasajeros < 500;
                            boolean alertaVueltas = vueltas > 20;
                            boolean alertaRecaudo = recaudo < (pasajeros * 1500 * 0.7);

                            boolean tieneAlerta = alertaDemanda || alertaVueltas || alertaRecaudo;

                            if (tieneAlerta) {
                                rutasConAlerta++;
                                System.out.println("ALERTA en " + nombreRuta);
                            } else {
                                System.out.println("OK en " + nombreRuta);
                            }
                        }

                        System.out.println("\nRutas con alerta: " + rutasConAlerta);

                        if (rutasConAlerta == 0)
                            System.out.println("Operacion NORMAL");
                        else if (rutasConAlerta == 1)
                            System.out.println("REVISION MENOR");
                        else if (rutasConAlerta == 2)
                            System.out.println("REVISION URGENTE");
                        else
                            System.out.println("CRITICA");

                        break;

                    case 0:
                        System.out.println("Cerrando...");
                        break;

                    default:
                        System.out.println("Opcion no valida");
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: debes ingresar numeros");
                sc.nextLine(); // limpiar buffer
            }
        }

        sc.close();
    }
}