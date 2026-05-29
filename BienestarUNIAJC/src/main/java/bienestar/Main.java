package bienestar;

import bienestar.model.Actividad.Categoria;
import bienestar.service.BienestarService;
import bienestar.util.DataLoader;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Punto de entrada del sistema Bienestar UNIAJC.
 * Menú principal de consola con todas las funcionalidades.
 */
public class Main {

    private static final Scanner sc  = new Scanner(System.in);
    private static final BienestarService svc = new BienestarService();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA BIENESTAR UNIAJC               ║");
        System.out.println("║  Facultad de Ingeniería - UNIAJC             ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        System.out.print("\n¿Cargar datos de demostración? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            DataLoader.cargarDatos(svc);
            System.out.println("✅ Datos de demo cargados.\n");
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerInt();
            procesarOpcion(opcion);
        } while (opcion != 0);

        System.out.println("\n👋 Sistema cerrado. ¡Hasta pronto!");
    }

    // ════════════════════════════════════════════════════════════════
    //  MENÚ PRINCIPAL
    // ════════════════════════════════════════════════════════════════
    private static void mostrarMenu() {
        System.out.println("\n┌──────────────────────────────────────────────┐");
        System.out.println("│            MENÚ PRINCIPAL                    │");
        System.out.println("├──────────────────────────────────────────────┤");
        System.out.println("│  [1] Registrar estudiante                    │");
        System.out.println("│  [2] Registrar actividad                     │");
        System.out.println("│  [3] Inscribir estudiante en actividad       │");
        System.out.println("│  [4] Cancelar inscripción                    │");
        System.out.println("│  [5] Completar actividad (registrar asist.)  │");
        System.out.println("│  [6] Calificar actividad (1-5)               │");
        System.out.println("│  [7] Ver inscripciones de estudiante         │");
        System.out.println("│  [8] Listar estudiantes                      │");
        System.out.println("│  [9] Listar actividades                      │");
        System.out.println("│ [10] Generar reporte / certif. de grado      │");
        System.out.println("│ [11] Ver métricas de satisfacción (admin)    │");
        System.out.println("│  [0] Salir                                   │");
        System.out.println("└──────────────────────────────────────────────┘");
        System.out.print("  Opción: ");
    }

    // ════════════════════════════════════════════════════════════════
    //  PROCESAMIENTO DE OPCIONES
    // ════════════════════════════════════════════════════════════════
    private static void procesarOpcion(int op) {
        switch (op) {
            case 1  -> menuRegistrarEstudiante();
            case 2  -> menuRegistrarActividad();
            case 3  -> menuInscribir();
            case 4  -> menuCancelar();
            case 5  -> menuCompletar();
            case 6  -> menuCalificar();
            case 7  -> menuVerInscripciones();
            case 8  -> System.out.println(svc.listarEstudiantes());
            case 9  -> System.out.println(svc.listarActividades());
            case 10 -> menuReporte();
            case 11 -> System.out.println(svc.verMetricas());
            case 0  -> {}
            default -> System.out.println("⚠️  Opción inválida.");
        }
    }

    // ════════════════════════════════════════════════════════════════
    //  SUB-MENÚS
    // ════════════════════════════════════════════════════════════════

    private static void menuRegistrarEstudiante() {
        System.out.println("\n── REGISTRAR ESTUDIANTE ──");
        System.out.print("Código   : "); String cod    = sc.nextLine().trim();
        System.out.print("Nombre   : "); String nombre = sc.nextLine().trim();
        System.out.print("Correo   : "); String correo = sc.nextLine().trim();
        System.out.print("Promedio : "); double prom   = leerDouble();
        System.out.println(svc.registrarEstudiante(cod, nombre, correo, prom));
    }

    private static void menuRegistrarActividad() {
        System.out.println("\n── REGISTRAR ACTIVIDAD ──");
        System.out.print("ID único    : "); String id     = sc.nextLine().trim();
        System.out.print("Nombre      : "); String nombre = sc.nextLine().trim();
        System.out.println("Categoría (1=DEPORTE 2=CULTURA 3=SALUD): ");
        int catNum = leerInt();
        Categoria cat = switch (catNum) {
            case 1 -> Categoria.DEPORTE;
            case 2 -> Categoria.CULTURA;
            case 3 -> Categoria.SALUD;
            default -> Categoria.SALUD;
        };
        System.out.print("Horas       : "); double horas  = leerDouble();
        System.out.print("Cupo máximo : "); int    cupo   = leerInt();
        System.out.print("Día (ej. LUNES): "); String dia = sc.nextLine().trim();
        System.out.print("Hora inicio (ej. 8 para 08:00): "); int ini = leerInt();
        System.out.print("Hora fin    : ");                    int fin = leerInt();
        System.out.print("¿Alto impacto? (s/n): "); boolean alto = sc.nextLine().trim().equalsIgnoreCase("s");
        System.out.println(svc.registrarActividad(id, nombre, cat, horas, cupo, dia, ini, fin, alto));
    }

    private static void menuInscribir() {
        System.out.println("\n── INSCRIBIR EN ACTIVIDAD ──");
        System.out.print("Código estudiante : "); String cod = sc.nextLine().trim();
        System.out.print("ID actividad      : "); String id  = sc.nextLine().trim();
        System.out.println(svc.inscribir(cod, id));
    }

    private static void menuCancelar() {
        System.out.println("\n── CANCELAR INSCRIPCIÓN ──");
        System.out.print("Código estudiante : "); String cod = sc.nextLine().trim();
        System.out.print("ID actividad      : "); String id  = sc.nextLine().trim();
        System.out.println(svc.cancelarInscripcion(cod, id));
    }

    private static void menuCompletar() {
        System.out.println("\n── REGISTRAR ASISTENCIA / COMPLETAR ──");
        System.out.print("Código estudiante : "); String cod = sc.nextLine().trim();
        System.out.print("ID actividad      : "); String id  = sc.nextLine().trim();
        System.out.println(svc.completarActividad(cod, id));
    }

    private static void menuCalificar() {
        System.out.println("\n── CALIFICAR ACTIVIDAD ──");
        System.out.print("Código estudiante : "); String cod  = sc.nextLine().trim();
        System.out.print("ID actividad      : "); String id   = sc.nextLine().trim();
        System.out.print("Nota (1-5)        : "); double nota = leerDouble();
        System.out.println(svc.calificarActividad(cod, id, nota));
    }

    private static void menuVerInscripciones() {
        System.out.println("\n── INSCRIPCIONES DEL ESTUDIANTE ──");
        System.out.print("Código estudiante : "); String cod = sc.nextLine().trim();
        System.out.println(svc.listarInscripcionesEstudiante(cod));
    }

    private static void menuReporte() {
        System.out.println("\n── REPORTE DE GRADO ──");
        System.out.print("Código estudiante : "); String cod = sc.nextLine().trim();
        System.out.println(svc.generarReporte(cod));
    }

    // ════════════════════════════════════════════════════════════════
    //  Utilidades de lectura segura
    // ════════════════════════════════════════════════════════════════

    private static int leerInt() {
        try {
            int n = Integer.parseInt(sc.nextLine().trim());
            return n;
        } catch (NumberFormatException e) {
            System.out.println("⚠️  Entrada inválida. Se usará 0.");
            return 0;
        }
    }

    private static double leerDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("⚠️  Entrada inválida. Se usará 0.0.");
            return 0.0;
        }
    }
}
