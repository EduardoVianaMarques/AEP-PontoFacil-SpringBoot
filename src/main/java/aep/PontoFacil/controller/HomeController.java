package aep.PontoFacil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/como-funciona")
    public String comoFunciona() {
        return "como-funciona";
    }

    @GetMapping("/servicos")
    public String servicos() {
        return "servicos";
    }

    @GetMapping("/contato")
    public String contato() {
        return "contato";
    }
}
