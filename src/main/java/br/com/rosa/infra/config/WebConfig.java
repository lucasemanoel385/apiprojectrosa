package br.com.rosa.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*WebMvcConfigurer (Configuração CORS Global): Define as políticas de CORS para toda a aplicação.
Isso é útil para habilitar CORS globalmente,
mas não influencia diretamente o comportamento do Spring Security.*/
@Configuration
//EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig implements WebMvcConfigurer {

	@Value("${url.ip.static.domain}")
	private String ipStatic;

	@Value("${url.ip.static.domain.two}")
	private String ipStaticTwo;

	@Override
	public void addCorsMappings(CorsRegistry resgistry) {
		resgistry.addMapping("/**")
								.allowedOrigins(ipStatic, ipStaticTwo)
								.allowedHeaders("*")
				                .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
								.allowCredentials(true);
	}

}
