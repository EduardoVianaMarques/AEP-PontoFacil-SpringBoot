package aep.PontoFacil.controller;

import aep.PontoFacil.model.SolicitacaoModel;
import aep.PontoFacil.service.SolicitacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoApiController {

    private final SolicitacaoService service;

    public SolicitacaoApiController(SolicitacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<SolicitacaoModel> listar() {
        return service.listar();
    }

    @GetMapping("/{protocolo}")
    public SolicitacaoModel buscar(@PathVariable String protocolo) {
        return service.buscar(protocolo);
    }
}
