package bienestar.service;

import bienestar.model.Actividad;
import bienestar.model.Actividad.Categoria;
import bienestar.model.Estudiante;
import bienestar.model.Inscripcion;
import bienestar.model.Inscripcion.Estado;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Capa de servicio: centraliza toda la lógica de negocio del sistema Bienestar UNIAJC.
 * Todas las operaciones retornan un String de resultado para mostrarlo en consola.
 */
public class BienestarService {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<Actividad>  actividades = new ArrayList<>();

    // ════════════════════════════════════════════════════════════════
    //  CRUD básico
    // ════════════════════════════════════════════════════════════════

    public String registrarEstudiante(String codigo, String nombre, String correo, double promedio) {
        if (buscarEstudiante(codigo).isPresent())
            return "❌ Ya existe un estudiante con el código " + codigo;
        estudiantes.add(new Estudiante(codigo, nombre, correo, promedio));
        return "✅ Estudiante registrado: " + nombre;
    }

    public String registrarActividad(String id, String nombre, Categoria cat,
                                     double horas, int cupo, String dia,
                                     int inicio, int fin, boolean altoImpacto) {
        if (buscarActividad(id).isPresent())
            return "❌ Ya existe una actividad con ID " + id;
        actividades.add(new Actividad(id, nombre, cat, horas, cupo, dia, inicio, fin, altoImpacto));
        return "✅ Actividad registrada: " + nombre;
    }

    // ════════════════════════════════════════════════════════════════
    //  RF-3 · RF-4 · RF-5 · RF-8  INSCRIPCIÓN
    // ════════════════════════════════════════════════════════════════

    /**
     * Intenta inscribir a un estudiante en una actividad.
     * Valida: promedio (RF-8), conflicto horario (RF-5), cupo (RF-3).
     * Si no hay cupo, agrega a lista de espera (RF-4).
     */
    public String inscribir(String codEstudiante, String idActividad) {
        Optional<Estudiante> oe = buscarEstudiante(codEstudiante);
        Optional<Actividad>  oa = buscarActividad(idActividad);

        if (oe.isEmpty()) return "❌ Estudiante no encontrado.";
        if (oa.isEmpty()) return "❌ Actividad no encontrada.";

        Estudiante e = oe.get();
        Actividad  a = oa.get();

        // RF-8: restricción por promedio
        if (a.isAltoImpacto() && e.getPromedio() < Estudiante.PROMEDIO_MINIMO_ALTO_IMPACTO)
            return "❌ Promedio insuficiente (" + e.getPromedio() + "). Necesitas ≥ 4.0 para esta actividad.";

        // Verificar que no esté ya inscrito
        boolean yaInscrito = e.getInscripciones().stream()
                .anyMatch(i -> i.getActividad().getId().equals(idActividad)
                            && i.getEstado() == Estado.INSCRITO);
        if (yaInscrito) return "❌ Ya estás inscrito en esta actividad.";

        // RF-5: conflicto de horario
        if (e.tieneConflictoHorario(a))
            return "❌ Conflicto de horario con otra actividad inscrita.";

        // RF-3 + RF-4: cupo
        if (!a.hayCupo()) {
            if (a.estaEnEspera(e)) return "⏳ Ya estás en lista de espera para esta actividad.";
            a.agregarAListaEspera(e);
            Inscripcion ins = new Inscripcion(e, a, Estado.EN_ESPERA);
            e.getInscripciones().add(ins);
            return "⏳ Cupo lleno. Agregado a lista de espera para: " + a.getNombre();
        }

        // Inscripción normal
        Inscripcion ins = new Inscripcion(e, a);
        e.getInscripciones().add(ins);
        a.incrementarCupo();
        return "✅ Inscripción exitosa en: " + a.getNombre();
    }

    // ════════════════════════════════════════════════════════════════
    //  RF-6  CANCELACIÓN (con penalización automática)
    // ════════════════════════════════════════════════════════════════

