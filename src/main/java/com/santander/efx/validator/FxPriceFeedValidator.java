package com.santander.efx.validator;

import com.santander.efx.entity.FxPriceFeedRecord;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class FxPriceFeedValidator {
    /**
     * Validate fx price feed record while assuming that all fields are required
     * @param fxPriceFeedRecordItem
     * @return true, if valid, else false
     */
    public static boolean validateFxPriceFeedRecord(FxPriceFeedRecord fxPriceFeedRecordItem){

        boolean isRecordValid = fxPriceFeedRecordItem.isValid();
        StringBuffer validationMessage = new StringBuffer("");
        //Assuming that id needs to be positive integer
        if (fxPriceFeedRecordItem.getId()<1){
            log.debug("Id has not been provided for FX price feed record for instrument name: " + fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Id has not been provided for FX price feed record for instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        if (fxPriceFeedRecordItem.getInstrumentName() == null || fxPriceFeedRecordItem.getInstrumentName().trim().isEmpty()) {
            log.debug("Instrument name has not been provided for FX price feed record with id: " + fxPriceFeedRecordItem.getId());
            validationMessage.append("Instrument name has not been provided for FX price feed record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append("; ");
            isRecordValid = false;
        }
        if (fxPriceFeedRecordItem.getTimestamp() == null) {
            log.debug("Timestamp has not been provided for FX price feed record with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Timestamp has not been provided for FX price feed record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        //assuming that original prices must not be 0, given the nature of FX market and more broadly assets
        if (fxPriceFeedRecordItem.getAsk() == null || fxPriceFeedRecordItem.getAsk().equals(BigDecimal.ZERO)) {
            log.debug("Ask price has not been provided for FX price feed record with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Ask price has not been provided for FX price feed record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        if (fxPriceFeedRecordItem.getBid() == null || fxPriceFeedRecordItem.getBid().equals(BigDecimal.ZERO)) {
            log.debug("Bid price has not been provided for FX price feed record with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Bid price has not been provided for FX price feed record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        //assuming negative prices are not allowed, given nature of FX market
        if (fxPriceFeedRecordItem.getAsk()!=null && fxPriceFeedRecordItem.getAsk().compareTo(BigDecimal.ZERO) < 0) {
            log.debug("Ask price must not be negative for FX price feed record, but it occurred in record with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Ask price must not be negative for FX price feed record, but it occurred in record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        if (fxPriceFeedRecordItem.getBid()!=null && fxPriceFeedRecordItem.getBid().compareTo(BigDecimal.ZERO) < 0) {
            log.debug("Bid price must not be negative for FX price feed record, but it occurred in record  with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Bid price must not be negative for FX price feed record, but it occurred in record  with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        if (!isRecordValid){
            fxPriceFeedRecordItem.setValidationMessage(fxPriceFeedRecordItem.getValidationMessage()!=null ? fxPriceFeedRecordItem.getValidationMessage() + validationMessage : validationMessage.toString());
        }

        return isRecordValid;
    }

    /**
     * Validate prices enriched with commission in fx price feed record while assuming that all fields are required
     * @param fxPriceFeedRecordItem
     * @return true, if valid, else false
     */
    public static boolean validatePricesWithCommissionInFxPriceFeedRecord(FxPriceFeedRecord fxPriceFeedRecordItem) {
        boolean isRecordValid = true;
        StringBuffer validationMessage = new StringBuffer("");

        //assuming that prices with commission must not be 0, given the nature of FX market and more broadly assets and requirement to calculate
        if (fxPriceFeedRecordItem.getAskWithCommission() == null || fxPriceFeedRecordItem.getAskWithCommission().equals(BigDecimal.ZERO)) {
            log.debug("Ask price with commission has not been provided for FX price feed record with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Ask price with commission has not been provided for FX price feed record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        if (fxPriceFeedRecordItem.getBidWithCommission() == null || fxPriceFeedRecordItem.getBidWithCommission().equals(BigDecimal.ZERO)) {
            log.debug("Bid price with commission has not been provided for FX price feed record with id: {} and instrument name: {}", fxPriceFeedRecordItem.getId(), fxPriceFeedRecordItem.getInstrumentName());
            validationMessage.append("Bid price with commission has not been provided for FX price feed record with id: ")
                    .append(fxPriceFeedRecordItem.getId())
                    .append(" and instrument name: ")
                    .append(fxPriceFeedRecordItem.getInstrumentName())
                    .append("; ");
            isRecordValid = false;
        }
        if (!isRecordValid){
            fxPriceFeedRecordItem.setValidationMessage(fxPriceFeedRecordItem.getValidationMessage()!= null ? fxPriceFeedRecordItem.getValidationMessage() + validationMessage : validationMessage.toString());
        }
        return isRecordValid;
    }


}
