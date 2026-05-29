package bienestar.model;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {

    private String codigo;
    private String nombre;
    private String correo;
    private double promedio;
    private double horasDeporte;
    private double horasCultura;
    private double horasSalud;
    private double penalizaciones;
    private List<Inscripcion> inscripciones;

    // ─── Constantes de negocio ───────────────────────────────────────────────
    public static final double HORAS_MINIMAS_CATEGORIA = 10.0;
    public static final double PROMEDIO_MINIMO_ALTO_IMPACTO = 4.0;

    // ─── Constructor ────────────────────────────────────────────────────────
    public Estudiante(String codigo, String nombre, String correo, double promedio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.correo = correo;
        this.promedio = promedio;
        this.horasDeporte = 0;
        this.horasCultura = 0;
        this.horasSalud = 0;
        this.penalizaciones = 0;
        this.inscripciones = new ArrayList<>();
    }

    // ─── Lógica de horas ────────────────────────────────────────────────────

    /** Suma horas según la categoría de la actividad aprobada. */
    public void acumularHoras(Actividad actividad) {
        double horas = actividad.getHoras();
        switch (actividad.getCategoria()) {
            case DEPORTE -> horasDeporte += horas;
            case CULTURA -> horasCultura += horas;
            case SALUD   -> horasSalud   += horas;
        }
    }

    /** Aplica penalización de 2 horas al total general (se resta del área correspondiente). */
    public void aplicarPenalizacion(Actividad actividad) {
        penalizaciones += 2.0;
        switch (actividad.getCategoria()) {
            case DEPORTE -> horasDeporte = Math.max(0, horasDeporte - 2);
            case CULTURA -> horasCultura = Math.max(0, horasCultura - 2);
            case SALUD   -> horasSalud   = Math.max(0, horasSalud   - 2);
        }
    }

    /** Bono de 5 horas al completar ciclo de bienestar en una categoría. */
    public void aplicarBono(Actividad.Categoria categoria) {
        switch (categoria) {
            case DEPORTE -> horasDeporte += 5;
            case CULTURA -> horasCultura += 5;
            case SALUD   -> horasSalud   += 5;
        }
    }

    public double getTotalHoras() {
        return horasDeporte + horasCultura + horasSalud;
    }

    public boolean cumpleRequisitos() {
        return horasDeporte >= HORAS_MINIMAS_CATEGORIA
            && horasCultura >= HORAS_MINIMAS_CATEGORIA
            && horasSalud   >= HORAS_MINIMAS_CATEGORIA;
    }

    /** Verifica si el estudiante tiene conflicto de horario con otra actividad. */
    public boolean tieneConflictoHorario(Actividad nueva) {
        for (Inscripcion ins : inscripciones) {
            if (ins.getEstado() == Inscripcion.Estado.INSCRITO) {
                Actividad act = ins.getActividad();
                if (act.getDia().equalsIgnoreCase(nueva.getDia())) {
                    // Solapamiento: (inicioA < finB) && (inicioB < finA)
                    if (act.getHoraInicio() < nueva.getHoraFin()
                     && nueva.getHoraInicio() < act.getHoraFin()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ─── Getters & Setters ──────────────────────────────────────────────────

    public String getCodigo()           { return codigo; }
    public String getNombre()           { return nombre; }
    public String getCorreo()           { return correo; }
    public double getPromedio()         { return promedio; }
    public double getHorasDeporte()     { return horasDeporte; }
    public double getHorasCultura()     { return horasCultura; }
    public double getHorasSalud()       { return horasSalud; }
    public double getPenalizaciones()   { return penalizaciones; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }

    public void setCodigo(String codigo)     { this.codigo = codigo; }
    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setCorreo(String correo)     { this.correo = correo; }
    public void setPromedio(double promedio) { this.promedio = promedio; }

    @Override
    public String toString() {
        return String.format("Estudiante[%s | %s | Promedio: %.1f | Horas: D=%.0f C=%.0f S=%.0f]",
                codigo, nombre, promedio, horasDeporte, horasCultura, horasSalud);
    }
}
