package uz.backecommers.identety.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.backecommers.identety.entity.Users;

import javax.net.ssl.SSLSession;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserUserDetailsDTO implements UserDetails {
    private final Long id;
    private final String name;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserUserDetailsDTO(Users user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}