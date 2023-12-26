package br.com.fiap.PosTech_SpringBlog.model;

import lombok.Data;

@Data       //anotação para o Lombok
public class ArtigoStatusCount {
    private Integer status;
    private Long quantidade;
}
