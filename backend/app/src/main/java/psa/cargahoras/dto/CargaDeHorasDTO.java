package psa.cargahoras.dto;

public class CargaDeHorasDTO {

    private String idCarga;
    private String nombreTarea;
    private double cantidadHoras;
    private String fechaCarga;
    private String tareaId;
    private String recursoId;

    public CargaDeHorasDTO(
        String idCarga,
        String nombreTarea,
        double cantidadHoras,
        String fechaCarga,
        String tareaId,
        String recursoId
    ) {
        this.idCarga = idCarga;
        this.nombreTarea = nombreTarea;
        this.cantidadHoras = cantidadHoras;
        this.fechaCarga = fechaCarga;
        this.tareaId = tareaId;
        this.recursoId = recursoId;
    }

    public String recursoId() {
        return recursoId;
    }

    public void setRecursoId(String recursoId) {
        this.recursoId = recursoId;
    }

    public String getTareaId() {
        return tareaId;
    }

    public void setTareaId(String tareaId) {
        this.tareaId = tareaId;
    }

    public String getIdCarga() {
        return idCarga;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public double getCantidadHoras() {
        return cantidadHoras;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }
}
