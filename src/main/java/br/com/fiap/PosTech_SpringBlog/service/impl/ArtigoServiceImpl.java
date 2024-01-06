package br.com.fiap.PosTech_SpringBlog.service.impl;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import br.com.fiap.PosTech_SpringBlog.model.ArtigoStatusCount;
import br.com.fiap.PosTech_SpringBlog.model.Autor;
import br.com.fiap.PosTech_SpringBlog.model.AutorTotalArtigo;
import br.com.fiap.PosTech_SpringBlog.repository.ArtigoRepository;
import br.com.fiap.PosTech_SpringBlog.repository.AutorRepository;
import br.com.fiap.PosTech_SpringBlog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

//    @Transactional
//    @Override
//    public Artigo criar(Artigo artigo) {
//
//        //Se o autor existe
//        if(artigo.getAutor().getCodigo() != null){
//
//            //recuperar o autor
//            Autor autor = this.autorRepository
//                    .findById(artigo.getAutor().getCodigo())
//                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente!"));
//
//            //define o autor no artigo
//            artigo.setAutor(autor);
//        } else {
//
//            //Caso contrário, gravar o artigo sem autor
//            artigo.setAutor(null);
//        }
//
//        try{
//            //Salvo o artigo com o autor já cadastrado
//            return this.artigoRepository.save(artigo);
//        } catch (OptimisticLockingFailureException ex){
//
//            //1. Recuperar o documento mais recente do banco de dados (na coleção Artigo)
//            Artigo artigoAtualizado =
//                    artigoRepository.findById(artigo.getCodigo()).orElse(null);
//
//            if(artigoAtualizado != null){
//                //2. Atualizar os campos desejados
//                artigoAtualizado.setTitulo(artigo.getTitulo());
//                artigoAtualizado.setTexto(artigo.getTexto());
//                artigoAtualizado.setStatus(artigo.getStatus());
//
//                //3. Incrementar a versão manualmente do documento
//                artigoAtualizado.setVersion(artigoAtualizado.getVersion() + 1);
//
//                //4. Tentar salvar novamente
//                return this.artigoRepository.save(artigo);
//            } else {
//                throw new RuntimeException("Artigo não encontrado: " + artigo.getCodigo());
//            }
//        }
//    }


    @Override
    public ResponseEntity<?> criar(Artigo artigo) {

        if (artigo.getAutor().getCodigo() != null) {
            //recuperar o autor
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor inexistente!"));

            //define o autor no artigo
            artigo.setAutor(autor);
        } else {
            //Caso contrário, gravar o artigo sem autor
            artigo.setAutor(null);
        }

        try{
            //Salvo o artigo com o autor já cadastrado
            this.artigoRepository.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Artigo já existe na coleção.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar artigo: " + e.getMessage());
        }
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

    @Transactional
    @Override
    public void atualizar(Artigo updateArtigo) {
        this.artigoRepository.save(updateArtigo);
    }

    @Transactional
    @Override
    public void atualizarArtigo(String id, String novaURL) {
        //Critério de busca pelo "_id"
        Query query = new Query(Criteria
                .where("_id").is(id));

        //Definindo os campos que serão atualizados
        Update update = new Update().set("url", novaURL);

        //Executa a atualização
        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo) {
        try{
            Artigo artigoExistente = this.artigoRepository.findById(id).orElse(null);

            if (artigoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artigo não encontrado na coleção!");
            }

            //Atualizar dados do Artigo Existente
            artigoExistente.setTitulo(artigo.getTitulo());
            artigoExistente.setData(artigo.getData());
            artigoExistente.setTexto(artigo.getTexto());
            artigoExistente.setUrl(artigo.getUrl());
            artigoExistente.setStatus(artigo.getStatus());
            this.artigoRepository.save(artigoExistente);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar artigo: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteById(String id){
        this.artigoRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteArtigoById(String id) {
        Query query = new Query(Criteria
                .where("_id").is(id));
        this.mongoTemplate.remove(query, Artigo.class);
    }

    @Transactional
    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return this.artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Transactional
    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return this.artigoRepository.obterArtigoPorDataHora(de, ate);
    }

    @Transactional
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

    @Transactional
    @Override
    public Page<Artigo> obterArtigosPaginados(Pageable pageable) {
        Sort sort = Sort.by("titulo").ascending();
        Pageable paginacao =
                PageRequest.of(pageable.getPageNumber(),
                               pageable.getPageSize(),
                               sort);
        return this.artigoRepository.findAll(paginacao);
    }

    @Transactional
    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return this.artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Transactional
    @Override
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status) {
        return this.artigoRepository.obterArtigoPorStatusComOrdenacao(status);
    }

    @Transactional
    @Override
    public List<Artigo> findByTexto(String termoPesquisa) {
        TextCriteria criteria =
                TextCriteria.forDefaultLanguage().matchingPhrase(termoPesquisa);
        Query query = TextQuery.queryText(criteria).sortByScore();
        return mongoTemplate.find(query, Artigo.class);
    }

    @Transactional
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

    @Transactional
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
