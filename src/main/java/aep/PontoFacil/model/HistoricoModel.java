package aep.PontoFacil.model;

import aep.PontoFacil.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoricoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String responsavel;

    private String comentario;

    public HistoricoModel() {
    }

    public HistoricoModel(Status status,
                          String responsavel,
                          String comentario) {

        this.data = LocalDateTime.now();
        this.status = status;
        this.responsavel = responsavel;
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getComentario() {
        return comentario;
    }
}