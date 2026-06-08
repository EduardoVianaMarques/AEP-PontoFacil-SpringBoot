package aep.PontoFacil.model;

import aep.PontoFacil.enums.*;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
public class SolicitacaoModel {

    @Id
    private String protocolo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String descricao;

    private String localizacao;

    private boolean anonimo;

    private String solicitante;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HistoricoModel> historico = new ArrayList<>();

    public SolicitacaoModel() {
    }

    public SolicitacaoModel(String protocolo,
                            Categoria categoria,
                            String descricao,
                            String localizacao,
                            boolean anonimo,
                            String solicitante,
                            Prioridade prioridade) {

        this.protocolo = protocolo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.anonimo = anonimo;
        this.solicitante = anonimo ? "ANONIMO" : solicitante;
        this.prioridade = prioridade;
        this.status = Status.ABERTO;

        historico.add(
                new HistoricoModel(
                        Status.ABERTO,
                        this.solicitante,
                        "Solicitação criada"
                )
        );
    }

    public String getProtocolo() {
        return protocolo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public List<HistoricoModel> getHistorico() {
        return historico;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}