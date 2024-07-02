package br.com.rosa.controller;

import br.com.rosa.domain.user.AutenticationService;
import br.com.rosa.domain.user.DataAutentication;
import br.com.rosa.domain.user.User;
import br.com.rosa.infra.security.DataTokenJWT;
import br.com.rosa.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class AutenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired //Injetamos a classe TokenService na classe controler
    private TokenService tokenService;

    @Autowired
    private AutenticationService service;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DataAutentication dados) {
        //Instancia um DTO do tipo token
        var user = service.checkLoginOrPassword(dados);

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.password());
        var authentication = manager.authenticate(authenticationToken);

        //getPrincipal pega o usuario logado.  getPrincipal devolve um object por isso temos que fazer um cast pra usuario e o Spring consegue identificar pq implementamos a interface UserDetails na classe usuario
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());


        return ResponseEntity.ok(new DataTokenJWT(tokenJWT ,user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));

    }
}
