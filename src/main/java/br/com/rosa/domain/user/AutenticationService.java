package br.com.rosa.domain.user;

import br.com.rosa.infra.exceptions.AutenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder test;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Quando o usuario fizer login é esse metodo que será chamado pra verificar caso nao encontre manda um UsernameNotFound(Não encontrado)
        var user = repository.findByLogin(username);
        return user;
    }

    public UserDetails checkLoginOrPassword(DataAutentication data) {
        var user = repository.findByLogin(data.login());

        if(user == null) {
            throw new AutenticationException("Login invalido");
        }

        var check = test.matches(data.password(), user.getPassword());

        if(!check) {
            throw new AutenticationException("Senha invalida");
        }
        return user;
    }
}
