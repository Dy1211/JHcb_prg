import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaMIO {

    static final double TARIFA_NORMAL = 3000;
    static final double TARIFA_ESTUDIANTE = 1500;
    static final int CAPACIDAD_MAXIMA = 80;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            try {
                System.out.println("\n==================================");
                System.out.println("     🚍 SISTEMA MIO - CALI");
                System.out.println("==================================");
                System.out.println("1. Cobrar tarifa");
                System.out.println("2. Control de ruta");
                System.out.println("3. Reporte del dia + alertas");
                System.out.println("0. Salir");
                System.out.println("==================================");
                System.out.print("Seleccione una opcion: ");
                opcion = sc.nextInt();

                switch (opcion) {
                    case 1:
                        cobrarTarifa(sc);
                        break;
                    case 2:
                        controlRuta(sc);
                        break;
                    case 3:
                        reporteDia(sc);
                        break;
                    case 0:
                        System.out.println("\n👋 Cerrando sistema...");
                        break;
                    default:
                        System.out.println("❌ Opcion no valida");
                }

            } catch (InputMismatchException e) {
                System.out.println("❌ Error: debes ingresar numeros");
                sc.nextLine();
            }
        }

        sc.close();
    }

    // =========================
    // COBRO DE TARIFA
    // =========================
    public static void cobrarTarifa(Scanner sc) {

        System.out.println("\n========== 💳 COBRO DE TARIFA ==========");

        System.out.print("🕒 Hora (0-23): ");
        int hora = sc.nextInt();

        if (hora < 0 || hora > 23) {
            System.out.println("❌ Hora invalida");
            return;
        }

        System.out.println("\n👤 Tipo de usuario:");
        System.out.println("1. Normal");
        System.out.println("2. Estudiante");
        System.out.println("3. Adulto mayor");
        System.out.println("4. Discapacitado");
        System.out.print("Seleccione: ");

        int tipo = sc.nextInt();

        double tarifaBase = 0;
        double descuento = 0;
        String nombreTipo = "";

        boolean esHoraValle = (hora >= 9 && hora <= 16);

        switch (tipo) {
            case 1:
                nombreTipo = "Normal";
                tarifaBase = TARIFA_NORMAL;
                break;
            case 2:
                nombreTipo = "Estudiante";
                tarifaBase = TARIFA_ESTUDIANTE;
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
                System.out.println("❌ Tipo invalido");
                return;
        }

        double total = tarifaBase - descuento;

        System.out.println("\n========== 🎟️ TIQUETE ==========");
        System.out.println("Hora: " + hora);
        System.out.println("Tipo: " + nombreTipo);
        System.out.println("Total: $" + total);
        System.out.println("================================");
    }

    // =========================
    // CONTROL DE RUTA 
    // =========================
    public static void controlRuta(Scanner sc) {

        System.out.println("\n========== 🚏 CONTROL DE RUTA ==========");

        int pasajerosActuales = 0;
        int parada = 1;

        while (parada <= 25) {

            System.out.println("\n----- PARADA " + parada + " -----");

            System.out.print("⬆️ Suben (-1 salir): ");
            int suben = sc.nextInt();

            if (suben == -1) break;

            if (suben < 0) {
                System.out.println("❌ Valor invalido");
                continue;
            }

            int bajan = 0;

            // ✅ En la primera parada NO se pregunta bajan
            if (parada > 1) {
                System.out.print("⬇️ Bajan: ");
                bajan = sc.nextInt();

                if (bajan < 0) {
                    System.out.println("❌ Valor invalido");
                    continue;
                }

                if (bajan > pasajerosActuales) {
                    bajan = pasajerosActuales;
                }

                // Primero bajan
                pasajerosActuales -= bajan;
            }

            // Espacio disponible
            int capacidadDisponible = CAPACIDAD_MAXIMA - pasajerosActuales;

            if (suben > capacidadDisponible) {
                System.out.println("⚠️ Bus lleno, solo pueden subir: " + capacidadDisponible);
                suben = capacidadDisponible;
            }

            // Luego suben
            pasajerosActuales += suben;

            System.out.println("👥 Pasajeros actuales: " + pasajerosActuales);

            parada++;
        }
    }

    // =========================
    // REPORTE DEL DÍA
    // =========================
    public static void reporteDia(Scanner sc) {

        System.out.println("\n========== 📊 REPORTE DEL DIA ==========");

        int rutasConAlerta = 0;

        for (int ruta = 1; ruta <= 3; ruta++) {

            String nombreRuta;
            if (ruta == 1) nombreRuta = "T31";
            else if (ruta == 2) nombreRuta = "A10";
            else nombreRuta = "P22A";

            System.out.println("\n🚍 Ruta " + nombreRuta);

            System.out.print("Pasajeros: ");
            int pasajeros = sc.nextInt();

            System.out.print("Vueltas: ");
            int vueltas = sc.nextInt();

            if (pasajeros < 0 || vueltas < 0) {
                System.out.println("❌ Datos invalidos");
                ruta--;
                continue;
            }

            System.out.print("Porcentaje normal (0-100): ");
            int pctNormal = sc.nextInt();

            if (pctNormal < 0 || pctNormal > 100) {
                System.out.println("❌ Porcentaje invalido");
                ruta--;
                continue;
            }

            int pctEstudiante = 100 - pctNormal;

            double recaudo = pasajeros *
                    ((pctNormal / 100.0) * TARIFA_NORMAL +
                     (pctEstudiante / 100.0) * TARIFA_ESTUDIANTE);

            boolean alerta = pasajeros < 500 || vueltas > 20 ||
                    recaudo < (pasajeros * TARIFA_ESTUDIANTE * 0.7);

            if (alerta) {
                rutasConAlerta++;
                System.out.println("⚠️ ALERTA en " + nombreRuta);
            } else {
                System.out.println("✅ OK en " + nombreRuta);
            }
        }

        System.out.println("\n================================");
        System.out.println("Rutas con alerta: " + rutasConAlerta);

        if (rutasConAlerta == 0)
            System.out.println("✅ OPERACION NORMAL");
        else if (rutasConAlerta == 1)
            System.out.println("⚠️ REVISION MENOR");
        else if (rutasConAlerta == 2)
            System.out.println("🚨 REVISION URGENTE");
        else
            System.out.println("🔥 ESTADO CRITICO");

        System.out.println("================================");
    }
}