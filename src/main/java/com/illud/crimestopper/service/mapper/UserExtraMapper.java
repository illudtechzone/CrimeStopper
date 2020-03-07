package com.illud.crimestopper.service.mapper;

import com.illud.crimestopper.domain.*;
import com.illud.crimestopper.service.dto.UserExtraDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {



    default UserExtra fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserExtra userExtra = new UserExtra();
        userExtra.setId(id);
        return userExtra;
    }
}
