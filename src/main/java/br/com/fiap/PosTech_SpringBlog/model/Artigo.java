package br.com.fiap.PosTech_SpringBlog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document   //anotação para o MongoDB
@Data       //anotação para o Lombok
public class Artigo {
    @Id
    private String codigo;
    private String titulo;
    private LocalDateTime data;
    private String texto;
    private String url;
    private Integer status;
}
