package by.vstu.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class UserModel implements UserDetails {

    //Всё-таки UUID нужен для Id...
    @Id
    @Column(name = "u_id", nullable = false)
    private UUID id = UUID.randomUUID();

    private Long externalId;
    private String helperName;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(unique = true, length = 100, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(length = 2048, nullable = false)
    private String scopes = "*_read";

    private boolean locked = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "u_id")}, inverseJoinColumns = {
            @JoinColumn(name = "r_id")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"u_id", "r_id"})})
    @JsonIgnore
    private List<RoleModel> roles;

    public List<String> getRolesAsString() {
        return roles.stream().map(RoleModel::getName).toList();
    }

    public List<String> getResourceIds() {
        List<String> result = new ArrayList<>();
        if (StringUtils.hasText(this.scopes)) {
            result = Arrays.stream(this.scopes.split(",")).map(m -> m.split("_")[0]).distinct().toList();
        }
        return result;
    }

    public List<String> getScopes() {
        List<String> result = new ArrayList<>();
        if (StringUtils.hasText(this.scopes)) {
            result = Arrays.stream(this.scopes.split(",")).toList();
        }
        return result;
    }

    public List<String> getAuthoritiesAsString() {
        return this.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName())))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountNonLocked();
    }
}
