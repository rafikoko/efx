package com.santander.efx.repository;

import com.santander.efx.entity.FxPriceFeedRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.santander.efx.TestUtils.createBaseFxPriceFeedRecords;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FxPriceFeedRepositoryTest {
    @Autowired
    private FxPriceFeedRepository repository;
    @Test
    void findTopByInstrumentNameOrderByTimestampDesc() {
        //given
        List<FxPriceFeedRecord> records = createBaseFxPriceFeedRecords();
        repository.saveAllAndFlush(records);

        //confirm successful persistence
        assertEquals(3,repository.findAll().size());

        //when
        FxPriceFeedRecord record = repository.findTopByInstrumentNameOrderByTimestampDesc("EUR/JPY");

        //then
        assertNotNull(record);
        assertEquals(110, record.getId());
        assertEquals("2020-06-01 12:01:02:110", record.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
    }

    @Test
    void findTopByInstrumentNameOrderByTimestampDesc_WrongInstrumentName() {
        //given
        List<FxPriceFeedRecord> records = createBaseFxPriceFeedRecords();
        repository.saveAllAndFlush(records);

        //confirm successful persistence
        assertEquals(3,repository.findAll().size());

        //when
        FxPriceFeedRecord record = repository.findTopByInstrumentNameOrderByTimestampDesc("EUR/PLN");

        //then
        assertNull(record);
    }
}