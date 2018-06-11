package br.com.rodolfo.loja.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.rodolfo.loja.modals.Loja;

@Repository
public class LojaDao {
 
    @PersistenceContext
    private EntityManager em;

    public List<Loja> getLojas() {

        TypedQuery<Loja> query = em.createQuery("FROM Loja", Loja.class);

        return query.getResultList();
    }

    public Loja getLoja(Integer id) {

        return em.find(Loja.class, id);
    }

}