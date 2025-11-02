package com.mycompany.integradorpa2.logica;

import com.mycompany.integradorpa2.logica.enums.EstadoTarea;
import com.mycompany.integradorpa2.logica.enums.TipoTarea;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    @Basic
    private String descripcion;
    private String observacion;
    
    @Enumerated(EnumType.STRING)
    private EstadoTarea estadoTarea;
    
    @Enumerated(EnumType.STRING)
    private TipoTarea tipoTarea;
    // relaciones
    
    @ManyToOne
    @JoinColumn(name = "voluntario_id")
    private Voluntario asignadaA;
    
    @ManyToOne
    @JoinColumn(name = "gato_id")
    private Gato gato;            // si la tarea se asocia a un gato
    
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public Tarea(Long id, String descripcion, EstadoTarea estadoTarea, TipoTarea tipoTarea, String observacion, Voluntario asignadaA, Gato gato, Date fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.estadoTarea = estadoTarea;
        this.tipoTarea = tipoTarea;
        this.observacion = observacion;
        this.asignadaA = asignadaA;
        this.gato = gato;
        this.fecha = fecha;
    }

    public Tarea() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTarea getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(EstadoTarea estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public TipoTarea getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(TipoTarea tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Voluntario getAsignadaA() {
        return asignadaA;
    }

    public void setAsignadaA(Voluntario asignadaA) {
        this.asignadaA = asignadaA;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
