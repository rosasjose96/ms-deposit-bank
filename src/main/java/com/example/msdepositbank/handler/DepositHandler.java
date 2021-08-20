package com.example.msdepositbank.handler;

import com.example.msdepositbank.models.dto.DebitAccountDTO;
import com.example.msdepositbank.models.dto.TransactionDTO;
import com.example.msdepositbank.models.entities.Deposit;
import com.example.msdepositbank.services.IDebitAccountDTOService;
import com.example.msdepositbank.services.IDepositService;
import com.example.msdepositbank.services.ITransactionDTOService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The type Deposit handler.
 */
@Component
@Slf4j(topic = "DEPOSIT_HANDLER")
public class DepositHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepositHandler.class);

    @Autowired
    private IDebitAccountDTOService accountService;

    @Autowired
    private IDepositService service;

    @Autowired
    private ITransactionDTOService transactionService;

    /**
     * Find all mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Deposit.class);
    }

    /**
     * Find debit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findDebit(ServerRequest request) {
        String id = request.pathVariable("id");
        return service.findById(id).flatMap((c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c))
                .switchIfEmpty(ServerResponse.notFound().build()))
        );
    }

    /**
     * Create deposit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> createDeposit(ServerRequest request){

        Mono<Deposit> depositMono = request.bodyToMono(Deposit.class);


        return depositMono.flatMap( depositRequest -> accountService.findByAccountNumber(depositRequest.getTypeOfAccount(),depositRequest.getAccountNumber())
                .flatMap(account -> {
                    if(account.getMaxLimitMovementPerMonth()>=account.getMovementPerMonth()){
                        account.setMovementPerMonth(account.getMovementPerMonth()+1);
                        account.setAmount(account.getAmount()+depositRequest.getAmount());
                    }else if (account.getMaxLimitMovementPerMonth()<account.getMovementPerMonth()){
                        LOGGER.info("La commission es: " + account.getCommission());
                        account.setAmount(account.getAmount()+depositRequest.getAmount()-account.getCommission());
                    }
                    LOGGER.info("El id del débito es: " + account.getId());
                    return accountService.updateDebit(account.getTypeOfAccount(),account);
                })
                .flatMap(ope -> {
                    TransactionDTO transaction = new TransactionDTO();
                    transaction.setTypeOfAccount(ope.getTypeOfAccount());
                    transaction.setTypeoftransaction("DEPOSIT");
                    transaction.setCustomerIdentityNumber(ope.getCustomerIdentityNumber());
                    transaction.setTransactionAmount(depositRequest.getAmount());
                    transaction.setIdentityNumber(depositRequest.getAccountNumber());
                    return transactionService.saveTransaction(transaction);
                })
                .flatMap(trans ->  accountService.findByAccountNumber(trans.getTypeOfAccount(),trans.getIdentityNumber()))
                .flatMap(debit -> {
                    if(debit.getMaxLimitMovementPerMonth()<debit.getMovementPerMonth()){
                        TransactionDTO Commission = new TransactionDTO();
                        Commission.setTypeOfAccount(debit.getTypeOfAccount());
                        Commission.setTypeoftransaction("COMMISSION");
                        Commission.setCustomerIdentityNumber(debit.getCustomerIdentityNumber());
                        Commission.setTransactionAmount(debit.getCommission());
                        Commission.setIdentityNumber(depositRequest.getAccountNumber());
                        return transactionService.saveTransaction(Commission);
                    } else {
                        return Mono.just(DebitAccountDTO.builder().build());
                    }

                })
                .flatMap(deposit ->  service.create(depositRequest)))
                .flatMap( c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(c)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Delete debit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> deleteDebit(ServerRequest request){

        String id = request.pathVariable("id");

        Mono<Deposit> depositMono = service.findById(id);

        return depositMono
                .doOnNext(c -> LOGGER.info("delete Paymencard: PaymentCardId={}", c.getId()))
                .flatMap(c -> service.delete(c).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Update debit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> updateDebit(ServerRequest request){
        Mono<Deposit> depositMono = request.bodyToMono(Deposit.class);
        String id = request.pathVariable("id");

        return service.findById(id).zipWith(depositMono, (db,req) -> {
            db.setAmount(req.getAmount());
            return db;
        }).flatMap( c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.create(c),Deposit.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
