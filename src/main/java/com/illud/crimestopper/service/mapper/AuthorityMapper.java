package com.illud.crimestopper.service.mapper;

import com.illud.crimestopper.domain.*;
import com.illud.crimestopper.service.dto.AuthorityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {


    @Mapping(target = "complaints", ignore = true)
    @Mapping(target = "removeComplaint", ignore = true)
    Authority toEntity(AuthorityDTO authorityDTO);

    default Authority fromId(Long id) {
        if (id == null) {
            return null;
        }
        Authority authority = new Authority();
        authority.setId(id);
        return authority;
    }
}
