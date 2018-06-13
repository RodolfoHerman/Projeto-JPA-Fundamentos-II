package br.com.rodolfo.loja.modals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicUpdate;

//Grafos de relacionamento : Com esse recurso podemos dizer à JPA quais relacionamentos queremos trazer nas "queries", evitando o LAZY LOADING
@NamedEntityGraphs(
    @NamedEntityGraph(name="produtoComCategoria",
                      attributeNodes={
                          @NamedAttributeNode("categorias")
                      })
)
@Entity
//A anotação DynamicUpdate realiza o update apenas do campo que sofreu alteração e não de todos os campos
@DynamicUpdate
//Utilizando cache de segundo nível. A estratégia 'NONSTRICT_READ_WRITE' informar que não há problemas em ler dados inconsistentes do cache. Aqui está sendo utilizado o 'EhCache'
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) 
public class Produto {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    //A anotação 'Version' faz um lock otimista. O lock otimista permite que os conflitos ocorram, a ideia é que o banco/aplicação nunca trave o acesso ao registro. Ou seja, se duas pessoas tentarem atualizar o mesmo registro a primeira conseguirá e a segunda será impedida
    @Version
    private int versao;

    @NotEmpty   
    private String nome;

    @NotEmpty
    private String linkDaFoto;

    @NotEmpty
    @Column(columnDefinition="TEXT")
    private String descricao;

    @Min(20)
    private Double preco;

    @Valid
    @ManyToOne
    private Loja loja;

    //Constroio uma tabela associativa entre 'Produto' e 'Categoria'
    @ManyToMany
    @JoinTable(name="PRODUTO_CATEGORIA")
    ////@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) 
    private List<Categoria> categorias = new ArrayList<>();

    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return String return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return String return the linkDaFoto
     */
    public String getLinkDaFoto() {
        return linkDaFoto;
    }

    /**
     * @param linkDaFoto the linkDaFoto to set
     */
    public void setLinkDaFoto(String linkDaFoto) {
        this.linkDaFoto = linkDaFoto;
    }

    /**
     * @return String return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return Double return the preco
     */
    public Double getPreco() {
        return preco;
    }

    /**
     * @param preco the preco to set
     */
    public void setPreco(Double preco) {
        this.preco = preco;
    }

    /**
     * @return Loja return the loja
     */
    public Loja getLoja() {
        return loja;
    }

    /**
     * @param loja the loja to set
     */
    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    //método auxiliar para associar categorias com o produto
	//se funcionar apos ter definido o relacionamento entre produto e categoria
	public void adicionarCategorias(Categoria... categorias) {
		for (Categoria categoria : categorias) {
			this.categorias.add(categoria);
		}
	}


    /**
     * @return List<Categoria> return the categorias
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * @param categorias the categorias to set
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }


    /**
     * @return int return the versao
     */
    public int getVersao() {
        return versao;
    }

    /**
     * @param versao the versao to set
     */
    public void setVersao(int versao) {
        this.versao = versao;
    }

}