    public String cancelarInscripcion(String codEstudiante, String idActividad) {
        Optional<Estudiante> oe = buscarEstudiante(codEstudiante);
        if (oe.isEmpty()) return "❌ Estudiante no encontrado.";

        Estudiante e = oe.get();
        Optional<Inscripcion> oi = e.getInscripciones().stream()
                .filter(i -> i.getActividad().getId().equals(idActividad)
                          && i.getEstado() == Estado.INSCRITO)
                .findFirst();

        if (oi.isEmpty()) return "❌ No tienes una inscripción activa en esa actividad.";

        Inscripcion ins = oi.get();
        boolean penalizado = ins.cancelar(); // lógica de penalización dentro de Inscripcion

        StringBuilder sb = new StringBuilder("✅ Inscripción cancelada en: " + ins.getActividad().getNombre());
        if (penalizado)
            sb.append("\n⚠️  Penalización aplicada: -2 horas en la categoría ").append(ins.getActividad().getCategoria());

        // RF-4: asignar siguiente en lista de espera
        Actividad a = ins.getActividad();
        Estudiante siguiente = a.siguienteEnEspera();
        if (siguiente != null) {
            // Cambiar su inscripción de EN_ESPERA a INSCRITO
            siguiente.getInscripciones().stream()
                    .filter(i -> i.getActividad().getId().equals(idActividad)
                              && i.getEstado() == Estado.EN_ESPERA)
                    .findFirst()
                    .ifPresent(i -> i.setEstado(Estado.INSCRITO));
            a.incrementarCupo();
            sb.append("\n🔔 Cupo asignado automáticamente a: ").append(siguiente.getNombre());
        }
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════
    //  COMPLETAR actividad  →  RF-7 (bono de rendimiento)
    // ════════════════════════════════════════════════════════════════

    public String completarActividad(String codEstudiante, String idActividad) {
        Optional<Estudiante> oe = buscarEstudiante(codEstudiante);
        if (oe.isEmpty()) return "❌ Estudiante no encontrado.";

        Estudiante e = oe.get();
        Optional<Inscripcion> oi = e.getInscripciones().stream()
                .filter(i -> i.getActividad().getId().equals(idActividad)
                          && i.getEstado() == Estado.INSCRITO)
                .findFirst();

        if (oi.isEmpty()) return "❌ No tienes inscripción activa en esa actividad.";

        Inscripcion ins = oi.get();
        ins.completar(); // acumula horas

        StringBuilder sb = new StringBuilder("✅ Actividad completada: " + ins.getActividad().getNombre());
        sb.append(String.format("\n   +%.0f horas en %s", ins.getActividad().getHoras(), ins.getActividad().getCategoria()));

        // RF-7: revisar si completó ciclo de bienestar (3 actividades completadas de la misma categoría)
        Categoria cat = ins.getActividad().getCategoria();
        long completadasEnCategoria = e.getInscripciones().stream()
                .filter(i -> i.getEstado() == Estado.COMPLETADO
                          && i.getActividad().getCategoria() == cat)
                .count();

        if (completadasEnCategoria % 3 == 0) { // cada múltiplo de 3 otorga bono
            e.aplicarBono(cat);
            sb.append("\n🏅 ¡Ciclo de bienestar completado en ").append(cat).append("! +5 horas bonus.");
        }
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════
    //  RF-9  CALIFICACIÓN DE ACTIVIDAD
    // ════════════════════════════════════════════════════════════════

    public String calificarActividad(String codEstudiante, String idActividad, double nota) {
        Optional<Estudiante> oe = buscarEstudiante(codEstudiante);
        Optional<Actividad>  oa = buscarActividad(idActividad);

        if (oe.isEmpty()) return "❌ Estudiante no encontrado.";
        if (oa.isEmpty()) return "❌ Actividad no encontrada.";
        if (nota < 1 || nota > 5) return "❌ La calificación debe estar entre 1 y 5.";

        Estudiante e = oe.get();
        boolean completoActividad = e.getInscripciones().stream()
                .anyMatch(i -> i.getActividad().getId().equals(idActividad)
                            && i.getEstado() == Estado.COMPLETADO);

        if (!completoActividad) return "❌ Solo puedes calificar actividades que hayas completado.";

        oa.get().agregarCalificacion(nota);
        return String.format("⭐ Calificación %.1f registrada para: %s", nota, oa.get().getNombre());
    }

    // ════════════════════════════════════════════════════════════════
    //  RF-10  REPORTE / CERTIFICACIÓN DE GRADO
    // ════════════════════════════════════════════════════════════════

    public String generarReporte(String codEstudiante) {
        Optional<Estudiante> oe = buscarEstudiante(codEstudiante);
        if (oe.isEmpty()) return "❌ Estudiante no encontrado.";

        Estudiante e = oe.get();
        String separador = "═".repeat(54);

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(separador).append("\n");
        sb.append("   CERTIFICADO DE BIENESTAR UNIAJC\n");
        sb.append(separador).append("\n");
        sb.append(String.format("  Estudiante : %s\n", e.getNombre()));
        sb.append(String.format("  Código     : %s\n", e.getCodigo()));
        sb.append(String.format("  Promedio   : %.2f\n", e.getPromedio()));
        sb.append(separador).append("\n");
        sb.append(String.format("  Horas Deporte : %6.1f / %.0f\n",
                e.getHorasDeporte(), Estudiante.HORAS_MINIMAS_CATEGORIA));
        sb.append(String.format("  Horas Cultura : %6.1f / %.0f\n",
                e.getHorasCultura(), Estudiante.HORAS_MINIMAS_CATEGORIA));
        sb.append(String.format("  Horas Salud   : %6.1f / %.0f\n",
                e.getHorasSalud(),   Estudiante.HORAS_MINIMAS_CATEGORIA));
        sb.append(String.format("  Total horas   : %6.1f\n", e.getTotalHoras()));
        sb.append(String.format("  Penalizaciones: %6.1f horas\n", e.getPenalizaciones()));
        sb.append(separador).append("\n");

        // Historial de inscripciones
        sb.append("  HISTORIAL DE ACTIVIDADES:\n");
        if (e.getInscripciones().isEmpty()) {
            sb.append("  (sin actividades)\n");
        } else {
            for (Inscripcion ins : e.getInscripciones()) {
                sb.append(String.format("    • %-25s [%s]\n",
                        ins.getActividad().getNombre(), ins.getEstado()));
            }
        }
        sb.append(separador).append("\n");

        // Veredicto
        String veredicto = e.cumpleRequisitos() ? "✅  APTO PARA GRADO" : "❌  NO APTO PARA GRADO";
        sb.append("  ESTADO: ").append(veredicto).append("\n");
        if (!e.cumpleRequisitos()) {
            if (e.getHorasDeporte() < Estudiante.HORAS_MINIMAS_CATEGORIA)
                sb.append("  ⚠️  Faltan horas en DEPORTE\n");
            if (e.getHorasCultura() < Estudiante.HORAS_MINIMAS_CATEGORIA)
                sb.append("  ⚠️  Faltan horas en CULTURA\n");
            if (e.getHorasSalud() < Estudiante.HORAS_MINIMAS_CATEGORIA)
                sb.append("  ⚠️  Faltan horas en SALUD\n");
        }
        sb.append(separador).append("\n");
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════
    //  MÉTRICAS ADMINISTRADOR  (RF-9 - promedio satisfacción)
    // ════════════════════════════════════════════════════════════════

    public String verMetricas() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n═══ MÉTRICAS DE SATISFACCIÓN ═══\n");
        for (Actividad a : actividades) {
            sb.append(String.format("  %-25s → Promedio: %.2f / 5.0  (%d calificaciones)\n",
                    a.getNombre(), a.getPromedioSatisfaccion(),
                    (int) a.getPromedioSatisfaccion())); // solo indicativo
        }
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════
    //  LISTADOS DE CONSULTA
    // ════════════════════════════════════════════════════════════════

    public String listarEstudiantes() {
        if (estudiantes.isEmpty()) return "  (No hay estudiantes registrados)";
        StringBuilder sb = new StringBuilder();
        estudiantes.forEach(e -> sb.append("  ").append(e).append("\n"));
        return sb.toString();
    }

    public String listarActividades() {
        if (actividades.isEmpty()) return "  (No hay actividades registradas)";
        StringBuilder sb = new StringBuilder();
        actividades.forEach(a -> sb.append("  ").append(a).append("\n"));
        return sb.toString();
    }

    public String listarInscripcionesEstudiante(String codEstudiante) {
        Optional<Estudiante> oe = buscarEstudiante(codEstudiante);
        if (oe.isEmpty()) return "❌ Estudiante no encontrado.";
        Estudiante e = oe.get();
        if (e.getInscripciones().isEmpty()) return "  (Sin inscripciones)";
        StringBuilder sb = new StringBuilder();
        e.getInscripciones().forEach(i -> sb.append("  ").append(i).append("\n"));
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════
    //  Métodos auxiliares de búsqueda
    // ════════════════════════════════════════════════════════════════

    public Optional<Estudiante> buscarEstudiante(String codigo) {
        return estudiantes.stream()
                .filter(e -> e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    public Optional<Actividad> buscarActividad(String id) {
        return actividades.stream()
                .filter(a -> a.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public List<Estudiante> getEstudiantes() { return estudiantes; }
    public List<Actividad>  getActividades()  { return actividades; }
}
