package com.santander.efx;

import com.santander.efx.entity.FxPriceFeedRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {


    public static List<FxPriceFeedRecord> createBaseFxPriceFeedRecords(){
        List<FxPriceFeedRecord> result = new ArrayList<>();

        FxPriceFeedRecord record = new FxPriceFeedRecord();
        record.setId(107l);
        record.setInstrumentName("EUR/JPY");
        record.setBid(new BigDecimal("119.6"));
        record.setAsk(new BigDecimal("119.9"));
        record.setBidWithCommission(new BigDecimal("119.7196"));
        record.setAskWithCommission(new BigDecimal("119.7801"));
        record.setTimestamp(LocalDateTime.parse("01-06-2020 12:01:02:002", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        result.add(record);

        FxPriceFeedRecord record2 = new FxPriceFeedRecord();
        record2.setId(123l);
        record2.setInstrumentName("EUR/USD");
        record2.setBid(new BigDecimal("1.23"));
        record2.setAsk(new BigDecimal("1.25"));
        record2.setBidWithCommission(new BigDecimal("1.23123"));
        record2.setAskWithCommission(new BigDecimal("1.24875"));
        record2.setTimestamp(LocalDateTime.parse("01-06-2020 12:01:01:001", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        result.add(record2);

        FxPriceFeedRecord record3 = new FxPriceFeedRecord();
        record3.setId(110l);
        record3.setInstrumentName("EUR/JPY");
        record3.setBid(new BigDecimal("119.61"));
        record3.setAsk(new BigDecimal("119.91"));
        record3.setBidWithCommission(new BigDecimal("119.72961"));
        record3.setAskWithCommission(new BigDecimal("119.79009"));
        record3.setTimestamp(LocalDateTime.parse("01-06-2020 12:01:02:110", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        result.add(record3);

//        FxPriceFeedRecord record4 = new FxPriceFeedRecord();
//        record4.setId(110l);
//        record4.setInstrumentName("EUR/JPY");
//        record4.setBid(new BigDecimal("119.61"));
//        record4.setAsk(new BigDecimal("119.91"));
//        record4.setBidWithCommission(new BigDecimal("119.72961"));
//        record4.setAskWithCommission(new BigDecimal("119.79009"));
//        record4.setTimestamp(LocalDateTime.parse("01-06-2020 12:01:02:110", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
//        result.add(record4);

        return result;
    }
}
