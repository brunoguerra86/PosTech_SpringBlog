package br.com.fiap.PosTech_SpringBlog.repository;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {

}
