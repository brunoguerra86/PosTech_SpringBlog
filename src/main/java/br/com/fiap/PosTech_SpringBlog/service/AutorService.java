package br.com.fiap.PosTech_SpringBlog.service;

import br.com.fiap.PosTech_SpringBlog.model.Autor;

public interface AutorService {
    public Autor criar(Autor autor);
    public Autor obterPorCodigo(String codigo);
}
