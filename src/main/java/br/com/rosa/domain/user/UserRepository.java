package br.com.rosa.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    //Metodo que vai fazer a consulta do usuario no banco de dados
    UserDetails findByLogin(String login);

}
