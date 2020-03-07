package com.illud.crimestopper.web.rest;

import com.illud.crimestopper.CrimeStopperApp;
import com.illud.crimestopper.config.TestSecurityConfiguration;
import com.illud.crimestopper.domain.Media;
import com.illud.crimestopper.repository.MediaRepository;
import com.illud.crimestopper.repository.search.MediaSearchRepository;
import com.illud.crimestopper.service.MediaService;
import com.illud.crimestopper.service.dto.MediaDTO;
import com.illud.crimestopper.service.mapper.MediaMapper;
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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link MediaResource} REST controller.
 */
@SpringBootTest(classes = {CrimeStopperApp.class, TestSecurityConfiguration.class})
public class MediaResourceIT {

    private static final String DEFAULT_FILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_FILE_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private MediaService mediaService;

    /**
     * This repository is mocked in the com.illud.crimestopper.repository.search test package.
     *
     * @see com.illud.crimestopper.repository.search.MediaSearchRepositoryMockConfiguration
     */
    @Autowired
    private MediaSearchRepository mockMediaSearchRepository;

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

    private MockMvc restMediaMockMvc;

    private Media media;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MediaResource mediaResource = new MediaResource(mediaService);
        this.restMediaMockMvc = MockMvcBuilders.standaloneSetup(mediaResource)
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
    public static Media createEntity(EntityManager em) {
        Media media = new Media()
            .fileLink(DEFAULT_FILE_LINK)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return media;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createUpdatedEntity(EntityManager em) {
        Media media = new Media()
            .fileLink(UPDATED_FILE_LINK)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);
        return media;
    }

    @BeforeEach
    public void initTest() {
        media = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedia() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);
        restMediaMockMvc.perform(post("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isCreated());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate + 1);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getFileLink()).isEqualTo(DEFAULT_FILE_LINK);
        assertThat(testMedia.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testMedia.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);

        // Validate the Media in Elasticsearch
        verify(mockMediaSearchRepository, times(1)).save(testMedia);
    }

    @Test
    @Transactional
    public void createMediaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // Create the Media with an existing ID
        media.setId(1L);
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaMockMvc.perform(post("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Media in Elasticsearch
        verify(mockMediaSearchRepository, times(0)).save(media);
    }


    @Test
    @Transactional
    public void getAllMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList
        restMediaMockMvc.perform(get("/api/media?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileLink").value(hasItem(DEFAULT_FILE_LINK)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }
    
    @Test
    @Transactional
    public void getMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get the media
        restMediaMockMvc.perform(get("/api/media/{id}", media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(media.getId().intValue()))
            .andExpect(jsonPath("$.fileLink").value(DEFAULT_FILE_LINK))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get("/api/media/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media
        Media updatedMedia = mediaRepository.findById(media.getId()).get();
        // Disconnect from session so that the updates on updatedMedia are not directly saved in db
        em.detach(updatedMedia);
        updatedMedia
            .fileLink(UPDATED_FILE_LINK)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);
        MediaDTO mediaDTO = mediaMapper.toDto(updatedMedia);

        restMediaMockMvc.perform(put("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getFileLink()).isEqualTo(UPDATED_FILE_LINK);
        assertThat(testMedia.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testMedia.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);

        // Validate the Media in Elasticsearch
        verify(mockMediaSearchRepository, times(1)).save(testMedia);
    }

    @Test
    @Transactional
    public void updateNonExistingMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc.perform(put("/api/media")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Media in Elasticsearch
        verify(mockMediaSearchRepository, times(0)).save(media);
    }

    @Test
    @Transactional
    public void deleteMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeDelete = mediaRepository.findAll().size();

        // Delete the media
        restMediaMockMvc.perform(delete("/api/media/{id}", media.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Media in Elasticsearch
        verify(mockMediaSearchRepository, times(1)).deleteById(media.getId());
    }

    @Test
    @Transactional
    public void searchMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);
        when(mockMediaSearchRepository.search(queryStringQuery("id:" + media.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(media), PageRequest.of(0, 1), 1));
        // Search the media
        restMediaMockMvc.perform(get("/api/_search/media?query=id:" + media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileLink").value(hasItem(DEFAULT_FILE_LINK)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }
}
