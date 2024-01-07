package br.com.fiap.PosTech_SpringBlog.model;

import lombok.Data;

@Data       //anotação para o Lombok
public class ArtigoComAutorRequest {
    private Artigo artigo;
    private Autor autor;
}
