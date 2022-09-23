package com.anton.gramophone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Comments")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "post")
public class Comment extends Likable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;
    private LocalDateTime creationTime;
    private Long ownerId;
    private String fileReference;
    private String photoReference;

    @ManyToOne
    @JsonIgnore
    private Post post;
}
