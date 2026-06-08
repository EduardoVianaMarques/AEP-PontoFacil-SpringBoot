package aep.PontoFacil.controller;

import aep.PontoFacil.enums.Categoria;
import aep.PontoFacil.enums.Status;
import aep.PontoFacil.model.SolicitacaoModel;
import aep.PontoFacil.model.UsuarioModel;
import aep.PontoFacil.service.SolicitacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping("/nova-solicitacao")
    public String novaSolicitacao(HttpSession session) {
        if (!estaLogado(session)) {
            return "redirect:/login";
        }
        return "nova-solicitacao";
    }

    @PostMapping("/solicitacoes/salvar")
    public String salvar(
            @RequestParam Categoria categoria,
            @RequestParam String descricao,
            @RequestParam String localizacao,
            @RequestParam String numero,
            @RequestParam(defaultValue = "true") boolean anonimo,
            HttpSession session,
            RedirectAttributes redirect) {

        if (!estaLogado(session)) {
            return "redirect:/login";
        }

        try {
            UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
            String localizacaoCompleta = localizacao + ", " + numero;
            String solicitante = anonimo ? "ANONIMO" : usuario.getNome();

            String protocolo = solicitacaoService.criarSolicitacao(
                    categoria,
                    descricao,
                    localizacaoCompleta,
                    anonimo,
                    solicitante);

            redirect.addFlashAttribute(
                    "sucesso",
                    "Solicitação registrada! Seu protocolo é: " + protocolo);
            return "redirect:/acompanhar?protocolo=" + protocolo;
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/nova-solicitacao";
        }
    }

    @GetMapping("/acompanhar")
    public String acompanhar(
            @RequestParam(required = false) String protocolo,
            HttpSession session,
            Model model) {

        if (!estaLogado(session)) {
            return "redirect:/login";
        }

        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        List<SolicitacaoModel> solicitacoes = solicitacaoService.listar().stream()
                .filter(s -> pertenceAoUsuario(s, usuario))
                .toList();

        model.addAttribute("totalRegistradas", solicitacoes.size());
        model.addAttribute("totalAndamento", contarPorStatus(solicitacoes, Status.EM_ANDAMENTO));
        model.addAttribute("totalFinalizadas", contarPorStatus(solicitacoes, Status.FINALIZADO));

        if (protocolo != null && !protocolo.isBlank()) {
            SolicitacaoModel solicitacao = solicitacaoService.buscar(protocolo.trim());

            if (solicitacao != null) {
                model.addAttribute("solicitacao", solicitacao);
                model.addAttribute("progresso", calcularProgresso(solicitacao.getStatus()));

                if (!solicitacao.getHistorico().isEmpty()) {
                    model.addAttribute(
                            "dataAbertura",
                            solicitacao.getHistorico().get(0).getData());
                    model.addAttribute(
                            "ultimaObservacao",
                            solicitacao.getHistorico()
                                    .get(solicitacao.getHistorico().size() - 1)
                                    .getComentario());
                }
            } else {
                model.addAttribute("erro", "Protocolo não encontrado.");
            }
        }

        return "acompanhar-solicitacao";
    }

    private boolean estaLogado(HttpSession session) {
        return session.getAttribute("usuario") != null;
    }

    private boolean pertenceAoUsuario(SolicitacaoModel solicitacao, UsuarioModel usuario) {
        if (solicitacao.isAnonimo()) {
            return false;
        }
        return usuario.getNome().equals(solicitacao.getSolicitante());
    }

    private long contarPorStatus(List<SolicitacaoModel> solicitacoes, Status status) {
        return solicitacoes.stream()
                .filter(s -> s.getStatus() == status)
                .count();
    }

    private int calcularProgresso(Status status) {
        return switch (status) {
            case ABERTO -> 25;
            case EM_ANDAMENTO -> 70;
            case FINALIZADO -> 100;
        };
    }
}
