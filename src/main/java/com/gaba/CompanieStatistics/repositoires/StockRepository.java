package com.gaba.CompanieStatistics.repositoires;

import com.gaba.CompanieStatistics.entities.Stock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends ReactiveCrudRepository<Stock, String>,
        ReactiveSortingRepository<Stock,String> {

}
