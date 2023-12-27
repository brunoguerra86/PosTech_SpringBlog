package br.com.fiap.PosTech_SpringBlog.service.impl;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import br.com.fiap.PosTech_SpringBlog.model.ArtigoStatusCount;
import br.com.fiap.PosTech_SpringBlog.model.Autor;
import br.com.fiap.PosTech_SpringBlog.model.AutorTotalArtigo;
import br.com.fiap.PosTech_SpringBlog.repository.ArtigoRepository;
import br.com.fiap.PosTech_SpringBlog.repository.AutorRepository;
import br.com.fiap.PosTech_SpringBlog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public Artigo criar(Artigo artigo) {

        //Se o autor existe
        if(artigo.getAutor().getCodigo() != null){

            //recuperar o autor
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente!"));

            //define o autor no artigo
            artigo.setAutor(autor);
        } else {

            //Caso contrário, gravar o artigo sem autor
            artigo.setAutor(null);
        }

        //Salvo o artigo com o autor já cadastrado
        return this.artigoRepository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        Query query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(Criteria
                .where("data").is(data)
                .and("status").is(status));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public void atualizar(Artigo updateArtigo) {
        this.artigoRepository.save(updateArtigo);
    }

    @Override
    public void atualizarArtigo(String id, String novaURL) {
        Query query = new Query(Criteria
                .where("_id").is(id));
        Update update = new Update().set("url", novaURL);
        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public void deleteById(String id){
        this.artigoRepository.deleteById(id);
    }

    @Override
    public void deleteArtigoById(String id) {
        Query query = new Query(Criteria
                .where("_id").is(id));
        this.mongoTemplate.remove(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return this.artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return this.artigoRepository.obterArtigoPorDataHora(de, ate);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {
        Criteria criteria = new Criteria();

        // Filtrar artigos com data menor ou igual ao valor fornecido
        criteria.and("data").lte(data);

        // Filtrar artigos com o status especificado
        if(status != null){
            criteria.and("status").is(status);
        }

        // Filtrar artigos cujo titulo exista
        if(titulo != null && !titulo.isEmpty()){
            criteria.and("titulo").regex(titulo, "i");
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Page<Artigo> obterArtigosPaginados(Pageable pageable) {
        Sort sort = Sort.by("titulo").ascending();
        Pageable paginacao =
                PageRequest.of(pageable.getPageNumber(),
                               pageable.getPageSize(),
                               sort);
        return this.artigoRepository.findAll(paginacao);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return this.artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status) {
        return this.artigoRepository.obterArtigoPorStatusComOrdenacao(status);
    }

    @Override
    public List<Artigo> findByTexto(String termoPesquisa) {
        TextCriteria criteria =
                TextCriteria.forDefaultLanguage().matchingPhrase(termoPesquisa);
        Query query = TextQuery.queryText(criteria).sortByScore();
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        TypedAggregation<Artigo> aggregation =
                Aggregation.newAggregation(
                        Artigo.class,
                        Aggregation.group("status").count().as("quantidade"),
                        Aggregation.project("quantidade").and("status")
                                .previousOperation()
                );

        AggregationResults<ArtigoStatusCount> result = mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);

        return result.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        TypedAggregation<Artigo> aggregation =
                Aggregation.newAggregation(
                        Artigo.class,
                        Aggregation.match(
                                Criteria.where("data")
                                        .gte(dataInicio.atStartOfDay())
                                        .lt(dataFim.plusDays(1).atStartOfDay())
                        ),
                        Aggregation.group("autor").count().as("totalArtigos"),
                        Aggregation.project("totalArtigos").and("autor")
                                .previousOperation()
                );

        AggregationResults<AutorTotalArtigo> results =
                mongoTemplate.aggregate(aggregation, AutorTotalArtigo.class);

        return results.getMappedResults();
    }
}
