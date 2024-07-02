package br.com.rosa.domain.profile;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
@Table(name="profile")
public class Profile implements GrantedAuthority{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Override
    public String getAuthority() {
        return "ROLE_"+nome;
    }
}
