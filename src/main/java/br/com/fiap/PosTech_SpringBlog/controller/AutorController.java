package br.com.fiap.PosTech_SpringBlog.controller;

import br.com.fiap.PosTech_SpringBlog.model.Autor;
import br.com.fiap.PosTech_SpringBlog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public Autor criar(@RequestBody Autor autor){
        return this.autorService.criar(autor);
    }

    @GetMapping("/{codigo}")
    public Autor obterPorCodigo(@PathVariable String codigo){
        return this.autorService.obterPorCodigo(codigo);
    }
}
