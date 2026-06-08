package aep.PontoFacil.service;

import aep.PontoFacil.enums.*;
import aep.PontoFacil.model.*;
import aep.PontoFacil.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final SolicitacaoRepository repository;

    public SolicitacaoServiceImpl(
            SolicitacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public String criarSolicitacao(Categoria categoria,
                                   String descricao,
                                   String localizacao,
                                   boolean anonimo,
                                   String solicitante) {

        if (descricao == null || descricao.length() < 10) {
            throw new RuntimeException(
                    "Descrição deve possuir pelo menos 10 caracteres.");
        }

        if (localizacao == null || localizacao.isBlank()) {
            throw new RuntimeException(
                    "Informe a localização.");
        }

        Prioridade prioridade;

        switch (categoria) {
            case SAUDE_PUBLICA, SEGURANCA_PUBLICA ->
                    prioridade = Prioridade.ALTA;

            case INFRAESTRUTURA_PUBLICA, TRANSPORTE_PUBLICO ->
                    prioridade = Prioridade.MEDIA;

            default ->
                    prioridade = Prioridade.BAIXA;
        }

        String protocolo =
                "PF-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        SolicitacaoModel solicitacao =
                new SolicitacaoModel(
                        protocolo,
                        categoria,
                        descricao,
                        localizacao,
                        anonimo,
                        solicitante,
                        prioridade
                );

        repository.save(solicitacao);

        return protocolo;
    }

    @Override
    public List<SolicitacaoModel> listar() {

        List<SolicitacaoModel> lista =
                repository.findAll();

        lista.sort(
                Comparator.comparing(
                        SolicitacaoModel::getPrioridade
                )
        );

        return lista;
    }

    @Override
    public SolicitacaoModel buscar(String protocolo) {
        return repository.findById(protocolo)
                .orElse(null);
    }

    @Override
    public void atualizarStatus(String protocolo,
                                String statusStr,
                                String responsavel,
                                String comentario) {

        SolicitacaoModel solicitacao =
                buscar(protocolo);

        if (solicitacao == null) {
            throw new RuntimeException(
                    "Protocolo não encontrado.");
        }

        Status status =
                Status.valueOf(statusStr);

        solicitacao.setStatus(status);

        if (solicitacao.isAnonimo()) {
            responsavel = "ANONIMO";
        }

        solicitacao.getHistorico().add(
                new HistoricoModel(
                        status,
                        responsavel,
                        comentario
                )
        );

        repository.save(solicitacao);
    }
}