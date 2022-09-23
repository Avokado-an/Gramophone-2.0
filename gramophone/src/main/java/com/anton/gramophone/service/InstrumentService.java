package com.anton.gramophone.service;

import com.anton.gramophone.entity.Instrument;

public interface InstrumentService {
    void save(Instrument instrument);

    void update(Instrument instrument);

    void remove(Long id);
}
