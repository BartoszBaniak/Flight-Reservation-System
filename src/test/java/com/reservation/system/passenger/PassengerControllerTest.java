//package com.reservation.system.passenger;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//class PassengerControllerTest {
//
//    @Mock
//    private PassengerService passengerService;
//
//    @InjectMocks
//    private PassengerController passengerController;
//
//    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private final PassengerDto validPassengerDto = PassengerDto.builder()
//            .firstName("Jan")
//            .lastName("Kowalski")
//            .email("jan.kowalski@example.com")
//            .phoneNumber("123456789")
//            .build();
//
//    @Test
//    void createPassenger_ShouldReturnPassengerCreateResponse() throws Exception {
//        // given
//        PassengerCreateResponse response = new PassengerCreateResponse("Passenger created successfully", List.of(errors), List.of(warnings));
//        when(passengerService.createPassenger(validPassengerDto)).thenReturn(response);
//
//        // when, then
//        mockMvc.perform(post("/passenger/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validPassengerDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value("Passenger created successfully."));
//    }
//
//    @Test
//    void getPassenger_ShouldReturnPassengerReadResponse() throws Exception {
//        // given
//        PassengerReadResponse response = new PassengerReadResponse("jan.kowalski@example.com", "Jan", "Kowalski", "123456789");
//        when(passengerService.getPassenger(new PassengerIdentifierRequest("jan.kowalski@example.com"))).thenReturn(response);
//
//        // when, then
//        mockMvc.perform(post("/passenger/search")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new PassengerIdentifierRequest("jan.kowalski@example.com"))))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.email").value("jan.kowalski@example.com"));
//    }
//
//    @Test
//    void getAllPassengers_ShouldReturnListOfPassengers() throws Exception {
//        // given
//        PassengerReadResponse response = new PassengerReadResponse("jan.kowalski@example.com", "Jan", "Kowalski", "123456789");
//        when(passengerService.getAllPassengers()).thenReturn(List.of(response));
//
//        // when, then
//        mockMvc.perform(get("/passenger/search-all")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].data.email").value("jan.kowalski@example.com"));
//    }
//
//    @Test
//    void deletePassenger_ShouldReturnPassengerIdentifierResponse() throws Exception {
//        // given
//        PassengerIdentifierResponse response = new PassengerIdentifierResponse("Passenger deleted successfully");
//        when(passengerService.deletePassenger(new PassengerIdentifierRequest("jan.kowalski@example.com"))).thenReturn(response);
//
//        // when, then
//        mockMvc.perform(delete("/passenger/delete")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new PassengerIdentifierRequest("jan.kowalski@example.com"))))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value("Passenger deleted successfully"));
//    }
//
//    @Test
//    void updatePassenger_ShouldReturnUpdatedPassengerResponse() throws Exception {
//        // given
//        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(new PassengerIdentifierRequest("jan.kowalski@example.com"), new PassengerDto("Janek", "Nowak", "jan.kowalski@example.com", "987654321"));
//        PassengerIdentifierResponse response = new PassengerIdentifierResponse("Passenger updated successfully");
//        when(passengerService.updatePassenger(updateRequest)).thenReturn(response);
//
//        // when, then
//        mockMvc.perform(put("/passenger/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value("Passenger updated successfully"));
//    }
//}
