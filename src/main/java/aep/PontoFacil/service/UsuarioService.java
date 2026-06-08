package aep.PontoFacil.service;

import aep.PontoFacil.model.UsuarioModel;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface UsuarioService {

    UsuarioModel cadastrar(String nome,
                           LocalDate dataNascimento,
                           String email,
                           String cpf,
                           String telefone,
                           String senha);

    UsuarioModel autenticar(String email, String senha);

    UsuarioModel atualizarFoto(Long usuarioId, MultipartFile foto);

    UsuarioModel atualizarDados(Long usuarioId,
                                String telefone,
                                String endereco,
                                String numero,
                                String cidade,
                                String cep);
}