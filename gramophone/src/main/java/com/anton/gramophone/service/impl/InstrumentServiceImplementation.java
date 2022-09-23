package com.anton.gramophone.service.impl;

import com.anton.gramophone.entity.Instrument;
import com.anton.gramophone.repository.InstrumentRepository;
import com.anton.gramophone.service.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImplementation implements InstrumentService {
    private final InstrumentRepository instrumentRepository;

    @Override
    public void save(Instrument instrument) {
        instrumentRepository.save(instrument);
    }

    @Override
    @Transactional
    public void update(Instrument instrument) {
        Optional<Instrument> redactedInstrument = instrumentRepository.findById(instrument.getId());
        if (redactedInstrument.isPresent()) {
            redactedInstrument.get().setSkillLevel(instrument.getSkillLevel());
            redactedInstrument.get().setInstrumentName(instrument.getInstrumentName());
            redactedInstrument.get().setGenres(instrument.getGenres());
            instrumentRepository.save(redactedInstrument.get());
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        instrumentRepository.removeById(id);
    }
}
