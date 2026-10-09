package com.tuncayturk.storelocator.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.tuncayturk.storelocator.model.Store;
import org.junit.jupiter.api.Test;

class StoreJsonMapperTest {

    @Test
    void shouldMapValidEntryToDomainStore() {
        Store store = StoreJsonMapper.toDomain(validEntry());

        assertThat(store.id()).isEqualTo("uuid-1");
        assertThat(store.name()).isEqualTo("Jumbo Test");
        assertThat(store.city()).isEqualTo("Utrecht");
        assertThat(store.postalCode()).isEqualTo("3511 AB");
        assertThat(store.street()).isEqualTo("Street");
        assertThat(store.houseNumber()).isEqualTo("12");
        assertThat(store.location().latitude()).isEqualTo(52.0907);
        assertThat(store.location().longitude()).isEqualTo(5.1214);
    }

    @Test
    void shouldThrowIllegalArgumentException_givenMissingUuid() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> StoreJsonMapper.toDomain(withUuid()))
                .withMessageContaining("uuid");
    }

    @Test
    void shouldThrowIllegalArgumentException_givenBlankLatitude() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> StoreJsonMapper.toDomain(withLatitude()))
                .withMessageContaining("latitude");
    }

    @Test
    void shouldThrowIllegalArgumentException_givenNonNumericLongitude() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> StoreJsonMapper.toDomain(withLongitude()))
                .withMessageContaining("longitude");
    }

    private static StoreJson validEntry() {
        return new StoreJson("uuid-1", "Jumbo Test", "Utrecht", "3511 AB", "Street", "12",
                "52.090700", "5.121400");
    }

    private static StoreJson withUuid() {
        StoreJson base = validEntry();
        return new StoreJson(null, base.addressName(), base.city(), base.postalCode(), base.street(), base.street2(),
                base.latitude(), base.longitude());
    }

    private static StoreJson withLatitude() {
        StoreJson base = validEntry();
        return new StoreJson(base.uuid(), base.addressName(), base.city(), base.postalCode(), base.street(), base.street2(),
                " ", base.longitude());
    }

    private static StoreJson withLongitude() {
        StoreJson base = validEntry();
        return new StoreJson(base.uuid(), base.addressName(), base.city(), base.postalCode(), base.street(), base.street2(),
                base.latitude(), "not-a-number");
    }
}
