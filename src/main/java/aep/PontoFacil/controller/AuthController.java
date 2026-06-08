package aep.PontoFacil.controller;

import aep.PontoFacil.model.UsuarioModel;
import aep.PontoFacil.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/login")
    public String autenticar(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            RedirectAttributes redirect) {

        UsuarioModel usuario = usuarioService.autenticar(email, senha);

        if (usuario == null) {
            redirect.addFlashAttribute("erro", "E-mail ou senha inválidos.");
            return "redirect:/login";
        }

        session.setAttribute("usuario", usuario);
        return "redirect:/perfil";
    }

    @PostMapping("/cadastro")
    public String registrar(
            @RequestParam String nome,
            @RequestParam LocalDate dataNascimento,
            @RequestParam String email,
            @RequestParam String cpf,
            @RequestParam String telefone,
            @RequestParam String senha,
            RedirectAttributes redirect) {

        try {
            usuarioService.cadastrar(nome, dataNascimento, email, cpf, telefone, senha);
            redirect.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
