package br.com.rosa.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
Spring Security: Tem seu próprio mecanismo de controle de segurança e precisa ser explicitamente instruído para permitir CORS.
*/

@Configuration 	//Avisar o spring que é uma class de configuração
@EnableWebSecurity     //Serve pra personalizar as configurações de segurança
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    //Bean serve pra expor o retorno desse metodo e o Bean fala pro spring oque esta sendo devolvido
    @Bean	//Esse é um objeto do spring que é usado pra configurar coisas relacionadas sobre autorizacao e autenticacao
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //csrf.disable, desabilitarmos o tratamento contra ataques do tipo CSRF (Cross-Site Request Forgery). Pq o nosso token ja vai fazer esse tratamento. E pediu pro spring o formulario e a autenticacao STATELESS pq aqui é uma API REST
        return http.csrf(AbstractHttpConfigurer::disable).sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                //authorizeHttpRequest serve pra gente configurar como vai ser a autorização das configurações
                 authorizeHttpRequests(authorize -> {
                    //requestMatchers é pra informar se é pra ser bloqueada ou liberada X URL .Se caso vim uma requisção /login do method POST permita e o resto bloquea tuto
                     authorize.requestMatchers(HttpMethod.POST,"/login/**").permitAll().
                             requestMatchers("/ws/**").permitAll().
                             requestMatchers(HttpMethod.GET, "/task/**").hasAnyRole("ADMIN", "USER").
                             requestMatchers(HttpMethod.POST, "/task/**").hasRole("ADMIN").
                             requestMatchers(HttpMethod.DELETE, "/task/**").hasRole("ADMIN").
                             requestMatchers(HttpMethod.GET, "item/**").hasAnyRole("ADMIN", "USER").
                             requestMatchers(HttpMethod.GET, "/data-company/**").hasAnyRole("ADMIN", "USER").
                             requestMatchers(HttpMethod.GET, "/data-company/accouting/**").hasRole("ADMIN").
                             requestMatchers(HttpMethod.PUT, "/data-company/**").hasRole("ADMIN").
                             requestMatchers(HttpMethod.PATCH, "/data-company/**").hasRole("ADMIN").
                     anyRequest().authenticated();
                             //anyRequest authenticated serve pra falar que se qualquer outra requisição tem que está atutenticado

                    //addFIlterBefore ordena a requisição por onde será passada primeiro. Username é o filter do spring
                })
                .cors(Customizer.withDefaults()) // Habilitar o CORS no securityConfigurations
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
    }


    @Bean //Serve pra exportar uma classe para o spring, fazendo com que ele consiga carregá-la e realize a sua injeção de dependência em outras classes.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        //Essa classe AuthenticationConfiguration tem esse metodo getAuthenticationManager que sabe criar um objeto AuthenticationManager
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //com isso ensinamos pro spring que é esse encoder que sera usado pro hashing de senha
        return new BCryptPasswordEncoder();
    }

}