package com.anton.gramophone.service.specification;

import com.anton.gramophone.entity.Instrument;
import com.anton.gramophone.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSpecification {
    private UserSpecification() {
    }

    public static Specification<User> userInstrumentsContain(Instrument.InstrumentTypes instrumentName, int skillLevel) {
        if (instrumentName == null) {
            Set<Instrument.InstrumentTypes> allTypes = Arrays
                    .stream(Instrument.InstrumentTypes.values())
                    .collect(Collectors.toSet());
            return (root, query, builder) -> {
                Join<User, Instrument> groupJoin = root.join("instruments");
                return builder.isTrue(groupJoin.get("instrumentName").in(allTypes));
            };
        }
        return (root, query, builder) -> {
            Join<User, Instrument> groupJoin = root.join("instruments");
            return builder.and(
                    builder.isTrue(groupJoin.get("instrumentName").in(instrumentName)),
                    builder.greaterThanOrEqualTo(groupJoin.get("skillLevel"), skillLevel));
        };
    }

    public static Specification<User> firstNameStartsWith(String beginningOfUsername) {
        return (root, query, builder) -> builder.like(root.get("firstName"), beginningOfUsername + "%");
    }

    public static Specification<User> lastNameStartsWith(String beginningOfUsername) {
        return (root, query, builder) -> builder.like(root.get("lastName"), beginningOfUsername + "%");
    }
}
