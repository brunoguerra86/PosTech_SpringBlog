package br.com.fiap.PosTech_SpringBlog.repository;

import br.com.fiap.PosTech_SpringBlog.model.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AutorRepository extends MongoRepository<Autor, String> {

}
