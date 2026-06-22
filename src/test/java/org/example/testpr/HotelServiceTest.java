package org.example.testpr;

import org.example.testpr.dto.*;
import org.example.testpr.mapper.HotelMapper;
import org.example.testpr.model.Address;

import org.example.testpr.model.Hotel;
import org.example.testpr.repository.AmenityRepository;
import org.example.testpr.repository.HotelRepository;
import org.example.testpr.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;


    @Test
    void createHotel_shouldSaveHotelAndReturnDto() {

        HotelCreateRequestDTO dto = new HotelCreateRequestDTO();
        dto.setName("Test Hotel");
        dto.setBrand("TestBrand");
        dto.setDescription("Test description");


        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setHouseNumber(1);
        addressDTO.setStreet("Test Street");
        addressDTO.setCity("Test City");
        addressDTO.setCountry("Test Country");
        addressDTO.setPostCode("12345");
        dto.setAddress(addressDTO);


        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setPhone("+123456789");
        contactDTO.setEmail("test@mail.com");
        dto.setContacts(contactDTO);


        ArrivalTimeDTO arrivalTimeDTO = new ArrivalTimeDTO();
        arrivalTimeDTO.setCheckIn("14:00");
        arrivalTimeDTO.setCheckOut("12:00");
        dto.setArrivalTime(arrivalTimeDTO);


        Hotel hotel = Hotel.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .deleted(false)
                .build();

        Hotel saved = Hotel.builder()
                .id(100L)
                .name(dto.getName())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .deleted(false)
                .build();

        HotelShortResponseDTO shortDto = new HotelShortResponseDTO();
        shortDto.setId(saved.getId());
        shortDto.setName(saved.getName());


        when(hotelMapper.toEntity(dto)).thenReturn(hotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(saved);
        when(hotelMapper.toShortDto(saved)).thenReturn(shortDto);


        HotelShortResponseDTO result = hotelService.createHotel(dto);


        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Test Hotel", result.getName());

        verify(hotelRepository).save(any(Hotel.class));
    }


    @Test
    void searchHotels_shouldReturnMappedDtos() {

        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .deleted(false)
                .build();

        HotelShortResponseDTO dto = new HotelShortResponseDTO();
        dto.setName("Hilton");

        when(hotelRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(hotel));

        when(hotelMapper.toShortDto(hotel)).thenReturn(dto);

        List<HotelShortResponseDTO> result =
                hotelService.searchHotels("hil", null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Hilton", result.get(0).getName());
    }

    @Test
    void getHistogram_byCity_shouldReturnCorrectCounts() {

        Address address1 = Address.builder()
                .city("London")
                .build();

        Address address2 = Address.builder()
                .city("Paris")
                .build();

        Hotel h1 = Hotel.builder()
                .address(address1)
                .deleted(false)
                .build();

        Hotel h2 = Hotel.builder()
                .address(address1)
                .deleted(false)
                .build();

        Hotel h3 = Hotel.builder()
                .address(address2)
                .deleted(false)
                .build();

        when(hotelRepository.findAll())
                .thenReturn(List.of(h1, h2, h3));

        Map<String, Long> result = hotelService.getHistogram("city");

        assertEquals(2L, result.get("London"));
        assertEquals(1L, result.get("Paris"));
    }
}

