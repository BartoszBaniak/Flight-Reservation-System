package com.reservation.system.passenger;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PassengerValidationTest {

    @Autowired
    private PassengerService passengerService;

    private final PassengerDto validPassengerDto = PassengerDto.builder()
            .firstName("Jan")
            .lastName("Kowalski")
            .email("jan.kowalski@example.com")
            .phoneNumber("123456789")
            .build();


    @Test
    void createPassenger_ShouldThrowException_WhenEmailFormatIsInvalid() {
        // given
        PassengerDto invalid = validPassengerDto.toBuilder().email("invalid-email").build();

        // when, then
        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void createPassenger_ShouldThrowException_WhenEmailIsEmpty() {
        // given
        PassengerDto invalid = validPassengerDto.toBuilder().email("").build();

        // when, then
        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Email is required.");
    }

    @Test
    void createPassenger_ShouldThrowException_WhenPhoneIsEmpty() {
        PassengerDto invalid = validPassengerDto.toBuilder().phoneNumber("").build();

        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Phone number must consist of exactly 9 digits.");
    }

    @Test
    void createPassenger_ShouldThrowException_WhenPhoneIsTooShort() {
        PassengerDto invalid = validPassengerDto.toBuilder().phoneNumber("123").build();

        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Phone number must consist of exactly 9 digits.");
    }

    @Test
    void createPassenger_ShouldThrowException_WhenFirstNameIsBlank() {
        PassengerDto invalid = validPassengerDto.toBuilder().firstName(" ").build();

        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("First name is required.");
    }

    @Test
    void createPassenger_ShouldThrowException_WhenLastNameIsBlank() {
        PassengerDto invalid = validPassengerDto.toBuilder().lastName("").build();

        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Last name is required.");
    }

    @Test
    void createPassenger_ShouldThrowException_WhenEmailIsTooLong() {
        String longEmail = "a".repeat(100) + "@example.com";
        PassengerDto invalid = validPassengerDto.toBuilder().email(longEmail).build();

        assertThatThrownBy(() -> passengerService.createPassenger(invalid))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Invalid email format.");
    }


}
