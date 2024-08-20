package br.com.sysmap.bootcamp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet")
public class Wallet {

    public Wallet(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "points")
    private Long points = 0L;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
