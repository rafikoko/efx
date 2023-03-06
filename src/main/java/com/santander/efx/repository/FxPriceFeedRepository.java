package com.santander.efx.repository;

import com.santander.efx.entity.FxPriceFeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FxPriceFeedRepository extends JpaRepository<FxPriceFeedRecord, Long> {

    FxPriceFeedRecord findTopByInstrumentNameOrderByTimestampDesc(String name);
}
