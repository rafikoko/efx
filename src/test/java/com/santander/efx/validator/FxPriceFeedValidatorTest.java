package com.santander.efx.validator;

import com.santander.efx.entity.FxPriceFeedRecord;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FxPriceFeedValidatorTest {

    @Test
    void validateCorrectFxPriceFeedRecord() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        assertTrue(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals(null, record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_MissingId() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setId(0l);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Id has not been provided for FX price feed record for instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_MissingInstrumentName() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setInstrumentName(null);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Instrument name has not been provided for FX price feed record with id: 123; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_MissingBid() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setBid(null);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Bid price has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_ZeroBid() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setBid(BigDecimal.ZERO);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Bid price has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_MissingAsk() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setAsk(null);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Ask price has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_ZeroAsk() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setAsk(BigDecimal.ZERO);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Ask price has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectFxPriceFeedRecord_MissingTimestamp() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setTimestamp(null);
        assertFalse(FxPriceFeedValidator.validateFxPriceFeedRecord(record));
        assertEquals("Timestamp has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }


    @Test
    void validateCorrectPricesWithCommissionInFxPriceFeedRecord() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecordWithCommission();
        assertTrue(FxPriceFeedValidator.validatePricesWithCommissionInFxPriceFeedRecord(record));
        assertEquals(null, record.getValidationMessage());
    }

    @Test
    void validateInCorrectPricesWithCommissionInFxPriceFeedRecord_MissingBidWithCommission() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecordWithCommission();
        record.setBidWithCommission(null);
        assertFalse(FxPriceFeedValidator.validatePricesWithCommissionInFxPriceFeedRecord(record));
        assertEquals("Bid price with commission has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectPricesWithCommissionInFxPriceFeedRecord_ZeroBidWithCommission() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecordWithCommission();
        record.setBidWithCommission(BigDecimal.ZERO);
        assertFalse(FxPriceFeedValidator.validatePricesWithCommissionInFxPriceFeedRecord(record));
        assertEquals("Bid price with commission has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectPricesWithCommissionInFxPriceFeedRecord_MissingAskWithCommission() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecordWithCommission();
        record.setAskWithCommission(null);
        assertFalse(FxPriceFeedValidator.validatePricesWithCommissionInFxPriceFeedRecord(record));
        assertEquals("Ask price with commission has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void validateInCorrectPricesWithCommissionInFxPriceFeedRecord_ZeroAskWithCommission() {
        FxPriceFeedRecord record = createBaseFxPriceFeedRecordWithCommission();
        record.setAskWithCommission(BigDecimal.ZERO);
        assertFalse(FxPriceFeedValidator.validatePricesWithCommissionInFxPriceFeedRecord(record));
        assertEquals("Ask price with commission has not been provided for FX price feed record with id: 123 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    FxPriceFeedRecord createBaseFxPriceFeedRecord(){
        FxPriceFeedRecord record = new FxPriceFeedRecord();
        record.setId(123l);
        record.setInstrumentName("EUR/USD");
        record.setBid(new BigDecimal("1.23"));
        record.setAsk(new BigDecimal("1.25"));
        record.setTimestamp(LocalDateTime.parse("01-06-2020 12:01:01:001", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        return record;
    }

    FxPriceFeedRecord createBaseFxPriceFeedRecordWithCommission(){
        FxPriceFeedRecord record = createBaseFxPriceFeedRecord();
        record.setBidWithCommission(new BigDecimal("1.23123"));
        record.setAskWithCommission(new BigDecimal("1.24875"));

        return record;
    }
}