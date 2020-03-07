package com.illud.crimestopper.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ComplaintMapperTest {

    private ComplaintMapper complaintMapper;

    @BeforeEach
    public void setUp() {
        complaintMapper = new ComplaintMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(complaintMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(complaintMapper.fromId(null)).isNull();
    }
}
