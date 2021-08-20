package com.example.msdepositbank.services;

import com.example.msdepositbank.models.dto.TransactionDTO;
import reactor.core.publisher.Mono;

/**
 * The interface Transaction dto service.
 */
public interface ITransactionDTOService {
    /**
     * Save transaction mono.
     *
     * @param transaction the transaction
     * @return the mono
     */
    public Mono<TransactionDTO> saveTransaction(TransactionDTO transaction);
}
