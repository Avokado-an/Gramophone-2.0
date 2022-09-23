package com.anton.gramophone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"subscriptions", "subscribers"})
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private boolean isEnabled;
    private String email;
    private String profilePicture;
    private String status;
    private LocalDateTime birthDay;//todo do it some time later

    @ManyToMany(mappedBy = "participants")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Instrument> instruments;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Post> posts;

    @CollectionTable(name = "Gender", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToMany
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "subscriptions")
    private List<User> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> subscribers;

    public boolean addSubscriber(User user) {
        return subscribers.add(user);
    }

    public boolean removeSubscriber(User user) {
        return subscribers.remove(user);
    }

    public boolean addSubscription(User user) {
        return subscriptions.add(user);
    }

    public boolean removeSubscription(User user) {
        return subscriptions.remove(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean addInstrument(Instrument instrument) {
        return instruments.add(instrument);
    }

    public boolean removeInstrument(Instrument instrument) {
        return instruments.remove(instrument);
    }

    public void removeInstrument(Long id) {
        instruments.removeIf(i -> i.getId().equals(id));
    }

    public void replaceInstrument(Instrument instrument) {
        removeInstrument(instrument.getId());
        instruments.add(instrument);
    }
}
