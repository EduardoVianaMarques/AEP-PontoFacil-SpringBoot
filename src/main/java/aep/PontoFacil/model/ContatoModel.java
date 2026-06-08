package aep.PontoFacil.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contatos")
public class ContatoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String assunto;

    @Column(length = 2000)
    private String mensagem;

    private LocalDateTime dataEnvio;

    public ContatoModel() {
    }

    public ContatoModel(String nome, String email, String assunto, String mensagem) {
        this.nome = nome;
        this.email = email;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.dataEnvio = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }
}
