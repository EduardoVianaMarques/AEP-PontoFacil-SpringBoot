package aep.PontoFacil.controller;

import aep.PontoFacil.service.ContatoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping("/contato")
    public String enviarContato(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String assunto,
            @RequestParam String mensagem,
            RedirectAttributes redirect) {

        try {
            contatoService.salvar(nome, email, assunto, mensagem);
            redirect.addFlashAttribute(
                    "sucesso",
                    "Mensagem enviada com sucesso, " + nome + "! Retornaremos em breve.");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/contato";
    }
}
