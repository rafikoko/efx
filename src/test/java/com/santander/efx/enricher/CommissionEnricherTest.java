package com.santander.efx.enricher;

import com.santander.efx.entity.FxPriceFeedRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CommissionEnricherTest {

    @Autowired
    CommissionEnricher enricher;
    @Test
    void enrichFxPriceFeedRecordWithCommission() {
        FxPriceFeedRecord testRecord = new FxPriceFeedRecord();
        testRecord.setBid(BigDecimal.valueOf(1.1d));
        testRecord.setAsk(BigDecimal.valueOf(1.2d));

        BigDecimal commissionPercent = new BigDecimal(0.1d);

        testRecord = enricher.enrichFxPriceFeedRecordWithCommission(testRecord, commissionPercent);

        FxPriceFeedRecord testRecord2 = new FxPriceFeedRecord();
        testRecord2.setBid(BigDecimal.valueOf(1.2499d));
        testRecord2.setAsk(BigDecimal.valueOf(1.2561d));

        testRecord2 = enricher.enrichFxPriceFeedRecordWithCommission(testRecord2, commissionPercent);

        FxPriceFeedRecord testRecord3 = new FxPriceFeedRecord();
        testRecord3.setBid(BigDecimal.valueOf(1.249957d));
        testRecord3.setAsk(BigDecimal.valueOf(1.256183d));

        testRecord3 = enricher.enrichFxPriceFeedRecordWithCommission(testRecord3, commissionPercent);

        //making assumption about precision, given BigDecimal value is initialized with double value which has limitation about scale
        MathContext mc = new MathContext(15);

        int testRecordEqualityScore = new BigDecimal(1.1011d, mc).compareTo(testRecord.getBidWithCommission());
        if (testRecordEqualityScore != 0) {
            log.error("Value calculated is: " + testRecord.getBidWithCommission() + " while expected: 1.1011");
        }
        assertEquals(0, testRecordEqualityScore);
        assertTrue(new BigDecimal(1.1988d, mc).compareTo(testRecord.getAskWithCommission())==0);
        assertTrue(new BigDecimal(1.2511499d, mc).compareTo(testRecord2.getBidWithCommission())==0);
        assertTrue(new BigDecimal(1.2548439d, mc).compareTo(testRecord2.getAskWithCommission())==0);
        assertTrue(new BigDecimal(1.251206957d, mc).compareTo(testRecord3.getBidWithCommission())==0);
        assertTrue(new BigDecimal(1.254926817d, mc).compareTo(testRecord3.getAskWithCommission())==0);

    }
}