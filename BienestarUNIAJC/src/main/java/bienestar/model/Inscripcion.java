package bienestar.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Inscripcion {

    public enum Estado { INSCRITO, CANCELADO, COMPLETADO, EN_ESPERA }

    private Estudiante estudiante;
    private Actividad actividad;
    private Estado estado;
    private LocalDateTime fechaInscripcion;
    private LocalDateTime fechaCancelacion;
    private boolean penalizado;

    // Horas mínimas de anticipación para cancelar sin penalización
    public static final int HORAS_ANTICIPACION_MINIMA = 24;

    // ─── Constructor ────────────────────────────────────────────────────────
    public Inscripcion(Estudiante estudiante, Actividad actividad) {
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.estado = Estado.INSCRITO;
        this.fechaInscripcion = LocalDateTime.now();
        this.penalizado = false;
    }

    public Inscripcion(Estudiante estudiante, Actividad actividad, Estado estado) {
        this(estudiante, actividad);
        this.estado = estado;
    }

    // ─── Lógica de cancelación ───────────────────────────────────────────────

    /**
     * Cancela la inscripción. Si han pasado menos de HORAS_ANTICIPACION_MINIMA
     * horas desde que se inscribió, se aplica penalización automática.
     * (En producción la fecha de la actividad sería el referente;
     *  aquí usamos la diferencia desde inscripción para la demo.)
     */
    public boolean cancelar() {
        if (estado != Estado.INSCRITO) return false;

        long horasTranscurridas = ChronoUnit.HOURS.between(fechaInscripcion, LocalDateTime.now());

        estado = Estado.CANCELADO;
        fechaCancelacion = LocalDateTime.now();

        // Cancelación con poco tiempo → penalización
        if (horasTranscurridas < HORAS_ANTICIPACION_MINIMA) {
            estudiante.aplicarPenalizacion(actividad);
            penalizado = true;
        }

        actividad.decrementarCupo();
        return penalizado;
    }

    /** Marca la inscripción como completada y acumula horas al estudiante. */
    public void completar() {
        if (estado == Estado.INSCRITO) {
            estado = Estado.COMPLETADO;
            estudiante.acumularHoras(actividad);
        }
    }

    // ─── Getters ─────────────────────────────────────────────────────────────
    public Estudiante getEstudiante()          { return estudiante; }
    public Actividad getActividad()            { return actividad; }
    public Estado getEstado()                  { return estado; }
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
    public LocalDateTime getFechaCancelacion() { return fechaCancelacion; }
    public boolean isPenalizado()              { return penalizado; }

    public void setEstado(Estado estado)       { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("Inscripcion[%s → %s | Estado: %s | Penalizado: %s]",
                estudiante.getNombre(), actividad.getNombre(), estado, penalizado ? "SÍ" : "NO");
    }
}
