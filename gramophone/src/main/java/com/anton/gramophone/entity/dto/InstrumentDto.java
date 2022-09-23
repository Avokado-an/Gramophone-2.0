package com.anton.gramophone.entity.dto;

import com.anton.gramophone.entity.Instrument;
import com.anton.gramophone.entity.MusicGenre;
import lombok.Data;

import java.util.Set;

@Data
public class InstrumentDto {
    private Long id;
    private Instrument.InstrumentTypes instrumentName;
    private int skillLevel;
    private Set<MusicGenre> genres;
}
