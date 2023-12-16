package br.com.fiap.PosTech_SpringBlog.controller;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @GetMapping
    public List<Artigo> obterTodos(){
        return null;
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo){
        return null;
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo ){
        return null;
    }
}
