package com.nutrition.mx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.nutrition.mx.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class NutritionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutritionAppApplication.class, args);
	}
	// Este método imprime la base de datos usada al iniciar
//    @Bean
//    public CommandLineRunner testMongoConnection(MongoTemplate mongoTemplate) {
//        return args -> {
//            System.out.println("🌱 Base de datos conectada: " + mongoTemplate.getDb().getName());
//        };
//    }

}
