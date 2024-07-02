package br.com.rosa.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import br.com.rosa.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;


@Service //Falar pro spring que é um service
public class TokenService {

    //Faz o spring ler esse atributo dentro do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {

        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    // Pra identificar a ferramenta/API que é dona pela geração do token
                    .withIssuer("API Food.pic")
                    //withSubject guarda no token quem é dona/dono desse token gerado
                    .withSubject(user.getLogin())
                    //data pra expiração do token
                    .withExpiresAt(dataExpiracao())
                    //withClaim("chave", valor) guarda informações adicionais no token
                    .withClaim("role", user.getProfile().get(0).getAuthority())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }

    }

    public String getSubject(String tokenJWT) {

        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Food.pic")
                    .build()
                    //verifica se esse token que está chegando como parametro está valido de acordo com esse algoritmo e com esse Issuer na hora de verificar o token
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }

    }

    private Instant dataExpiracao() {
        // localdatetim.now() pegar hora atual, adiciona 2 horas, toInstant converte localdatetime pra uma instant e pra converter precimos passar o ZoneOffset pra passar o fuso horario (Timezone)
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }


}