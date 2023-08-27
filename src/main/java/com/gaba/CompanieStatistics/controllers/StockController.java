package com.gaba.CompanieStatistics.controllers;

import com.gaba.CompanieStatistics.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

}
