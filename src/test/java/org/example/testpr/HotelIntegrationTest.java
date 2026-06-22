package org.example.testpr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testpr.dto.AddressDTO;
import org.example.testpr.dto.ArrivalTimeDTO;
import org.example.testpr.dto.ContactDTO;
import org.example.testpr.dto.HotelCreateRequestDTO;
import org.example.testpr.model.Address;
import org.example.testpr.model.Hotel;
import org.example.testpr.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class HotelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        hotelRepository.deleteAll();
    }


    @Test
    void createHotel_shouldReturn201() throws Exception {

        HotelCreateRequestDTO dto = new HotelCreateRequestDTO();
        dto.setName("Integration Hotel");
        dto.setBrand("TestBrand");

        AddressDTO address = new AddressDTO();
        address.setCountry("Italy");
        address.setCity("Rome");
        address.setStreet("Street");
        address.setHouseNumber(1);
        address.setPostCode("00100");

        ContactDTO contact = new ContactDTO();
        contact.setEmail("test@mail.com");
        contact.setPhone("+123456789");

        ArrivalTimeDTO arrival = new ArrivalTimeDTO();
        arrival.setCheckIn("14:00");
        arrival.setCheckOut("12:00");

        dto.setAddress(address);
        dto.setContacts(contact);
        dto.setArrivalTime(arrival);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Hotel"))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andExpect(jsonPath("$.address").value("1 Street, Rome, Italy"))
                .andExpect(jsonPath("$.phone").value("+123456789"));
    }


    @Test
    void search_shouldReturnMatchingHotels() throws Exception {

        Hotel hotel = Hotel.builder()
                .name("Hilton")
                .brand("Brand")
                .deleted(false)
                .build();

        hotelRepository.save(hotel);

        mockMvc.perform(get("/property-view/search")
                        .param("name", "Hil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hilton"));
    }


    @Test
    void histogram_city_shouldReturnCorrectCount() throws Exception {

        Address address = Address.builder()
                .country("Italy")
                .city("Rome")
                .street("Street")
                .houseNumber("1")
                .build();

        hotelRepository.save(
                Hotel.builder()
                        .name("H1")
                        .brand("B")
                        .address(address)
                        .deleted(false)
                        .build()
        );

        hotelRepository.save(
                Hotel.builder()
                        .name("H2")
                        .brand("B")
                        .address(address)
                        .deleted(false)
                        .build()
        );

        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Rome").value(2));
    }
}
