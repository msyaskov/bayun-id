package dev.bayun.id.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private UUID id;

    private String passwordHash;

    @Column(name = "authority")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_authorities", joinColumns = @JoinColumn(name = "account_id"))
    private Set<String> authorities;

    private boolean locked;
    private boolean enabled;

}
