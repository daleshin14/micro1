package ru.diasoft.micro.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.diasoft.micro.demo.dto.AutoModelDto;
import ru.diasoft.micro.demo.dto.AutoModelMapper;
import ru.diasoft.micro.demo.dto.AutoModelMapper2;
import ru.diasoft.micro.demo.repository.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AutoModelServiceImpl implements AutoModelService{

    private final AutoModelRepository autoModelRepository;
    
    private final AutoModelMapper autoModelMapper;
    
    private AutoModelMapper2 autoModelMapper2 = Mappers.getMapper(AutoModelMapper2.class);
    
    @Override
    @Transactional
    public Long create(AutoModelDto dto) {
    	AutoModel res = autoModelRepository.save(autoModelMapper.map(dto, AutoModel.class));
    	log.info("autoModelMapper.map(dto)={}", autoModelMapper.map(dto, AutoModel.class));
    	log.info("autoModelMapper2.autoModelDtoToAutoModel(dto)={}", autoModelMapper2.autoModelDtoToAutoModel(dto));
    	log.info("res={}", res);
        return res.getModelId();
    }

    @Override
    public List<AutoModelDto> findAll() {
    	List<AutoModel> objRes = autoModelRepository.findAll();
    	List<AutoModelDto> res = new ArrayList<>();
		for(AutoModel obj : objRes) {
    		res.add(autoModelMapper.map(obj, AutoModelDto.class));
    	}
        return res;
    }

    @Override
    public Long update(AutoModelDto dto) {
    	AutoModel res = autoModelRepository.saveAndFlush(autoModelMapper.map(dto, AutoModel.class));
        return res.getModelId();
    }
    
    @Override
    public Long updateWithCheck(AutoModelDto dto, Long id) {
		AutoModelDto modelExists = findById(id);
		if(modelExists!=null) {
			dto.setModelId(id);
			return update(dto);
		}else {
			return null;
		}
    }

    @Override 
    public AutoModelDto findById(Long id) {
    	
    	if(id==null) return null;
    	
    	Optional<AutoModel> res = autoModelRepository.findById(id);
    	if(res.isPresent()) {
    		return autoModelMapper.map(res.get(), AutoModelDto.class);
    	}else {
    		return null;
    	}
    }
    
    @Override 
    public AutoModelDto findByBriefName(String briefName){
    	List<AutoModel> res = autoModelRepository.findByBriefName(briefName);
    	if(res!=null && !res.isEmpty()) {
    		return autoModelMapper.map(res.get(0), AutoModelDto.class);
    	}else {
    		return null;
    	}
    }
    
    @Override 
    public List<AutoModelDto> findByMarkId(Long markId){
    	List<AutoModel> objRes = autoModelRepository.findByMarkId(markId);
    	List<AutoModelDto> res = new ArrayList<>();
		for(AutoModel obj : objRes) {
			res.add(autoModelMapper.map(obj, AutoModelDto.class));
    	}
        return res;
    }
    
    @Override 
    public void deleteById(Long modelId) {
    	autoModelRepository.deleteById(modelId);
    }
}
