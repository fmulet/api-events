package com.gestion.eventos.api.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.repository.RoleRepository;
import com.gestion.eventos.api.security.dto.RegisterDto;

/**
 * EventMapper
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", source = "registerDto.roles", qualifiedByName = "mapRoleStringsToRoles")
    public abstract User registerDtoToUser(RegisterDto registerDto);

    @Named("mapRoleStringsToRoles")
    public Set<Role> mapRoleStringsToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return roleRepository.findByName("ROLE_USER")
                    .map(Collections::singleton)
                    .orElseThrow(() -> new RuntimeException("No se encontró el rol por defecto"));
        }
        return roleNames.stream().map(roleName -> roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("No se encontró el rol " + roleName)))
                .collect(Collectors.toSet()); // @formatter:off

}

}
