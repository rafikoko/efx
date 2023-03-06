package com.santander.efx.rest;

import com.santander.efx.entity.FxPriceFeedRecord;
import com.santander.efx.repository.FxPriceFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/fx")
public class FxPriceController {

    @Autowired
    FxPriceFeedRepository fxPriceFeedRepository;

    /**
     * Endpoint to retrieve latest price record for given instrument
     * @param instrumentName Name of istrument
     * @return Latest Fx Price Record for given instrument name. If unavailable, then returns {@link HttpStatus.NO_CONTENT}
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FxPriceFeedRecord getLatestPriceByInstrumentName(@RequestParam("instrumentName") String instrumentName){
        FxPriceFeedRecord result = fxPriceFeedRepository.findTopByInstrumentNameOrderByTimestampDesc(instrumentName);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Unable to find instrument with name: " + instrumentName);
        }
        return result;
    }

}
