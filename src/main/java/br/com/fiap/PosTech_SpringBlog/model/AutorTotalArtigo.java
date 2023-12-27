package br.com.fiap.PosTech_SpringBlog.model;

import lombok.Data;

@Data   //anotação para o Lombok
public class AutorTotalArtigo {
    private Autor autor;
    private Long totalArtigos;
}
