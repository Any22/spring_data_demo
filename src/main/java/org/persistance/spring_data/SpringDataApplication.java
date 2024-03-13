package org.persistance.spring_data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/******************************************************************************************************************************************
 * crud operations : create , find ,update and delete
 * @author saba akhtar
 *
 *****************************************************************************************************************************************/
@SpringBootApplication
@ComponentScan(basePackages = "org.persistance.spring_data")

public class SpringDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataApplication.class, args);
	}

}
