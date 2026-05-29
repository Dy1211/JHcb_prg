package bienestar.util;

import bienestar.model.Actividad.Categoria;
import bienestar.service.BienestarService;

/**
 * Carga datos de demostración para probar el sistema sin ingresar todo manualmente.
 */
public class DataLoader {

    public static void cargarDatos(BienestarService svc) {

        // ── Estudiantes ─────────────────────────────────────────────────────
        svc.registrarEstudiante("2210001", "Ana Torres",    "ana@uniajc.edu.co",   4.2);
        svc.registrarEstudiante("2210002", "Luis Gómez",   "luis@uniajc.edu.co",  3.5);
        svc.registrarEstudiante("2210003", "María Pérez",  "maria@uniajc.edu.co", 4.7);
        svc.registrarEstudiante("2210004", "Carlos Ruiz",  "carlos@uniajc.edu.co",2.8);

        // ── Actividades ─────────────────────────────────────────────────────
        // Deporte
        svc.registrarActividad("D01","Fútbol Sala",      Categoria.DEPORTE, 4, 20,"LUNES",    8, 12, false);
        svc.registrarActividad("D02","Baloncesto",       Categoria.DEPORTE, 4,  2,"MARTES",  14, 18, false);
        svc.registrarActividad("D03","Natación",         Categoria.DEPORTE, 5, 15,"MIERCOLES",7, 12, false);
        svc.registrarActividad("D04","Atletismo",        Categoria.DEPORTE, 3, 10,"JUEVES",  16, 19, false);

        // Cultura
        svc.registrarActividad("C01","Teatro Clásico",   Categoria.CULTURA, 5, 30,"VIERNES",  9, 14, false);
        svc.registrarActividad("C02","Fotografía",       Categoria.CULTURA, 4, 20,"SABADO",  10, 14, false);
        svc.registrarActividad("C03","Viaje Cultural Cartagena", Categoria.CULTURA,10,15,"SABADO", 6, 20, true);
        svc.registrarActividad("C04","Música Andina",    Categoria.CULTURA, 3, 25,"LUNES",   14, 17, false);

        // Salud
        svc.registrarActividad("S01","Yoga",             Categoria.SALUD,   3, 20,"MIERCOLES",16,19, false);
        svc.registrarActividad("S02","Primeros Auxilios",Categoria.SALUD,   5, 30,"JUEVES",   8, 13, false);
        svc.registrarActividad("S03","Meditación",       Categoria.SALUD,   3, 20,"VIERNES",  7, 10, false);
        svc.registrarActividad("S04","Nutrición y Salud",Categoria.SALUD,   4, 15,"MARTES",   9, 13, false);
    }
}
