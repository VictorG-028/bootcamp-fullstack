package br.com.sysmap.bootcamp.domain.listener;

import br.com.sysmap.bootcamp.domain.service.WalletService;
import br.com.sysmap.bootcamp.dto.WalletDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RabbitListener(queues = "WalletQueue")
public class WalletListener {

    @Autowired
    private WalletService walletService;

    @RabbitHandler
    public void receive(WalletDto walletDto)  {
        log.info("Processing wallet in queue");
        walletService.debit(walletDto);
    }
}
