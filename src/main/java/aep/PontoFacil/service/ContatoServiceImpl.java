package aep.PontoFacil.service;

import aep.PontoFacil.model.ContatoModel;
import aep.PontoFacil.repository.ContatoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContatoServiceImpl implements ContatoService {

    private final ContatoRepository repository;

    public ContatoServiceImpl(ContatoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContatoModel salvar(String nome, String email, String assunto, String mensagem) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Informe seu nome.");
        }
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Informe seu e-mail.");
        }
        if (mensagem == null || mensagem.isBlank()) {
            throw new RuntimeException("Informe sua mensagem.");
        }

        return repository.save(new ContatoModel(nome, email, assunto, mensagem));
    }
}
