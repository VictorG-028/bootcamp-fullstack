package br.com.sysmap.bootcamp.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class WalletDto implements Serializable {

    private String email;
    private BigDecimal value;

}
