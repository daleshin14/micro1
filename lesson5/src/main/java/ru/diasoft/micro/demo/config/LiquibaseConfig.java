package ru.diasoft.micro.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import liquibase.integration.spring.SpringLiquibase;

@Component
@Configuration
public class LiquibaseConfig {
    
	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
	    SpringLiquibase liquibase = new SpringLiquibase();
	    liquibase.setDataSource(dataSource);
	    liquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.xml");
	    return liquibase;
	}

}
