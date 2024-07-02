package br.com.rosa.infra.security;

import java.io.IOException;

import br.com.rosa.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// É utilizado para que o Spring carregue uma classe/componente genérico
@Component							//Essa classe que estamos herdando do spring OncePerRequestFilter garante que sera executada uma unica vez a cada requisição
public class SecurityFilter extends OncePerRequestFilter {

    //Spring Injeta a classe
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;


    //Sempre que chegar uma nova requisição API o spring ja sabe que tem essa classe/metodo
    @Override //request pega coisas da requisição, response - enviar coisas na resposta, filterChain - representa a cadeia de filtros na aplicação
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //recupera o token da requisição
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            //getSubject pega o token do sujeito logado se nao estiver vazio/null, valida se o token está correto, pega o email usuario que está dentro do token
            var subject = tokenService.getSubject(tokenJWT);
            //Depois que pegar o usuario com a ajuda do userDetails que está dentro do token, carrega esse subject (usuario) lá dentro do banco de dados
            var usuario = repository.findByLogin(subject);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());


            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //necessário para chamar os próximos filtros na aplicação
        filterChain.doFilter(request, response);

    }

    public String recuperarToken(HttpServletRequest request) {
        //request.getHeader pega o cabeçalho obs tem que conter o nome do cabeçalho
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            //replace substitui a palavra Bearer por nada/um espaço
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;

    }

}