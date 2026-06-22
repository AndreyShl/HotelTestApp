package org.example.testpr.mapper;

import org.example.testpr.dto.ContactDTO;
import org.example.testpr.model.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactInfoMapper {

    Contacts toEntity(ContactDTO dto);

    ContactDTO toDto(Contacts entity);
}