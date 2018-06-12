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
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;



@Entity
public class Produto {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

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

}