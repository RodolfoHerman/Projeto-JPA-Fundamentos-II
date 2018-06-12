package br.com.rodolfo.loja.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.rodolfo.loja.modals.Loja;
import br.com.rodolfo.loja.modals.Produto;

@Repository
public class ProdutoDao {
    
    @PersistenceContext
    private EntityManager em;

    public List<Produto> getProdutos() {
        
        TypedQuery<Produto> query = em.createQuery("FROM Produto", Produto.class);

        return query.getResultList();
    }

    public Produto getProduto(Integer id) {
        
        return em.find(Produto.class, id);
    }


    //O problema de seu utilizar a JPQL é a validação dos parâmetros e a montagem através da concatenação 
    public List<Produto> getProdutosJPQL(String nome, Integer categoriaId, Integer lojaId) {

        String jpql = "SELECT p FROM Produto p ";

        if(categoriaId != null) {

            jpql += "JOIN FETCH p.categorias c WHERE c.id = :pCategoriaId AND ";
        } else {

            jpql += "WHERE ";
        }

        if(lojaId != null) {

            jpql += "p.loja.id = :pLojaId AND ";
        }

        //Por padrão o Spring coloca uma String como vazia caso seja null
        if(!nome.isEmpty()) {

            jpql += "p.nome LIKE :pNome AND ";
        }

        //Necessário para tratar o 'AND' no final das concatenações acima
        jpql += "1 = 1";

        TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
        
        if(categoriaId != null) {

            query.setParameter("pCategoriaId", categoriaId);
        }

        if(lojaId != null) {

            query.setParameter("pLojaId", lojaId);
        }

        if(!nome.isEmpty()) {

            query.setParameter("pNome", nome);
        }


        return query.getResultList();
    }


    //Construção de queries dinâmicas com Criteria Hibernate
    //A JPA é gerenciada pelo SPRING quando chamarmos o método 'unwrap' ele exige que já exista um EntityManager ativo e para isso anotamos o método como @Transacional dizendo que dentro de todo o método deverá ter uma transação ativa e portanto o entityManager 
    @Transactional
    public List<Produto> getProdutosHibernate(String nome, Integer categoriaId, Integer lojaId) {

        //A 'Session' é equivalente a uma instância do 'entityManager'
        Session session = em.unwrap(Session.class);

        //Ao invés de criar um objeto do tipo 'Query' criamos um do tipo 'Criteria' passando em qual tabela ele irá buscar os resultados
        Criteria criteria = session.createCriteria(Produto.class);

        //A definição acima já é suficiente para retornar todos os produtos basta chamar '(List<Produto>)criteria.list()'

        if (!nome.isEmpty()) {
            
            //A classe 'Restrictions' adiciona os filtros
            criteria.add(Restrictions.like("nome", "%" + nome + "%"));
        }

        if (lojaId != null) {
            
            criteria.add(Restrictions.like("loja.id", lojaId));
        }

        if (categoriaId != null) {
            
            criteria.setFetchMode("categorias", FetchMode.JOIN)
                .createAlias("categorias", "c")
                .add(Restrictions.like("c.id", categoriaId));
        }

        //O método 'list' retorna o resultado
        return (List<Produto>) criteria.list();
    }



    //Construção de queries dinâmicas com Criteria JPA
    public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {

        //solicita ao JPA o builder do 'criteria' fazendo com que o programador não se preocupe com o banco de dados que está trabalhando (MySQL, PostgreSQL)
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        //O Hibernate possui o 'CriteriaQuery' que seria análogo ao 'TypedQuery'
        CriteriaQuery<Produto> query = criteriaBuilder.createQuery(Produto.class);

        //O método 'from(Produto.class)' é o from do SQL, esse trecho seria "SELECT m FROM Produto m". Aqui irá retornar uma lista de todos os produtos. Para construir o filtro precisamos pegar as referências dos atributos, para isso o JPA retorna uma instância da interface Root. Através dessa interface que é possível traçar os caminhos, 'path', para cada atributo. Ou seja, para navegar até os atributos de uma entidade
        Root<Produto> root = query.from(Produto.class);

        //Path<String> nomePath = root.get("nome")

        //Como os atributos possui tipos utilizamos os generics para tipar de acordo com cada atributo
        Path<String> nomePath = root.<String>get("nome");

        //Selecionar o obketo 'loja' e seu id igual na JPQL -> p.loja.id = :pIdLoja
        Path<Integer> lojaPath = root.<Loja>get("loja").<Integer>get("id");

        //O JPA precisa realizar um JOIN na tabela/objeto 'categorias' e pega o 'id' da categoria
        Path<Integer> categoriaPath = root.join("categorias").<Integer>get("id");

        //Array de filtros
        List<Predicate> predicates = new ArrayList<>();

        //Query mais avançada utilizando conjunção
        Predicate conjuncao = criteriaBuilder.conjunction();

        //Tem que verificar se os filtros foram passados pelo usuário
        if (!nome.isEmpty()) {

            //Montando a query de forma dinâmica. A JPA chama o equals, like, and, between, or ... de predicados ('Predicate')
			Predicate nomeIgual = criteriaBuilder.like(nomePath, "%" + nome + "%");
            predicates.add(nomeIgual);
            
            //Utilizar a conjunção em vez do ArrayList
            conjuncao = criteriaBuilder.and(nomeIgual);
		}
		if (categoriaId != null) {
			Predicate categoriaIgual = criteriaBuilder.equal(categoriaPath, categoriaId);
            predicates.add(categoriaIgual);
            
            //Utilizar a conjunção em vez do ArrayList
            conjuncao = criteriaBuilder.and(conjuncao, categoriaIgual);
		}
		if (lojaId != null) {
			Predicate lojaIgual = criteriaBuilder.equal(lojaPath, lojaId);
            predicates.add(lojaIgual);
            
            //Utilizar a conjunção em vez do ArrayList
            conjuncao = criteriaBuilder.and(conjuncao, lojaIgual);
		}

        //Colocar a clausula WHERE na query
        //query.where((Predicate[]) predicates.toArray(new Predicate[0]));

        //Utilizar a conjuncao
        query.where(conjuncao);

        TypedQuery<Produto> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }


    public void insere(Produto produto) {
        
        if(produto.getId() == null) {

            em.persist(produto);
        } else {

            em.merge(produto);
        }
    }

}