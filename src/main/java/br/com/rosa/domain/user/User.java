package br.com.rosa.domain.user;


import br.com.rosa.domain.profile.Profile;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name="user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Profile> profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.profile;
    }
    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return senha;
    }
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return login;
    }
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
