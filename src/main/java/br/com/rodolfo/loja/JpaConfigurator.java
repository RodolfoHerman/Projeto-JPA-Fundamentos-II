package br.com.rodolfo.loja;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JpaConfigurator {
    
	//O SPRING BOOT realiza a configuração no arquivo properties.
	//O atributo 'destroyMethod' define o método 'close' do Pool que o Spring chama Quando o TOMCAT é desligado, assim garantimos que todas as conexões serão fechadas corretamente
    // @Bean(destroyMethod="close")
	// public DataSource getDataSource() {
	//	   //O DriverManagerDataSource cria uma nova conexão a cada uso o que leva a não ser uma boa solução
	//     //DriverManagerDataSource dataSource = new DriverManagerDataSource();
	//	   //Abrir conexão com pool de conexões utilizando o C3P0
	//	   ComboPooledDataSource dataSource = new ComboPooledDataSource();

	//     dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	//     dataSource.setUrl("jdbc:mysql://127.0.0.1/lista");
	//     dataSource.setUsername("root");
	//     dataSource.setPassword("");

	//	   //Criar conexões deixando-as disponíveis
	//	   dataSource.setMinPoolSize(5);
	//	   //Limitando o número de conexões
	//	   dataSource.setMaxPoolSize(9);
	//	   //O C3P0 solicita a configuração de número de threads auxiliares para distribuir o processamento porque o JDBC possui alguns processamentos lentos e fazendo isso ganha-se performance
	//	   dataSource.setNumHelperThreads(5)

	//	   //Para evitar que o Pool conteha conexões que podem quebrar com o tempo (ex.: o banco de dados cai e retorna quebrando as conexões do pool) existe o método 'setIdleConnectionTestPeriod' que elimina as conexões que ficam ociosas por muito tempo no pool elimando o risco de escolher uma conexão quebrada.
	//	   dataSource.setIdleConnectionTestPeriod(1); //a cada um segundo testamos as conexões ociosas

	//     return dataSource;
	// }

	// @Bean
	// public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource) {
	// 	LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

	// 	entityManagerFactory.setPackagesToScan("br.com.caelum");
	// 	entityManagerFactory.setDataSource(dataSource);

	// 	entityManagerFactory
	// 			.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

	// 	Properties props = new Properties();

	// 	props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
	// 	props.setProperty("hibernate.show_sql", "true");
	// 	props.setProperty("hibernate.hbm2ddl.auto", "create-drop");

	// 	entityManagerFactory.setJpaProperties(props);
	// 	return entityManagerFactory;
	// }
    
    
    @Bean
    public JpaTransactionManager getTransactionManager(EntityManagerFactory emf) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }


}