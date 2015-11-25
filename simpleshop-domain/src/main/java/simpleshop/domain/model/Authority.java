package simpleshop.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Authorities of a spring security user.
 */
@Entity
public class Authority {

    @Id
    @GeneratedValue
    private Integer authorityId;

    @NotNull
    @ManyToOne(optional = false)
    private User owner;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String authority;

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority1 = (Authority) o;

        if (!owner.equals(authority1.owner)) return false;
        return authority.equals(authority1.authority);

    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + authority.hashCode();
        return result;
    }
}
