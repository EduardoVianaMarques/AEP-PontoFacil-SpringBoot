package aep.PontoFacil.service;

import aep.PontoFacil.model.UsuarioModel;
import aep.PontoFacil.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final UsuarioRepository repository;
    private final Path diretorioFotos;

    public UsuarioServiceImpl(UsuarioRepository repository) throws IOException {
        this.repository = repository;
        this.diretorioFotos =
                Paths.get("uploads", "fotos").toAbsolutePath().normalize();

        Files.createDirectories(diretorioFotos);
    }

    @Override
    public UsuarioModel cadastrar(String nome,
                                  LocalDate dataNascimento,
                                  String email,
                                  String cpf,
                                  String telefone,
                                  String senha) {

        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(nome);
        usuario.setDataNascimento(dataNascimento);
        usuario.setEmail(email);
        usuario.setCpf(cpf.replaceAll("\\D", ""));
        usuario.setTelefone(telefone.replaceAll("\\D", ""));
        usuario.setSenha(senha);

        return repository.save(usuario);
    }

    @Override
    public UsuarioModel autenticar(String email, String senha) {
        return repository.findByEmail(email)
                .filter(u -> u.getSenha().equals(senha))
                .orElse(null);
    }

    @Override
    public UsuarioModel atualizarFoto(Long usuarioId, MultipartFile foto) {

        if (foto == null || foto.isEmpty()) {
            throw new RuntimeException("Selecione uma imagem.");
        }

        if (!TIPOS_PERMITIDOS.contains(foto.getContentType())) {
            throw new RuntimeException("Formato inválido.");
        }

        UsuarioModel usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        String extensao = switch (foto.getContentType()) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> ".jpg";
        };

        String nomeArquivo =
                usuarioId + "_" + UUID.randomUUID().toString().substring(0, 8)
                        + extensao;

        Path destino = diretorioFotos.resolve(nomeArquivo);

        try {
            if (usuario.getFotoPath() != null) {
                Path antiga = Paths.get("uploads")
                        .resolve(usuario.getFotoPath())
                        .toAbsolutePath()
                        .normalize();

                Files.deleteIfExists(antiga);
            }

            Files.write(destino,
                    foto.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar foto.");
        }

        usuario.setFotoPath("fotos/" + nomeArquivo);

        return repository.save(usuario);
    }

    @Override
    public UsuarioModel atualizarDados(Long usuarioId,
                                       String telefone,
                                       String endereco,
                                       String numero,
                                       String cidade,
                                       String cep) {

        UsuarioModel usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuario.setTelefone(telefone);
        usuario.setEndereco(endereco);
        usuario.setNumero(numero);
        usuario.setCidade(cidade);
        usuario.setCep(cep);

        return repository.save(usuario);
    }
}