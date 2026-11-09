package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.dto.RoleDto;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * RoleMapper
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {
  RoleDto toDto(Role role);

  Role toEntity(RoleDto roleDto);

  List<RoleDto> toDtoList(List<Role> roles);
}
