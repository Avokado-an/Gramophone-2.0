package com.anton.gramophone.repository;

import com.anton.gramophone.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    void removeById(Long id);
}
