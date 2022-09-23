package com.anton.gramophone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "user")
@Entity
@Table(name = "Instrument")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum InstrumentTypes {
        GUITAR, PIANO, VOCAL, BASS, CELLO, DRUMS, FLUTE, SAXOPHONE
    }

    private int skillLevel;
    private InstrumentTypes instrumentName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    @JsonIgnore
    private User user;

    @ElementCollection(targetClass = MusicGenre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Genre", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    Set<MusicGenre> genres;
}
