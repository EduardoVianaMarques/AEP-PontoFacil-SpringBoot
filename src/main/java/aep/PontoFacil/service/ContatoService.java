package aep.PontoFacil.service;

import aep.PontoFacil.model.ContatoModel;

public interface ContatoService {

    ContatoModel salvar(String nome, String email, String assunto, String mensagem);
}
