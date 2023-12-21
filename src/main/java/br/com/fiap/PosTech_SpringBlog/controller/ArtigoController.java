package br.com.fiap.PosTech_SpringBlog.controller;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import br.com.fiap.PosTech_SpringBlog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo ){
        return this.artigoService.criar(artigo);
    }
    @GetMapping
    public List<Artigo> obterTodos(){
        return this.artigoService.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo){
        return this.artigoService.obterPorCodigo(codigo);
    }

    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(@RequestParam("data")LocalDateTime data){
        return this.artigoService.findByDataGreaterThan(data);
    }

    @GetMapping("/datastatus")
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status){
        return this.artigoService.findByDataAndStatus(data, status);
    }

    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThan(
            @RequestParam("status") Integer status,
            @RequestParam("data") LocalDateTime data)
    {
        return this.artigoService.findByStatusAndDataGreaterThan(status, data);
    }

    @GetMapping("/periodo")
    public List<Artigo> obterArtigoPorDataHora(
            @RequestParam("de") LocalDateTime de,
            @RequestParam("ate") LocalDateTime ate)
    {
        return this.artigoService.obterArtigoPorDataHora(de, ate);
    }

    @GetMapping("/artigos-complexo")
    public List<Artigo> encontrarArtigosComplexos(@RequestParam("status") Integer status,
                                                  @RequestParam("data") LocalDateTime data,
                                                  @RequestParam("titulo") String titulo) {
        return this.artigoService.encontrarArtigosComplexos(status, data, titulo);
    }

    @GetMapping("/artigos-paginado")
    public ResponseEntity<Page<Artigo>> obterArtigosPaginados(Pageable pageable){
        Page<Artigo> artigos = this.artigoService.obterArtigosPaginados(pageable);
        return ResponseEntity.ok(artigos);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo artigo){
        this.artigoService.atualizar(artigo);
    }

    @PutMapping("/{id}")
    public void atualizarArtigo(@PathVariable String id, @RequestBody String novaURL){
        this.artigoService.atualizarArtigo(id, novaURL);
    }

    @DeleteMapping("/{id}")
    public void deleteArtigo(@PathVariable String id){
        this.artigoService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public void deleteArtigoById(@RequestParam("Id") String id){
        this.artigoService.deleteArtigoById(id);
    }

}
