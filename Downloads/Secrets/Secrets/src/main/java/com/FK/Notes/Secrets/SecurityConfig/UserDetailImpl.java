package com.FK.Notes.Secrets.SecurityConfig;

import com.FK.Notes.Secrets.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Data
public class UserDetailImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
 /*
 @JsonIgnore is used when you are serializing/deserializing Java objects to/from JSON (e.g., in REST APIs).

When applied to a field, it tells Jackson to ignore this field when sending the object in API responses or reading JSON requests.

  */
    @JsonIgnore
    private String password;

    private boolean is2faEnabled;

    private Collection<? extends GrantedAuthority>authorities;

    public UserDetailImpl(Long id, String username, String email, String password,
                           boolean is2faEnabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.is2faEnabled = is2faEnabled;
        this.authorities = authorities;
    }

    public static UserDetailImpl build(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName().name());
        return new UserDetailImpl(user.getUserId(), user.getUserName(), user.getEmail(),user.getPassword(), user.isTwoFactorEnabled(), List.of(authority));
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public boolean is2faEnabled() {
        return is2faEnabled;
    }
    @Override
    public boolean equals(Object o){
        if(this==o){
            return true;
        }
        if(o==null || getClass()!=o.getClass()){
            return false;
        }
        UserDetailImpl user = (UserDetailImpl) o;
        return Objects.equals(id,user.id);
    }
}
