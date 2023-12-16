package br.com.fiap.PosTech_SpringBlog.controller;

import br.com.fiap.PosTech_SpringBlog.model.Autor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/autores")
public class AutorController {

    @PostMapping
    public Autor criar(@RequestBody Autor autor){
        return null;
    }

    @GetMapping("/{codigo}")
    public Autor obterPorCodigo(@PathVariable String codigo){
        return null;
    }
}
