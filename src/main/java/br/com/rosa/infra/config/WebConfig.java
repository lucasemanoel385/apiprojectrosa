package br.com.rosa.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*WebMvcConfigurer (Configuração CORS Global): Define as políticas de CORS para toda a aplicação.
Isso é útil para habilitar CORS globalmente,
mas não influencia diretamente o comportamento do Spring Security.*/
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry resgistry) {
		resgistry.addMapping("/**")
								.allowedOrigins("http://localhost:4200")
								.allowedHeaders("*")
				                .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
								.allowCredentials(true);
	}

}
