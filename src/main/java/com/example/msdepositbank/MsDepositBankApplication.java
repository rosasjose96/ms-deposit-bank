package com.example.msdepositbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * The type Ms deposit bank application.
 */
@SpringBootApplication
@EnableEurekaClient
public class MsDepositBankApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		SpringApplication.run(MsDepositBankApplication.class, args);
	}

}
