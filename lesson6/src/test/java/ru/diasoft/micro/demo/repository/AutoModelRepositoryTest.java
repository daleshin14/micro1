package ru.diasoft.micro.demo.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

@DisplayName("AutoModelRepository test")
@SpringBootTest
class AutoModelRepositoryTest {

    @Autowired
    private AutoModelRepository autoModelRepository;
	
    @Test
    @DisplayName("create")
    void create1() {
    	AutoModel mock = AutoModel.builder()
    			.modelId(1L)
    			.markId(1L)
    			.briefName("briefName")
    			.fullName("fullName")
    			.category("category")
    			.year(2020)
    			.build();
        
        AutoModel link = autoModelRepository.save(mock);

        Assertions.assertNotNull(link);
    }

    @Test
    @DisplayName("create without required params")
    void create2() {
    	AutoModel mock = new AutoModel();
    	mock.setMarkId(2L);
        mock.setBriefName("briefName2");

        AutoModel link = autoModelRepository.save(mock);

        Assertions.assertNotNull(link);
    }

    @Test
    @DisplayName("findById")
    void findById() {
    	AutoModel mock = new AutoModel();
    	mock.setMarkId(2L);
        mock.setBriefName("briefNameUpdate");
        mock.setFullName("fullNameUpdate");
        mock.setCategory("category");
        mock.setYear(2021);
        AutoModel link = autoModelRepository.save(mock);

        AutoModel link2 = autoModelRepository.findById(link.getModelId())
                .orElseThrow(EntityNotFoundException::new);

        Assertions.assertNotNull(link2);
    }
    
    @Test
    @DisplayName("update")
    void update() {
    	AutoModel mock = new AutoModel();
    	mock.setMarkId(2L);
        mock.setBriefName("briefName3");
        mock.setFullName("fullName3");
        mock.setCategory("category");
        mock.setYear(2020);
        AutoModel link = autoModelRepository.save(mock);
        mock.setModelId(link.getModelId());
        
//        mock = autoModelRepository.findById(mock.getModelId())
//                .orElseThrow(EntityNotFoundException::new);

        mock.setBriefName("briefName3Update");
        mock.setFullName("fullName3Update");
        
        autoModelRepository.save(mock);

        AutoModel link2 = autoModelRepository.findById(mock.getModelId())
                .orElseThrow(EntityNotFoundException::new);

        Assertions.assertEquals(mock, link2);
        
    }

    @Test
    @DisplayName("delete")
    void delete() {
    	AutoModel mock = new AutoModel();
    	mock.setMarkId(2L);
        mock.setBriefName("briefNameUpdate");
        mock.setFullName("fullNameUpdate");
        AutoModel link = autoModelRepository.save(mock);

        Long modelId = link.getModelId();
        autoModelRepository.deleteById(modelId);

        Assertions.assertThrows(Exception.class, () -> {
            autoModelRepository.findById(modelId)
                    .orElseThrow(Exception::new);
        });
    }

}