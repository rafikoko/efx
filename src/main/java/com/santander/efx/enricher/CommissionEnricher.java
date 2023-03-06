package com.santander.efx.enricher;

import com.santander.efx.entity.FxPriceFeedRecord;
import com.santander.efx.validator.FxPriceFeedValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommissionEnricher {

    public List<FxPriceFeedRecord> enrichFxPriceFeedRecordsWithCommission(List<FxPriceFeedRecord> records, BigDecimal commissionPercent){
        return records.stream().map(record -> enrichFxPriceFeedRecordWithCommission(record, commissionPercent)).collect(Collectors.toList());
    }

    public FxPriceFeedRecord enrichFxPriceFeedRecordWithCommission(FxPriceFeedRecord record, BigDecimal commissionPercent){
        /*calculation is based on assumptions separately described in document and sent
        Baseline assumption:
        Bid is a lower price, normally a buy price - the maximum price that a buyer is willing to pay for an asset.
        Ask price is a higher price, normally a selling price - the minimum price that seller is willing to take for its asset.
        Thus:
        Adding commission to Bid
        Deducting commission from Ask
         */

        //making assumption about precision, given BigDecimal value is initialized with double value which has limitation about scale
        MathContext mc = new MathContext(15);

        //dividing by 100 to turn % input into multiplicand
        BigDecimal multiplicand = commissionPercent.movePointLeft(2);
        record.setBidWithCommission(record.getBid().add(record.getBid().multiply(multiplicand, mc), mc) );
        record.setAskWithCommission(record.getAsk().subtract(record.getAsk().multiply(multiplicand, mc), mc) );

        //assuming that invalid value is not blocking, given lack of requirements in this context
        FxPriceFeedValidator.validatePricesWithCommissionInFxPriceFeedRecord(record);

        return record;
    }
}
