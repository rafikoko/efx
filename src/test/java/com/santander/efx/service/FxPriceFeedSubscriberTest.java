package com.santander.efx.service;

import com.santander.efx.entity.FxPriceFeedRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class FxPriceFeedSubscriberTest {

    @MockBean
    FxPriceFeedService fxPriceFeedService;

    @Captor
    ArgumentCaptor<List<FxPriceFeedRecord>> captor;

    FxPriceFeedSubscriber fxPriceFeedSubscriber;

    private MathContext mc;

    @BeforeEach
    void setUp() {
        fxPriceFeedSubscriber = new FxPriceFeedSubscriber();
        fxPriceFeedSubscriber.setFxPriceFeedService(fxPriceFeedService);
        //making assumption about precision, given BigDecimal value is initialized with double value which has limitation about scale
        mc = new MathContext(15);
    }

    @Test
    void onMessage() {
        //given
        final String message = """
                106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
                108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
                109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100
                110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110
                """;

        //when
        fxPriceFeedSubscriber.onMessage(message);
        Mockito.verify(fxPriceFeedService, Mockito.times(1)).saveFxPriceFeed(captor.capture());

        List<FxPriceFeedRecord> listToBeSaved = captor.getValue();
        //simplified evaluation, given we already check parsing separately in subsequent tests
        assertEquals(5, listToBeSaved.size());
        assertEquals(5, listToBeSaved.stream().filter(record -> record.isValid()).toList().size());
    }

    @Test
    void onMessage_InvalidRecord() {
        //given
        final String message = """
                111, , 119.61,119.91,01-06-2020 12:01:02:110
                106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
                108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
                109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100
                110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110
                """;

        //when
        fxPriceFeedSubscriber.onMessage(message);
        Mockito.verify(fxPriceFeedService, Mockito.times(1)).saveFxPriceFeed(captor.capture());

        List<FxPriceFeedRecord> listToBeSaved = captor.getValue();
        //simplified evaluation, given we already check parsing separately in subsequent tests
        assertEquals(6, listToBeSaved.size());
        assertEquals(5, listToBeSaved.stream().filter(record -> record.isValid()).toList().size());
        assertEquals(1, listToBeSaved.stream().filter(record -> !record.isValid()).toList().size());
    }

    @Test
    void getRecordFromLine(){
        //given
        final String messageLine = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertEquals(106, record.getId());
        assertNotNull(record.getInstrumentName());
        assertEquals("EUR/USD", record.getInstrumentName());
        assertNotNull(record.getBid());
        assertTrue(new BigDecimal(1.1, mc).compareTo(record.getBid())==0);
        assertNotNull(record.getAsk());
        assertTrue(new BigDecimal(1.2, mc).compareTo(record.getAsk())==0);
        assertNotNull(record.getTimestamp());
        assertEquals("01-06-2020 12:01:01:001", record.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));

    }

    @Test
    void getRecordFromLine_InvalidId(){
        //given
        final String messageLine = ", EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertFalse(record.isValid());
        assertNotNull(record.getInstrumentName());
        assertEquals("EUR/USD", record.getInstrumentName());
        assertNotNull(record.getBid());
        assertTrue(new BigDecimal(1.1, mc).compareTo(record.getBid())==0);
        assertNotNull(record.getAsk());
        assertTrue(new BigDecimal(1.2, mc).compareTo(record.getAsk())==0);
        assertNotNull(record.getTimestamp());
        assertEquals("01-06-2020 12:01:01:001", record.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        assertNotNull(record.getValidationMessage());
        assertEquals("Id has not been provided for FX price feed record for instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void getRecordFromLine_InvalidInstrumentName(){
        //given
        final String messageLine = "106, , 1.1000,1.2000,01-06-2020 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertFalse(record.isValid());
        assertEquals(106, record.getId());
        assertNotNull(record.getInstrumentName());
        assertEquals("", record.getInstrumentName());
        assertNotNull(record.getBid());
        assertTrue(new BigDecimal(1.1, mc).compareTo(record.getBid())==0);
        assertNotNull(record.getAsk());
        assertTrue(new BigDecimal(1.2, mc).compareTo(record.getAsk())==0);
        assertNotNull(record.getTimestamp());
        assertEquals("01-06-2020 12:01:01:001", record.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        assertNotNull(record.getValidationMessage());
        assertEquals("Instrument name has not been provided for FX price feed record with id: 106; ", record.getValidationMessage());
    }

    @Test
    void getRecordFromLine_InvalidBid(){
        //given
        final String messageLine = "106, EUR/USD, ,1.2000,01-06-2020 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertFalse(record.isValid());
        assertEquals(106, record.getId());
        assertNotNull(record.getInstrumentName());
        assertEquals("EUR/USD", record.getInstrumentName());
        assertNull(record.getBid());
        assertNotNull(record.getAsk());
        assertTrue(new BigDecimal(1.2, mc).compareTo(record.getAsk())==0);
        assertNotNull(record.getTimestamp());
        assertEquals("01-06-2020 12:01:01:001", record.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        assertNotNull(record.getValidationMessage());
        assertEquals("Bid price has not been provided for FX price feed record with id: 106 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void getRecordFromLine_InvalidAsk(){
        //given
        final String messageLine = "106, EUR/USD, 1.1000,aaa,01-06-2020 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertFalse(record.isValid());
        assertEquals(106, record.getId());
        assertNotNull(record.getInstrumentName());
        assertEquals("EUR/USD", record.getInstrumentName());
        assertNotNull(record.getBid());
        assertTrue(new BigDecimal(1.1, mc).compareTo(record.getBid())==0);
        assertNull(record.getAsk());
        assertNotNull(record.getTimestamp());
        assertEquals("01-06-2020 12:01:01:001", record.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        assertNotNull(record.getValidationMessage());
        assertEquals("Ask price has not been provided for FX price feed record with id: 106 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void getRecordFromLine_InvalidTimestamp(){
        //given
        final String messageLine = "106, EUR/USD, 1.1000,1.2000,01-06- 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertFalse(record.isValid());
        assertEquals(106, record.getId());
        assertNotNull(record.getInstrumentName());
        assertEquals("EUR/USD", record.getInstrumentName());
        assertNotNull(record.getBid());
        assertTrue(new BigDecimal(1.1, mc).compareTo(record.getBid())==0);
        assertNotNull(record.getAsk());
        assertTrue(new BigDecimal(1.2, mc).compareTo(record.getAsk())==0);
        assertNull(record.getTimestamp());
        assertNotNull(record.getValidationMessage());
        assertEquals("Timestamp has not been provided for FX price feed record with id: 106 and instrument name: EUR/USD; ", record.getValidationMessage());
    }

    @Test
    void getRecordFromLine_InvalidMultipleValues(){
        //given
        final String messageLine = ", EUR/USD, ,1.2000,01-06-2020 12:01:01:001";

        //when
        FxPriceFeedRecord record = fxPriceFeedSubscriber.getRecordFromLine(messageLine);

        //then
        assertNotNull(record);
        assertFalse(record.isValid());
        assertNotNull(record.getInstrumentName());
        assertEquals("EUR/USD", record.getInstrumentName());
        assertNull(record.getBid());
        assertNotNull(record.getAsk());
        assertTrue(new BigDecimal(1.2, mc).compareTo(record.getAsk())==0);
        assertNotNull(record.getTimestamp());
        assertEquals("01-06-2020 12:01:01:001", record.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")));
        assertNotNull(record.getValidationMessage());
        assertEquals("Id has not been provided for FX price feed record for instrument name: EUR/USD; Bid price has not been provided for FX price feed record with id: 0 and instrument name: EUR/USD; ", record.getValidationMessage());
    }
}