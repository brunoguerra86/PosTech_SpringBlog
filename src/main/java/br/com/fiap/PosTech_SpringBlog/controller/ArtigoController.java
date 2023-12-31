package br.com.fiap.PosTech_SpringBlog.controller;

import br.com.fiap.PosTech_SpringBlog.model.*;
import br.com.fiap.PosTech_SpringBlog.service.ArtigoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailureException( OptimisticLockingFailureException ex ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro de concorrência: o Artigo foi atualizado por outro usuário. Por favor, tente novamente.");
    }

//    @PostMapping
//    public Artigo criar(@RequestBody Artigo artigo ){
//        return this.artigoService.criar(artigo);
//    }

//    @PostMapping
//    public ResponseEntity<?> criar(@RequestBody Artigo artigo){
//        return this.artigoService.criar(artigo);
//    }

    @PostMapping
    public ResponseEntity<?> criarArtigoComAutor(@RequestBody ArtigoComAutorRequest request){
        Artigo artigo = request.getArtigo();
        Autor autor = request.getAutor();
        return this.artigoService.criarArtigoComAutor(artigo, autor);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo artigo){
        this.artigoService.atualizar(artigo);
    }

    @PutMapping("/{id}")
    public void atualizarArtigo(@PathVariable String id, @RequestBody String novaURL){
        this.artigoService.atualizarArtigo(id, novaURL);
    }

    @PutMapping("/{id}/atualizarArtigo")
    public ResponseEntity<?> atualizarArtigo(@PathVariable("id") String id,
                                             @Valid @RequestBody Artigo artigo) {
        return this.artigoService.atualizarArtigo(id, artigo);
    }

    @DeleteMapping("/{id}")
    public void deleteArtigo(@PathVariable String id){
        this.artigoService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public void deleteArtigoById(@RequestParam("Id") String id){
        this.artigoService.deleteArtigoById(id);
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

    @GetMapping("/status-ordenado")
    public List<Artigo> findByStatusOrderByTituloAsc(@RequestParam("status") Integer status){
        return this.artigoService.findByStatusOrderByTituloAsc(status);
    }

    @GetMapping("/status-ordenado-query")
    public List<Artigo> obterArtigoPorStatusComOrdenacao(@RequestParam("status") Integer status){
        return this.artigoService.obterArtigoPorStatusComOrdenacao(status);
    }

    @GetMapping("/buscatermo")
    public List<Artigo> findByTexto(@RequestParam("termo") String termo){
        return this.artigoService.findByTexto(termo);
    }

    @GetMapping("/contarartigos")
    public List<ArtigoStatusCount> contarArtigosPorStatus(){
        return this.artigoService.contarArtigosPorStatus();
    }

    @GetMapping("/contarartigosporautor")
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(
            @RequestParam("dataInicio")LocalDate dataInicio,
            @RequestParam("dataFim")LocalDate dataFim) {
        return this.artigoService.calcularTotalArtigosPorAutorNoPeriodo(dataInicio, dataFim);
    }

}
