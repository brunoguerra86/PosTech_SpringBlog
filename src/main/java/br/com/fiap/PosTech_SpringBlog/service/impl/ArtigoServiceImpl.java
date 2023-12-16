package br.com.fiap.PosTech_SpringBlog.service.impl;

import br.com.fiap.PosTech_SpringBlog.model.Artigo;
import br.com.fiap.PosTech_SpringBlog.model.Autor;
import br.com.fiap.PosTech_SpringBlog.repository.ArtigoRepository;
import br.com.fiap.PosTech_SpringBlog.repository.AutorRepository;
import br.com.fiap.PosTech_SpringBlog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public Artigo criar(Artigo artigo) {

        //Se o autor existe
        if(artigo.getAutor().getCodigo() != null){

            //recuperar o autor
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente!"));

            //define o autor no artigo
            artigo.setAutor(autor);
        } else {

            //Caso contrário, gravar o artigo sem autor
            artigo.setAutor(null);
        }

        //Salvo o artigo com o autor já cadastrado
        return this.artigoRepository.save(artigo);
    }
}
