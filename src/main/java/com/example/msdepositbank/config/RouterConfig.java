package com.example.msdepositbank.config;

import com.example.msdepositbank.handler.DepositHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * The type Router config.
 */
@Configuration
public class RouterConfig {
    /**
     * Rutas router function.
     *
     * @param handler the handler
     * @return the router function
     */
    @Bean
    public RouterFunction<ServerResponse> rutas(DepositHandler handler) {
        return route(GET("/api/deposit"), handler::findAll)
                .andRoute(GET("/api/deposit/{id}"), handler::findDebit)
                .andRoute(DELETE("/api/deposit/{id}"), handler::deleteDebit)
                .andRoute(PUT("/api/deposit/{id}"), handler::updateDebit)
                .andRoute(POST("/api/deposit"), handler::createDeposit);
    }
}
