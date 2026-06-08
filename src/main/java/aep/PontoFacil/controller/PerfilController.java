package aep.PontoFacil.controller;

import aep.PontoFacil.enums.Status;
import aep.PontoFacil.model.SolicitacaoModel;
import aep.PontoFacil.model.UsuarioModel;
import aep.PontoFacil.service.SolicitacaoService;
import aep.PontoFacil.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;
    private final SolicitacaoService solicitacaoService;

    public PerfilController(UsuarioService usuarioService,
                            SolicitacaoService solicitacaoService) {
        this.usuarioService = usuarioService;
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {

        if (!estaLogado(session)) {
            return "redirect:/login";
        }

        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");

        adicionarEstatisticas(model, usuario);

        return "perfil";
    }

    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(
            @RequestParam String telefone,
            @RequestParam String endereco,
            @RequestParam String numero,
            @RequestParam String cidade,
            @RequestParam String cep,
            HttpSession session,
            RedirectAttributes redirect) {

        if (!estaLogado(session)) {
            return "redirect:/login";
        }

        try {
            UsuarioModel usuario =
                    (UsuarioModel) session.getAttribute("usuario");

            UsuarioModel atualizado = usuarioService.atualizarDados(
                    usuario.getId(),
                    telefone,
                    endereco,
                    numero,
                    cidade,
                    cep
            );

            session.setAttribute("usuario", atualizado);

            redirect.addFlashAttribute("sucesso",
                    "Dados atualizados com sucesso!");

        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/perfil";
    }

    @PostMapping("/perfil/foto")
    public String atualizarFoto(@RequestParam("foto") MultipartFile foto,
                                HttpSession session,
                                RedirectAttributes redirect) {

        if (!estaLogado(session)) {
            return "redirect:/login";
        }

        try {
            UsuarioModel usuario =
                    (UsuarioModel) session.getAttribute("usuario");

            UsuarioModel atualizado =
                    usuarioService.atualizarFoto(usuario.getId(), foto);

            session.setAttribute("usuario", atualizado);

            redirect.addFlashAttribute("sucesso",
                    "Foto atualizada com sucesso!");

        } catch (RuntimeException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/perfil";
    }

    private void adicionarEstatisticas(Model model, UsuarioModel usuario) {

        List<SolicitacaoModel> solicitacoes = solicitacaoService.listar().stream()
                .filter(s -> !s.isAnonimo()
                        && usuario.getNome().equals(s.getSolicitante()))
                .toList();

        model.addAttribute("usuario", usuario);
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("totalRegistradas", solicitacoes.size());
        model.addAttribute("totalAndamento",
                contarPorStatus(solicitacoes, Status.EM_ANDAMENTO));
        model.addAttribute("totalFinalizadas",
                contarPorStatus(solicitacoes, Status.FINALIZADO));
    }

    private boolean estaLogado(HttpSession session) {
        return session.getAttribute("usuario") != null;
    }

    private long contarPorStatus(List<SolicitacaoModel> solicitacoes,
                                 Status status) {
        return solicitacoes.stream()
                .filter(s -> s.getStatus() == status)
                .count();
    }
}