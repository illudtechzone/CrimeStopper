package com.illud.crimestopper.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.illud.crimestopper.web.rest.TestUtil;

public class ComplaintTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Complaint.class);
        Complaint complaint1 = new Complaint();
        complaint1.setId(1L);
        Complaint complaint2 = new Complaint();
        complaint2.setId(complaint1.getId());
        assertThat(complaint1).isEqualTo(complaint2);
        complaint2.setId(2L);
        assertThat(complaint1).isNotEqualTo(complaint2);
        complaint1.setId(null);
        assertThat(complaint1).isNotEqualTo(complaint2);
    }
}
