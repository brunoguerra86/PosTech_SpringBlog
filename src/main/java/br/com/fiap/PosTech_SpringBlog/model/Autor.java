package br.com.fiap.PosTech_SpringBlog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document   //anotação para o MongoDB
@Data       //anotação para o Lombok
public class Autor {
    @Id
    private String codigo;
    private String nome;
    private String biografia;
    private String imagem;
}
