package com.illud.crimestopper.web.rest;

import com.illud.crimestopper.CrimeStopperApp;
import com.illud.crimestopper.config.TestSecurityConfiguration;
import com.illud.crimestopper.domain.Complaint;
import com.illud.crimestopper.repository.ComplaintRepository;
import com.illud.crimestopper.repository.search.ComplaintSearchRepository;
import com.illud.crimestopper.service.ComplaintService;
import com.illud.crimestopper.service.dto.ComplaintDTO;
import com.illud.crimestopper.service.mapper.ComplaintMapper;
import com.illud.crimestopper.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.illud.crimestopper.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ComplaintResource} REST controller.
 */
@SpringBootTest(classes = {CrimeStopperApp.class, TestSecurityConfiguration.class})
public class ComplaintResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_IDP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_USER_IDP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ComplaintRepository complaintRepository;

    @Mock
    private ComplaintRepository complaintRepositoryMock;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Mock
    private ComplaintService complaintServiceMock;

    @Autowired
    private ComplaintService complaintService;

    /**
     * This repository is mocked in the com.illud.crimestopper.repository.search test package.
     *
     * @see com.illud.crimestopper.repository.search.ComplaintSearchRepositoryMockConfiguration
     */
    @Autowired
    private ComplaintSearchRepository mockComplaintSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restComplaintMockMvc;

    private Complaint complaint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComplaintResource complaintResource = new ComplaintResource(complaintService);
        this.restComplaintMockMvc = MockMvcBuilders.standaloneSetup(complaintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complaint createEntity(EntityManager em) {
        Complaint complaint = new Complaint()
            .description(DEFAULT_DESCRIPTION)
            .userIdpCode(DEFAULT_USER_IDP_CODE)
            .address(DEFAULT_ADDRESS)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .createdOn(DEFAULT_CREATED_ON);
        return complaint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complaint createUpdatedEntity(EntityManager em) {
        Complaint complaint = new Complaint()
            .description(UPDATED_DESCRIPTION)
            .userIdpCode(UPDATED_USER_IDP_CODE)
            .address(UPDATED_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .createdOn(UPDATED_CREATED_ON);
        return complaint;
    }

    @BeforeEach
    public void initTest() {
        complaint = createEntity(em);
    }

    @Test
    @Transactional
    public void createComplaint() throws Exception {
        int databaseSizeBeforeCreate = complaintRepository.findAll().size();

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isCreated());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeCreate + 1);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testComplaint.getUserIdpCode()).isEqualTo(DEFAULT_USER_IDP_CODE);
        assertThat(testComplaint.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testComplaint.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testComplaint.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testComplaint.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);

        // Validate the Complaint in Elasticsearch
        verify(mockComplaintSearchRepository, times(1)).save(testComplaint);
    }

    @Test
    @Transactional
    public void createComplaintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = complaintRepository.findAll().size();

        // Create the Complaint with an existing ID
        complaint.setId(1L);
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeCreate);

        // Validate the Complaint in Elasticsearch
        verify(mockComplaintSearchRepository, times(0)).save(complaint);
    }


    @Test
    @Transactional
    public void getAllComplaints() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList
        restComplaintMockMvc.perform(get("/api/complaints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaint.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userIdpCode").value(hasItem(DEFAULT_USER_IDP_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllComplaintsWithEagerRelationshipsIsEnabled() throws Exception {
        ComplaintResource complaintResource = new ComplaintResource(complaintServiceMock);
        when(complaintServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restComplaintMockMvc = MockMvcBuilders.standaloneSetup(complaintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restComplaintMockMvc.perform(get("/api/complaints?eagerload=true"))
        .andExpect(status().isOk());

        verify(complaintServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllComplaintsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ComplaintResource complaintResource = new ComplaintResource(complaintServiceMock);
            when(complaintServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restComplaintMockMvc = MockMvcBuilders.standaloneSetup(complaintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restComplaintMockMvc.perform(get("/api/complaints?eagerload=true"))
        .andExpect(status().isOk());

            verify(complaintServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get the complaint
        restComplaintMockMvc.perform(get("/api/complaints/{id}", complaint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(complaint.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.userIdpCode").value(DEFAULT_USER_IDP_CODE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComplaint() throws Exception {
        // Get the complaint
        restComplaintMockMvc.perform(get("/api/complaints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Update the complaint
        Complaint updatedComplaint = complaintRepository.findById(complaint.getId()).get();
        // Disconnect from session so that the updates on updatedComplaint are not directly saved in db
        em.detach(updatedComplaint);
        updatedComplaint
            .description(UPDATED_DESCRIPTION)
            .userIdpCode(UPDATED_USER_IDP_CODE)
            .address(UPDATED_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .createdOn(UPDATED_CREATED_ON);
        ComplaintDTO complaintDTO = complaintMapper.toDto(updatedComplaint);

        restComplaintMockMvc.perform(put("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isOk());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComplaint.getUserIdpCode()).isEqualTo(UPDATED_USER_IDP_CODE);
        assertThat(testComplaint.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testComplaint.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testComplaint.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testComplaint.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);

        // Validate the Complaint in Elasticsearch
        verify(mockComplaintSearchRepository, times(1)).save(testComplaint);
    }

    @Test
    @Transactional
    public void updateNonExistingComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintMockMvc.perform(put("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Complaint in Elasticsearch
        verify(mockComplaintSearchRepository, times(0)).save(complaint);
    }

    @Test
    @Transactional
    public void deleteComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        int databaseSizeBeforeDelete = complaintRepository.findAll().size();

        // Delete the complaint
        restComplaintMockMvc.perform(delete("/api/complaints/{id}", complaint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Complaint in Elasticsearch
        verify(mockComplaintSearchRepository, times(1)).deleteById(complaint.getId());
    }

    @Test
    @Transactional
    public void searchComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);
        when(mockComplaintSearchRepository.search(queryStringQuery("id:" + complaint.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(complaint), PageRequest.of(0, 1), 1));
        // Search the complaint
        restComplaintMockMvc.perform(get("/api/_search/complaints?query=id:" + complaint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaint.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userIdpCode").value(hasItem(DEFAULT_USER_IDP_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }
}
