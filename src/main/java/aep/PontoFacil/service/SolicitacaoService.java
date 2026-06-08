package aep.PontoFacil.service;

import aep.PontoFacil.enums.Categoria;
import aep.PontoFacil.model.SolicitacaoModel;

import java.util.List;

public interface SolicitacaoService {

    String criarSolicitacao(
            Categoria categoria,
            String descricao,
            String localizacao,
            boolean anonimo,
            String solicitante);

    List<SolicitacaoModel> listar();

    SolicitacaoModel buscar(String protocolo);

    void atualizarStatus(
            String protocolo,
            String status,
            String responsavel,
            String comentario);
}