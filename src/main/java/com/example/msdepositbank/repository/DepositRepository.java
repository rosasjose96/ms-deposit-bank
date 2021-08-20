package com.example.msdepositbank.repository;

import com.example.msdepositbank.models.entities.Deposit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The interface Deposit repository.
 */
public interface DepositRepository extends ReactiveMongoRepository<Deposit, String> {
}