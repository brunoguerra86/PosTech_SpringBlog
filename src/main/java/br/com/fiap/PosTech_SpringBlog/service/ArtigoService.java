package br.com.fiap.PosTech_SpringBlog.service;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import br.com.fiap.PosTech_SpringBlog.model.ArtigoStatusCount;
import br.com.fiap.PosTech_SpringBlog.model.Autor;
import br.com.fiap.PosTech_SpringBlog.model.AutorTotalArtigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {
    public List<Artigo> obterTodos();
    public Artigo obterPorCodigo(String codigo);
//    public Artigo criar(Artigo artigo);
//    public ResponseEntity<?> criar(Artigo artigo);
    public ResponseEntity<?> criarArtigoComAutor(Artigo artigo, Autor autor);
    public List<Artigo> findByDataGreaterThan(LocalDateTime data);
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);
    public void atualizar(Artigo updateArtigo);
    public void atualizarArtigo(String id, String novaURL);
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo);
    public void deleteById(String id);
    public void deleteArtigoById(String id);
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo);
    Page<Artigo> obterArtigosPaginados(Pageable pageable);
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status);
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status);
    public List<Artigo> findByTexto(String termoPesquisa);
    public List<ArtigoStatusCount> contarArtigosPorStatus();
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim);
}
