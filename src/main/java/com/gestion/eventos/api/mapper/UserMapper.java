package com.gestion.eventos.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.RegisterDto;

/**
 * EventMapper
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User registerDtoToUser(RegisterDto registerDto);
}
