package com.santander.efx.service;

import com.santander.efx.entity.FxPriceFeedRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.santander.efx.validator.FxPriceFeedValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class FxPriceFeedSubscriber implements Subscriber {

    FxPriceFeedService fxPriceFeedService;
    final String COMMA_DELIMITER = ",";

    @Autowired
    public void setFxPriceFeedService(FxPriceFeedService fxPriceFeedService) {
        this.fxPriceFeedService = fxPriceFeedService;
    }

    @Override
    public void onMessage(String message){
        System.out.println("Received message:" + message );

        if (message!=null && !message.trim().isEmpty()){
            List<FxPriceFeedRecord> feed = new ArrayList<>();
            Scanner scanner = new Scanner(message);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                FxPriceFeedRecord record = getRecordFromLine(line);
                if (record != null) {
                    feed.add(record);
                }
            }
            fxPriceFeedService.saveFxPriceFeed(feed);
        }
        else {
            log.error("Received message is empty");
        }

        fxPriceFeedService.loadAll().stream().forEach(System.out::println);
    }

    FxPriceFeedRecord getRecordFromLine(String line){
        String[] values = line.split(COMMA_DELIMITER);
        if (values == null || values.length!=5) {
            return null;
        }
        FxPriceFeedRecord record = new FxPriceFeedRecord();
        boolean validRecord = true;
        try {
            record.setId(Long.parseLong(values[0]));
        }
        catch(NumberFormatException nfe){
            validRecord=false;

            log.error("Unable to parse Id for FxPriceRecord. Received value: " + values[0]);
        }

        record.setInstrumentName(values[1]!=null? values[1].trim() : null);
        if (record.getInstrumentName() == null || record.getInstrumentName().isEmpty()) {
            validRecord=false;
            log.error("Unable to parse Instrument Name for FxPriceRecord with Id: {}. Received value: {}", values[0], values[1]);
        }

        try {
            record.setBid(BigDecimal.valueOf(Double.parseDouble(values[2])));
        }
        catch(NumberFormatException nfe){
            validRecord=false;
            log.error("Unable to parse Bid price for FxPriceRecord with Id: {}. Received value: {}", values[0], values[2]);
        }

        try {
            record.setAsk(BigDecimal.valueOf(Double.parseDouble(values[3])));
        }
        catch(NumberFormatException nfe){
            validRecord=false;
            log.error("Unable to parse Ask price for FxPriceRecord with Id: {}. Received value: {}", values[0], values[3]);
        }

        try {
            //given time format provided for records, assuming we do not take zones into account
            record.setTimestamp(LocalDateTime.parse(values[4], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        }
        catch(DateTimeParseException nfe){
            validRecord=false;
            log.error("Unable to parse Timestamp for FxPriceRecord with Id: {}. Received value: {}", values[0], values[4]);
        }

        record.setValid(validRecord || FxPriceFeedValidator.validateFxPriceFeedRecord(record));

        return record;
    }


}
