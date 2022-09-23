package com.anton.gramophone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@ToString(exclude = "participants")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "ChatRoom")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "chatRoom")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Message> messages;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    @JoinTable(
            name = "ChatRoom_Users",
            joinColumns = {@JoinColumn(name = "hatRoom_id")},
            inverseJoinColumns = {@JoinColumn(name = "Users_id")}
    )
    private Set<User> participants;

    private String name;
    private String pictureReference;
}
