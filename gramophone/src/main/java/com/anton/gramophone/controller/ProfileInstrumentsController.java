package com.anton.gramophone.controller;

import com.anton.gramophone.controller.util.CurrentPrincipalDefiner;
import com.anton.gramophone.entity.Instrument;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.IdDto;
import com.anton.gramophone.entity.dto.InstrumentDto;
import com.anton.gramophone.service.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("profile/instruments")
public class ProfileInstrumentsController {
    private final InstrumentService instrumentService;
    private final CurrentPrincipalDefiner currentPrincipalDefiner;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<Instrument> addInstruments(@RequestBody InstrumentDto instrumentDto) {
        User user = currentPrincipalDefiner.getPrincipal();
        ModelMapper modelMapper = new ModelMapper();
        Instrument instrument = modelMapper.map(instrumentDto, Instrument.class);
        user.addInstrument(instrument);
        instrument.setUser(user);
        instrumentService.save(instrument);
        return user.getInstruments();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<Instrument> redactInstruments(@RequestBody InstrumentDto instrumentDto) {
        User user = currentPrincipalDefiner.getPrincipal();
        ModelMapper modelMapper = new ModelMapper();
        Instrument instrument = modelMapper.map(instrumentDto, Instrument.class);
        user.replaceInstrument(instrument);
        instrumentService.update(instrument);
        return user.getInstruments();
    }

    @DeleteMapping()
    public Set<Instrument> deleteInstruments(@RequestBody IdDto id) {
        User user = currentPrincipalDefiner.getPrincipal();
        user.removeInstrument(id.getId());
        instrumentService.remove(id.getId());
        return user.getInstruments();
    }
}
