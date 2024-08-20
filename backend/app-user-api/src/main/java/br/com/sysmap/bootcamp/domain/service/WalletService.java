package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.entity.Wallet;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.domain.util.Util;
import br.com.sysmap.bootcamp.dto.WalletDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalletService {

    private final UserService userService;
    private final WalletRepository walletRepository;
    private final Util util;


    // =-= Endpoints section =-= //
    public void credit(BigDecimal value) throws EntityNotFoundException {

        User user = util.getLoggedUser();
        Wallet wallet = this.findWalletByUser(user);
        BigDecimal balance = wallet.getBalance().add(value);

        wallet.setBalance(balance);

        String email = wallet.getUser().getEmail();
        log.info("Credited {} to {} wallet", value, email);

        walletRepository.save(wallet);
    }


    public WalletDto getMyWallet() throws EntityNotFoundException {

        User user = util.getLoggedUser();
        String loggedEmail = user.getEmail();

        Wallet wallet = this.findWalletByUser(user);

        return WalletDto.builder()
                        .email(user.getEmail())
                        .value(wallet.getBalance())
                        .build();
    }


    public void debit(WalletDto walletDto) {

        User user = userService.findByEmail(walletDto.getEmail());
        Wallet wallet = this.findWalletByUser(user);
        BigDecimal balance = wallet.getBalance().subtract(walletDto.getValue());

        // Can't implement this logic whithout a new rabbit queue
        // boolean notEnoughMoney = balance.signum() < 0; // is negative
        // if (notEnoughMoney) {
        //    throw new RuntimeException("Not enough money to buy");
        //}

        Long pointsToAdd = getPointOfCurrentDay();

        // Apply balance and point changes
        wallet.setBalance(balance);
        wallet.setPoints(wallet.getPoints() + pointsToAdd);

        String email = wallet.getUser().getEmail();
        log.info("Debited and added points to {} wallet", email);

        walletRepository.save(wallet);
    }

    // =-= Util section =-= //
    /**
     * self-explanatory. easy to read.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Wallet createUserWallet(User user) {
        Wallet wallet = new Wallet(user);
        return this.walletRepository.save(wallet);
    }


    /**
     * self-explanatory. easy to read.
     */
    private Wallet findWalletByUser(User user) throws EntityNotFoundException {
        return this.walletRepository
                   .findByUser(user)
                   .orElseThrow(() -> new EntityNotFoundException(
                           "Wallet not found (DATABASE IS INCONSISTENT!!!)"));
    }


    @Getter
    @AllArgsConstructor
    private enum Points {
        MONDAY(25L),
        TUESDAY(7L),
        WEDNESDAY(6L),
        THURSDAY(2L),
        FRIDAY(10L),
        SATURDAY(15L),
        SUNDAY(20L);

        private final Long points;
    }
    /**
     * Use the private enum Points to associate day to points.
     *
     * @return The points for the current day.
     */
    private Long getPointOfCurrentDay() {
        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();

        Points currentDayPoints = Points.valueOf(currentDayOfWeek.toString());
        return currentDayPoints.getPoints();
    }
}
