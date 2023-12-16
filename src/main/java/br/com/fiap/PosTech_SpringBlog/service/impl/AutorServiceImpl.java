package br.com.fiap.PosTech_SpringBlog.service.impl;

import br.com.fiap.PosTech_SpringBlog.model.Autor;
import br.com.fiap.PosTech_SpringBlog.repository.AutorRepository;
import br.com.fiap.PosTech_SpringBlog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServiceImpl implements AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public Autor criar(Autor autor) {
        return this.autorRepository.save(autor);
    }

    @Override
    public Autor obterPorCodigo(String codigo) {
        return this.autorRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Autor não existe!"));
    }
}
