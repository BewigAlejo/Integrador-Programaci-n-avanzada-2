package com.mycompany.integradorpa2.logica;

import com.mycompany.integradorpa2.logica.enums.ResultadoSeguimiento;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;

    @Enumerated(EnumType.STRING)
    private ResultadoSeguimiento resultado;

    @Basic
    private String observacion;

    // relaciones
    @ManyToOne
    @JoinColumn(name = "voluntario_id")
    private Voluntario voluntario;

    @ManyToOne
    @JoinColumn(name = "adopcion_id")
    private Adopcion adopcion;

    // --- constructores ---
    public Seguimiento() { }

    public Seguimiento(Long id, Date fechaHora, ResultadoSeguimiento resultado,
                       String observacion, Voluntario voluntario, Adopcion adopcion) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.resultado = resultado;
        this.observacion = observacion;
        this.voluntario = voluntario;
        this.adopcion = adopcion;
    }

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public ResultadoSeguimiento getResultado() { return resultado; }
    public void setResultado(ResultadoSeguimiento resultado) { this.resultado = resultado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public Voluntario getVoluntario() { return voluntario; }
    public void setVoluntario(Voluntario voluntario) { this.voluntario = voluntario; }

    public Adopcion getAdopcion() { return adopcion; }
    public void setAdopcion(Adopcion adopcion) { this.adopcion = adopcion; }
}
