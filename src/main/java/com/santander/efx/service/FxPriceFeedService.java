package com.santander.efx.service;

import com.santander.efx.enricher.CommissionEnricher;
import com.santander.efx.entity.FxPriceFeedRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.santander.efx.repository.FxPriceFeedRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class FxPriceFeedService {

    //assuming that hardcoding commission % is fine for this exercise
    //it would have been rather provided in real world from certain source
    private final BigDecimal COMMISSION_PERCENT = new BigDecimal(0.1);

    @Autowired
    FxPriceFeedRepository fxPriceFeedRepository;
    @Autowired
    CommissionEnricher enricher;

    public List<FxPriceFeedRecord> saveFxPriceFeed(List<FxPriceFeedRecord> feed) {

        enricher.enrichFxPriceFeedRecordsWithCommission(feed, COMMISSION_PERCENT);

        return fxPriceFeedRepository.saveAllAndFlush(feed);
    }

    public List<FxPriceFeedRecord> loadAll() {
        return fxPriceFeedRepository.findAll();
    }

}
