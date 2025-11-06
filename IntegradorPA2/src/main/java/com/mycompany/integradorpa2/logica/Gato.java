package com.mycompany.integradorpa2.logica;

import com.mycompany.integradorpa2.logica.enums.EstadoSalud;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Gato {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    @Basic
    private String nombre;
    private String raza;
    private Integer edad;
    private String foto;
    private String qr;            // código QR asignado
    private boolean adoptado;
    
    @Enumerated(EnumType.STRING)
    private EstadoSalud estadoDeSalud;

    // relaciones
    // Muchos gatos pertenecen a una zona → relación N:1
    @ManyToOne
    @JoinColumn(name = "zona_id") // FK en la tabla gato
    private Zona zona;
    
    // Un gato tiene un solo historial médico → relación 1:1
    @OneToOne(mappedBy = "gato", cascade = CascadeType.ALL)
    private HistorialMedico historialMedico;
    
    
    // Un gato puede tener muchas tareas → relación 1:N
    @OneToMany(mappedBy = "gato", cascade = CascadeType.ALL)
    private List<Tarea> tareas;
    
    
    // Un gato puede tener una adopción actual → relación 1:1
    @OneToOne(mappedBy = "gato")
    private Adopcion adopcionActual;   // opcional

    public Gato(Long id, String nombre, String raza, Integer edad, String foto, EstadoSalud estadoDeSalud, String qr, boolean adoptado, Zona zona, HistorialMedico historialMedico, List<Tarea> tareas, Adopcion adopcionActual) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.foto = foto;
        this.estadoDeSalud = estadoDeSalud;
        this.qr = qr;
        this.adoptado = adoptado;
        this.zona = zona;
        this.historialMedico = historialMedico;
        this.tareas = tareas;
        this.adopcionActual = adopcionActual;
    }

    public Gato() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public EstadoSalud getEstadoDeSalud() {
        return estadoDeSalud;
    }

    public void setEstadoDeSalud(EstadoSalud estadoDeSalud) {
        this.estadoDeSalud = estadoDeSalud;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public boolean isAdoptado() {
        return adoptado;
    }

    public void setAdoptado(boolean adoptado) {
        this.adoptado = adoptado;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(HistorialMedico historialMedico) {
        this.historialMedico = historialMedico;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Adopcion getAdopcionActual() {
        return adopcionActual;
    }

    public void setAdopcionActual(Adopcion adopcionActual) {
        this.adopcionActual = adopcionActual;
    }
    
    
}
