package bienestar.model;

import java.util.ArrayList;
import java.util.List;

public class Actividad {

    // ─── Enumeración de categorías ──────────────────────────────────────────
    public enum Categoria { DEPORTE, CULTURA, SALUD }

    private String id;
    private String nombre;
    private Categoria categoria;
    private double horas;
    private int cupoMaximo;
    private int cupoActual;
    private String dia;          // ej. "LUNES"
    private int horaInicio;      // formato 24h, ej. 8  → 08:00
    private int horaFin;         // ej. 10 → 10:00
    private boolean altoImpacto; // requiere promedio >= 4.0
    private List<Double> calificaciones;
    private List<Estudiante> listaEspera;

    // ─── Constructor ────────────────────────────────────────────────────────
    public Actividad(String id, String nombre, Categoria categoria,
                     double horas, int cupoMaximo,
                     String dia, int horaInicio, int horaFin,
                     boolean altoImpacto) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.horas = horas;
        this.cupoMaximo = cupoMaximo;
        this.cupoActual = 0;
        this.dia = dia.toUpperCase();
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.altoImpacto = altoImpacto;
        this.calificaciones = new ArrayList<>();
        this.listaEspera = new ArrayList<>();
    }

    // ─── Gestión de cupos ───────────────────────────────────────────────────
    public boolean hayCupo()             { return cupoActual < cupoMaximo; }
    public void incrementarCupo()        { cupoActual++; }
    public void decrementarCupo()        { if (cupoActual > 0) cupoActual--; }

    public void agregarAListaEspera(Estudiante e)  { listaEspera.add(e); }
    public Estudiante siguienteEnEspera() {
        return listaEspera.isEmpty() ? null : listaEspera.remove(0);
    }
    public boolean estaEnEspera(Estudiante e) { return listaEspera.contains(e); }

    // ─── Calificaciones ─────────────────────────────────────────────────────
    public void agregarCalificacion(double nota) {
        if (nota >= 1 && nota <= 5) calificaciones.add(nota);
    }

    public double getPromedioSatisfaccion() {
        if (calificaciones.isEmpty()) return 0;
        return calificaciones.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    // ─── Getters & Setters ──────────────────────────────────────────────────
    public String getId()           { return id; }
    public String getNombre()       { return nombre; }
    public Categoria getCategoria() { return categoria; }
    public double getHoras()        { return horas; }
    public int getCupoMaximo()      { return cupoMaximo; }
    public int getCupoActual()      { return cupoActual; }
    public String getDia()          { return dia; }
    public int getHoraInicio()      { return horaInicio; }
    public int getHoraFin()         { return horaFin; }
    public boolean isAltoImpacto()  { return altoImpacto; }
    public List<Estudiante> getListaEspera() { return listaEspera; }

    public void setNombre(String nombre)         { this.nombre = nombre; }
    public void setCupoMaximo(int cupoMaximo)    { this.cupoMaximo = cupoMaximo; }
    public void setAltoImpacto(boolean v)        { this.altoImpacto = v; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %.0fh | %s %02d:00-%02d:00 | Cupo: %d/%d | Alto Impacto: %s",
                id, nombre, categoria, horas, dia, horaInicio, horaFin,
                cupoActual, cupoMaximo, altoImpacto ? "SÍ" : "NO");
    }
}
