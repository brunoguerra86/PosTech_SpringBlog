package br.com.fiap.PosTech_SpringBlog.service;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;

import java.util.List;

public interface ArtigoService {
    public List<Artigo> obterTodos();
    public Artigo obterPorCodigo(String codigo);
    public Artigo criar(Artigo artigo);
}
