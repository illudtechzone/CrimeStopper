package com.illud.crimestopper.service.mapper;

import com.illud.crimestopper.domain.*;
import com.illud.crimestopper.service.dto.MediaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring", uses = {ComplaintMapper.class})
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {

    @Mapping(source = "complaint.id", target = "complaintId")
    MediaDTO toDto(Media media);

    @Mapping(source = "complaintId", target = "complaint")
    Media toEntity(MediaDTO mediaDTO);

    default Media fromId(Long id) {
        if (id == null) {
            return null;
        }
        Media media = new Media();
        media.setId(id);
        return media;
    }
}
