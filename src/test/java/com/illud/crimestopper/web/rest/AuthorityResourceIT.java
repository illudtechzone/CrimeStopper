package com.illud.crimestopper.web.rest;

import com.illud.crimestopper.CrimeStopperApp;
import com.illud.crimestopper.config.TestSecurityConfiguration;
import com.illud.crimestopper.domain.Authority;
import com.illud.crimestopper.repository.AuthorityRepository;
import com.illud.crimestopper.repository.search.AuthoritySearchRepository;
import com.illud.crimestopper.service.AuthorityService;
import com.illud.crimestopper.service.dto.AuthorityDTO;
import com.illud.crimestopper.service.mapper.AuthorityMapper;
import com.illud.crimestopper.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
 * Integration tests for the {@link AuthorityResource} REST controller.
 */
@SpringBootTest(classes = {CrimeStopperApp.class, TestSecurityConfiguration.class})
public class AuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHORITY_IDP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORITY_IDP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private AuthorityService authorityService;

    /**
     * This repository is mocked in the com.illud.crimestopper.repository.search test package.
     *
     * @see com.illud.crimestopper.repository.search.AuthoritySearchRepositoryMockConfiguration
     */
    @Autowired
    private AuthoritySearchRepository mockAuthoritySearchRepository;

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

    private MockMvc restAuthorityMockMvc;

    private Authority authority;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorityResource authorityResource = new AuthorityResource(authorityService);
        this.restAuthorityMockMvc = MockMvcBuilders.standaloneSetup(authorityResource)
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
    public static Authority createEntity(EntityManager em) {
        Authority authority = new Authority()
            .name(DEFAULT_NAME)
            .authorityIdpCode(DEFAULT_AUTHORITY_IDP_CODE)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .website(DEFAULT_WEBSITE);
        return authority;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createUpdatedEntity(EntityManager em) {
        Authority authority = new Authority()
            .name(UPDATED_NAME)
            .authorityIdpCode(UPDATED_AUTHORITY_IDP_CODE)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .website(UPDATED_WEBSITE);
        return authority;
    }

    @BeforeEach
    public void initTest() {
        authority = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthority() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);
        restAuthorityMockMvc.perform(post("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
            .andExpect(status().isCreated());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeCreate + 1);
        Authority testAuthority = authorityList.get(authorityList.size() - 1);
        assertThat(testAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuthority.getAuthorityIdpCode()).isEqualTo(DEFAULT_AUTHORITY_IDP_CODE);
        assertThat(testAuthority.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAuthority.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAuthority.getWebsite()).isEqualTo(DEFAULT_WEBSITE);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(1)).save(testAuthority);
    }

    @Test
    @Transactional
    public void createAuthorityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Authority with an existing ID
        authority.setId(1L);
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorityMockMvc.perform(post("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeCreate);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(0)).save(authority);
    }


    @Test
    @Transactional
    public void getAllAuthorities() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);

        // Get all the authorityList
        restAuthorityMockMvc.perform(get("/api/authorities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].authorityIdpCode").value(hasItem(DEFAULT_AUTHORITY_IDP_CODE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }
    
    @Test
    @Transactional
    public void getAuthority() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);

        // Get the authority
        restAuthorityMockMvc.perform(get("/api/authorities/{id}", authority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.authorityIdpCode").value(DEFAULT_AUTHORITY_IDP_CODE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE));
    }

    @Test
    @Transactional
    public void getNonExistingAuthority() throws Exception {
        // Get the authority
        restAuthorityMockMvc.perform(get("/api/authorities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthority() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);

        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Update the authority
        Authority updatedAuthority = authorityRepository.findById(authority.getId()).get();
        // Disconnect from session so that the updates on updatedAuthority are not directly saved in db
        em.detach(updatedAuthority);
        updatedAuthority
            .name(UPDATED_NAME)
            .authorityIdpCode(UPDATED_AUTHORITY_IDP_CODE)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .website(UPDATED_WEBSITE);
        AuthorityDTO authorityDTO = authorityMapper.toDto(updatedAuthority);

        restAuthorityMockMvc.perform(put("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
            .andExpect(status().isOk());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeUpdate);
        Authority testAuthority = authorityList.get(authorityList.size() - 1);
        assertThat(testAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuthority.getAuthorityIdpCode()).isEqualTo(UPDATED_AUTHORITY_IDP_CODE);
        assertThat(testAuthority.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAuthority.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAuthority.getWebsite()).isEqualTo(UPDATED_WEBSITE);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(1)).save(testAuthority);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthority() throws Exception {
        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityMockMvc.perform(put("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(0)).save(authority);
    }

    @Test
    @Transactional
    public void deleteAuthority() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);

        int databaseSizeBeforeDelete = authorityRepository.findAll().size();

        // Delete the authority
        restAuthorityMockMvc.perform(delete("/api/authorities/{id}", authority.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(1)).deleteById(authority.getId());
    }

    @Test
    @Transactional
    public void searchAuthority() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);
        when(mockAuthoritySearchRepository.search(queryStringQuery("id:" + authority.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(authority), PageRequest.of(0, 1), 1));
        // Search the authority
        restAuthorityMockMvc.perform(get("/api/_search/authorities?query=id:" + authority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].authorityIdpCode").value(hasItem(DEFAULT_AUTHORITY_IDP_CODE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }
}
