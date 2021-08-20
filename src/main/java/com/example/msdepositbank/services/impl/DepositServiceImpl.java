package com.example.msdepositbank.services.impl;

import com.example.msdepositbank.models.entities.Deposit;
import com.example.msdepositbank.repository.DepositRepository;
import com.example.msdepositbank.services.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Deposit service.
 */
@Service
public class DepositServiceImpl implements IDepositService {

    @Autowired
    private DepositRepository repository;

    @Override
    public Mono<Deposit> create(Deposit o) {
        return repository.save(o);
    }

    @Override
    public Flux<Deposit> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Deposit> findById(String s) {
        return repository.findById(s);
    }

    @Override
    public Mono<Deposit> update(Deposit o) {
        return repository.save(o);
    }

    @Override
    public Mono<Void> delete(Deposit o) {
        return repository.delete(o);
    }
}
