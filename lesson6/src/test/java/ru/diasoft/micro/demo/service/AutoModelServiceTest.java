package ru.diasoft.micro.demo.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;
import ru.diasoft.micro.demo.dto.AutoModelDto;
import ru.diasoft.micro.demo.dto.AutoModelMapper;
import ru.diasoft.micro.demo.repository.AutoModel;
import ru.diasoft.micro.demo.repository.AutoModelRepository;

import static org.mockito.ArgumentMatchers.any;

@DisplayName("AutoModelService test")
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class AutoModelServiceTest {

	@MockBean 
	private AutoModelRepository repository;
	
	@MockBean
	private AutoModelMapper autoModelMapper;
	
	@TestConfiguration
    class AutoModelServiceImplTestContextConfiguration {
 
        @Bean
        public AutoModelService autoModelService() {
            return new AutoModelServiceImpl(repository, autoModelMapper);
        }
        
    }

    @Autowired
    private AutoModelService autoModelService;
    
    private AutoModel getMockAutoModelEntity() {
    	AutoModel obj = AutoModel.builder()
    			.modelId(10L)
    			.markId(20L)
    			.briefName("briefName")
    			.fullName("fullName")
    			.category("category")
    			.year(2020)
    			.build();
        return obj;
    }

    private AutoModel getMockAutoModelEntity2() {
    	AutoModel obj = AutoModel.builder()
    			.modelId(20L)
    			.markId(20L)
    			.briefName("briefName")
    			.fullName("fullNameUpdate")
    			.category("category")
    			.year(2020)
    			.build();
        return obj;
    }
    
    private AutoModelDto getAutoModelDtoEntity() {
    	AutoModelDto dto = new AutoModelDto();
        dto.setModelId(10L);
        dto.setMarkId(20L);
        dto.setBriefName("briefName");
        dto.setFullName("fullName");
        dto.setCategory("category");
        dto.setYear(2020);
        return dto;
    }

    @BeforeEach
    public void setUp() {
        Mockito.when(repository.save(any(AutoModel.class))).thenReturn(getMockAutoModelEntity());
        Mockito.when(repository.saveAndFlush(any(AutoModel.class))).thenReturn(getMockAutoModelEntity());
        Mockito.when(repository.findById(10L)).thenReturn(Optional.of(getMockAutoModelEntity()));
        Mockito.when(repository.findById(20L)).thenReturn(Optional.of(getMockAutoModelEntity2()));
        Mockito.when(autoModelMapper.map(any(AutoModel.class), any())).thenReturn(getAutoModelDtoEntity());
        Mockito.when(autoModelMapper.map(any(AutoModelDto.class), any())).thenReturn(getMockAutoModelEntity());
    }
    
    @Test
    @DisplayName("create")
    void create() {
    	log.info("repository="+repository);
    	log.info("autoModelMapper="+autoModelMapper);
    	AutoModelDto mock = autoModelMapper.map(getMockAutoModelEntity(), AutoModelDto.class);
        Long link = autoModelService.create(mock);

        Assertions.assertNotNull(link);
        Assertions.assertEquals(mock.getModelId(), link);
    }

    @Test
    @DisplayName("update")
    void update() {
    	AutoModelDto mock = new AutoModelDto();
        mock.setBriefName("briefName2");
        mock.setFullName("fullName");
        mock.setCategory("category");
        mock.setYear(2020);
        Long link = autoModelService.create(mock);
        mock.setModelId(link);
        mock.setFullName("fullNameUpdate");
        autoModelService.update(mock);

        AutoModelDto link2 = autoModelService.findById(mock.getModelId());
        log.info("update link2="+link2);
     //   Assertions.assertEquals(mock, link2);//TODO
    }

    @Test
    @DisplayName("findById")
    void findById() {
    	AutoModelDto mock = autoModelMapper.map(getMockAutoModelEntity(), AutoModelDto.class);
        
        Long link = autoModelService.create(mock);
        mock.setModelId(link);

        AutoModelDto link2 = autoModelService.findById(link);

        Assertions.assertNotNull(link2);
        Assertions.assertEquals(mock, link2);
    }

    @Test
    @DisplayName("delete")
    void delete() {
    	AutoModelDto mock = new AutoModelDto();
        mock.setBriefName("briefName2");
        mock.setFullName("fullName");
        mock.setCategory("category");
        mock.setYear(2020);
        Long link = autoModelService.create(mock);

        autoModelService.deleteById(link);

       // Assertions.assertNull(autoModelService.findById(link));//TODO
        
    }

    
    public class AutoModelMatcher implements ArgumentMatcher<AutoModel> {

        private AutoModel left;

        public AutoModelMatcher(AutoModel left) {
        	this.left = left;
        }

        @Override
        public boolean matches(AutoModel right) {
        	log.info("left"+left);
        	log.info("right"+right);
            return left.getBriefName().equals(right.getBriefName());
        }
    }
}