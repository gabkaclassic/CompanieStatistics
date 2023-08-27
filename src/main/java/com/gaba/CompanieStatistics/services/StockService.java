package com.gaba.CompanieStatistics.services;

import com.gaba.CompanieStatistics.clients.IEXClient;
import com.gaba.CompanieStatistics.entities.CompanyDTO;
import com.gaba.CompanieStatistics.repositoires.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableAsync
public class StockService {

    private final StockRepository stockRepository;
    private final IEXClient client;


    @Scheduled(fixedDelay = 60 * 60 * 1000)
    @Async
    public void saveStocks() {

        stockRepository.deleteAll().subscribe();

        client.getEnabledCompanies()
                .map(CompanyDTO::symbol)
                .flatMap(client::getStock)
                .doOnNext(stock -> stockRepository.save(stock).subscribe())
                .subscribe();
    }

    @Scheduled(fixedRate = 5 * 1000)
    @Async
    public void computeVolumeStatistics() {

        stockRepository.findAll()
                .sort((s1, s2) -> s1.compareByVolumeAndCompany(s1, s2))
                .take(5)
                .log()
                .subscribe();
    }

    @Scheduled(fixedRate = 5 * 1000)
    @Async
    public void computeDeltaStatistics() {

        stockRepository.findAll()
                .sort((s1, s2) -> s1.compareByDelta(s1, s2))
                .take(5)
                .log()
                .subscribe();
    }
}
