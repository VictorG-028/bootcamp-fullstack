package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.service.WalletService;
import br.com.sysmap.bootcamp.dto.WalletDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Credit value in wallet")
    @PostMapping("/credit/{value}")
    public ResponseEntity<WalletDto> credit(@PathVariable BigDecimal value) {

        try {
            this.walletService.credit(value);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "My wallet")
    @GetMapping("/")
    public ResponseEntity<WalletDto> getMyWallet() {

        try {
            WalletDto walletDto = this.walletService.getMyWallet();
            return ResponseEntity.ok(walletDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
