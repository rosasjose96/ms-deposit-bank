package com.example.msdepositbank.services;

import com.example.msdepositbank.models.dto.DebitAccountDTO;
import reactor.core.publisher.Mono;

public interface IDebitAccountDTOService {
    public Mono<DebitAccountDTO> findByAccountNumber(String typeofdebit,String accountNumber);
    public Mono<DebitAccountDTO> updateDebit(String typeofdebit, DebitAccountDTO account);
}
