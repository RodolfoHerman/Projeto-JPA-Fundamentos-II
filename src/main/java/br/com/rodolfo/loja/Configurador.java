package br.com.rodolfo.loja;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import br.com.rodolfo.loja.dao.CategoriaDao;
import br.com.rodolfo.loja.dao.LojaDao;
import br.com.rodolfo.loja.dao.ProdutoDao;
import br.com.rodolfo.loja.modals.Categoria;
import br.com.rodolfo.loja.modals.Loja;
import br.com.rodolfo.loja.modals.Produto;

@Configuration
@EnableWebMvc
@ComponentScan("br.com.rodolfo")
@EnableTransactionManagement
public class Configurador extends WebMvcConfigurerAdapter {
	
	@Bean
	@Scope("request")
	public List<Produto> produtos(ProdutoDao produtoDao) {
		List<Produto> produtos = produtoDao.getProdutos();
		
		return produtos;
	}
	
	@Bean
	public List<Categoria> categorias(CategoriaDao categoriaDao) { 
		List<Categoria> categorias = categoriaDao.getCategorias();
		
		return categorias;
	}
	
	@Bean
	public List<Loja> lojas(LojaDao lojaDao) { 
		List<Loja> lojas = lojaDao.getLojas();
		
		return lojas;
	}
	
	@Bean
	public MessageSource messageSource() { 
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		
		messageSource.setBasename("/WEB-INF/messages");
		messageSource.setCacheSeconds(1);
		messageSource.setDefaultEncoding("ISO-8859-1");
		
		return messageSource;
		
	}


	//O SPRING MVC necessita configurar a EntityManager para que não feche durante um 'request' e sim o feche ao final do 'request' em relacionamentos LAZY LOADING.
	//Essa configuração é conhecida como  padrão 'OpenEntityManagerInView'. O método 'OpenEntityManagerInViewInterceptor' permte abrir o EntityManager apenas ao chamar algum método do Controller, evitando problemas de EntityManager desnecessários. Também chamado de 'Interceptiors'
	
	//Responsável por gerar uma instância do interceptor que será registrado.
	// @Bean
	// public OpenEntityManagerInViewInterceptor getOpenEntityManagerInViewInterceptor() { 
	// 	return new OpenEntityManagerInViewInterceptor();
	// }

	//Esse método, nos fornece um registro InterceptorRegistry que usamos para adicionar os interceptors que serão usados no SpringMVC
	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// 	registry.addWebRequestInterceptor(getOpenEntityManagerInViewInterceptor());
	// }

	
	@Bean 
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		
		viewResolver.setExposeContextBeansAsAttributes(true);

		return viewResolver;
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new Converter<String, Categoria>() {

			@Override
			public Categoria convert(String categoriaId) {
				Categoria categoria = new Categoria();
				categoria.setId(Integer.valueOf(categoriaId));
				
				return categoria;
			}
			
		});
	}
	
}