package simpleshop.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Spring Security User.
 */
@Entity
public class User {


    private String username;
    private String password;
    private Boolean enabled = Boolean.TRUE;
    private Set<Authority> authorities = new HashSet<>();
    private Map<String, String> permissions = new HashMap<>();
    private Customer customer;

    public Authority addAuthority(String auth){
        Authority authority = new Authority();
        authority.setOwner(this);
        authority.setAuthority(auth);
        if(!this.getAuthorities().contains(authority))
            this.authorities.add(authority);
        else {
            for (Authority a : this.getAuthorities()){
                if(Objects.equals(a.getAuthority(), auth))
                    return a;
            }
        }
        return authority;
    }

    @Id
    @NotNull
    @Size(min = 4)
    @Column(length = 50, nullable = false, updatable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Size(min = 4)
    @NotNull
    @Column(length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull
    @Column(nullable = false)
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "owner", cascade = {CascadeType.ALL})
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @CollectionTable(name = "user_permissions")
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @MapKeyClass(String.class)
    @MapKeyColumn(nullable = false, length = 60)
    @Column(length = 70)
    public Map<String, String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, String> permissions) {
        this.permissions = permissions;
    }

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", unique = true)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
