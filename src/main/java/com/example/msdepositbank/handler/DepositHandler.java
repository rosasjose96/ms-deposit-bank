package com.example.msdepositbank.handler;

import com.example.msdepositbank.models.entities.Deposit;
import com.example.msdepositbank.services.IDepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j(topic = "DEPOSIT_HANDLER")
public class DepositHandler {
    private final IDepositService depositService;

    @Autowired
    public DepositHandler(IDepositService depositService) {
        this.depositService = depositService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(depositService.findAll(), Deposit.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        return depositService.findById(id).flatMap(deposit -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(deposit))
                .switchIfEmpty(Mono.error(new RuntimeException("DEPOSIT DOES NOT EXIST")));
    }
}
