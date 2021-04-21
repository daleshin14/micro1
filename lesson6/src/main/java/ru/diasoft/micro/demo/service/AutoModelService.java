package ru.diasoft.micro.demo.service;

import java.util.List;

import ru.diasoft.micro.demo.dto.AutoModelDto;

public interface AutoModelService {

	Long create(AutoModelDto dto);

    List<AutoModelDto> findAll();
    
    Long update(AutoModelDto dto);
    
    Long updateWithCheck(AutoModelDto dto, Long id);
    
    AutoModelDto findById(Long id);
    
    AutoModelDto findByBriefName(String briefName);
    
    List<AutoModelDto> findByMarkId(Long markId);

    void deleteById(Long modelId);
    
}
