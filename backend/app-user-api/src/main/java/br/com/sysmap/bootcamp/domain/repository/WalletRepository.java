package br.com.sysmap.bootcamp.domain.repository;

import br.com.sysmap.bootcamp.domain.entity.Wallet;
import br.com.sysmap.bootcamp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}