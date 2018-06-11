package br.com.rodolfo.loja.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.rodolfo.loja.modals.Categoria;

@Repository
public class CategoriaDao {

    @PersistenceContext
    private EntityManager em;

    public List<Categoria> getCategorias() {
        
        TypedQuery<Categoria> query = em.createQuery("FROM Categoria", Categoria.class);
        
        return query.getResultList();
    }

}