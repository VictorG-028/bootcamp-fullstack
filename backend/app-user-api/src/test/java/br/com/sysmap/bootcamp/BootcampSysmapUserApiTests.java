package br.com.sysmap.bootcamp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes= BootcampSysmapUserApi.class)
class BootcampSysmapUserApiTests {

	@Test
	void contextLoads() {
	}

}
