package ru.diasoft.micro.demo.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ru.diasoft.micro.demo.repository.AutoModel;

@Mapper(componentModel = "spring", uses = {})
public interface AutoModelMapper2 {

	AutoModelMapper2 INSTANCE = Mappers.getMapper(AutoModelMapper2.class);
	
	AutoModelDto autoModelToAutoModelDto(AutoModel autoModel);
	
	AutoModel autoModelDtoToAutoModel(AutoModelDto autoModelDto);

}
