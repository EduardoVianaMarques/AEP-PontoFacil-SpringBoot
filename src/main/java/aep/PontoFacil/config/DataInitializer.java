package aep.PontoFacil.config;

import aep.PontoFacil.enums.Categoria;
import aep.PontoFacil.service.SolicitacaoService;
import aep.PontoFacil.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UsuarioService usuarioService,
            SolicitacaoService solicitacaoService) {

        return args -> {
            if (usuarioService.autenticar("demo@pontofacil.com", "123456") == null) {
                usuarioService.cadastrar(
                        "João Silva",
                        LocalDate.of(1995, 3, 15),
                        "demo@pontofacil.com",
                        "123.456.789-00",
                        "(44) 99999-0000",
                        "123456");

                solicitacaoService.criarSolicitacao(
                        Categoria.INFRAESTRUTURA_PUBLICA,
                        "Buraco grande na via pública causando risco aos motoristas.",
                        "Av. Brasil, 1500",
                        false,
                        "João Silva");

                solicitacaoService.criarSolicitacao(
                        Categoria.LIMPEZA_URBANA,
                        "Acúmulo de lixo na esquina há mais de uma semana.",
                        "Rua das Flores, 220",
                        false,
                        "João Silva");
            }
        };
    }
}
