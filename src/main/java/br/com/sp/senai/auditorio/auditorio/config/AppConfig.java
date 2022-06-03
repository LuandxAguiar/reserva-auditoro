package br.com.sp.senai.auditorio.auditorio.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.sp.senai.auditorio.auditorio.interceptor.AppInterceptorURL;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private AppInterceptorURL interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// Adiciona o Interceptor na aplicação

		registry.addInterceptor(interceptor);
	}

	// config banco de dados

	@Bean
	public DataSource dataSoucer() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3307/auditorio");
		ds.setUsername("root");
		ds.setPassword("root");
		return ds;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

		// --//
		adapter.setDatabase(org.springframework.orm.jpa.vendor.Database.MYSQL);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
		adapter.setPrepareConnection(true);
		// quando o banco ja exista, e vc nao quer mexer na estrutura voce usa o
		// generate ddl.
		adapter.setGenerateDdl(true);
		// show sql para mostrar os codigos do sql, Depurar o codigo do sql
		adapter.setShowSql(true);
		return adapter;

	}
}
