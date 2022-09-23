package com.anton.gramophone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Likable {
    protected int likes;

    public int like() {
        likes++;
        return likes;
    }

    public int removeLike() {
        likes--;
        return likes;
    }
}
