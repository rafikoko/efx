package com.santander.efx.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
//Thanks to Lombok, all boilerplate code (like constructor, getters, setters) can be avoided to keep code shorter and cleaner
@NoArgsConstructor
@Data
public class FxPriceFeedRecord {

    @Id
    private long id;

    //Keeping instrument name based on simplistic data model.
    //In real life, we might want to consider storing references to both Products/Assets being traded
    private String instrumentName;
    @Column(precision=20, scale = 10)
    private BigDecimal bid;
    @Column(precision=20, scale = 10)
    private BigDecimal ask;

    //Assuming for simplicity that we keep prices with applied commission within the same entity
    //Though, it could be kept in separated dedicated object in real life use case for clearer separation of original data vs. processed data
    @Column(precision=20, scale = 10)
    private BigDecimal bidWithCommission;
    @Column(precision=20, scale = 10)
    private BigDecimal askWithCommission;
    @Column(name = "Timestamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;

    //validity status, as the intention is to store all received data, but be able to filter invalid records, if needed
    private boolean valid = true;

    String validationMessage;

}
