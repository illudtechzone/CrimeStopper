package com.illud.crimestopper.service.mapper;

import com.illud.crimestopper.domain.*;
import com.illud.crimestopper.service.dto.ComplaintDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Complaint} and its DTO {@link ComplaintDTO}.
 */
@Mapper(componentModel = "spring", uses = {AuthorityMapper.class})
public interface ComplaintMapper extends EntityMapper<ComplaintDTO, Complaint> {


    @Mapping(target = "media", ignore = true)
    @Mapping(target = "removeMedia", ignore = true)
    @Mapping(target = "removeAuthority", ignore = true)
    Complaint toEntity(ComplaintDTO complaintDTO);

    default Complaint fromId(Long id) {
        if (id == null) {
            return null;
        }
        Complaint complaint = new Complaint();
        complaint.setId(id);
        return complaint;
    }
}
