package ru.diasoft.micro.demo.rest;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.diasoft.micro.demo.dto.AutoModelDto;
import ru.diasoft.micro.demo.service.AutoModelService;


@RestController
@RequestMapping("automodel")
@RequiredArgsConstructor
@Slf4j
public class AutoModelController {

    private final AutoModelService autoModelService;
	
	@GetMapping
	public List<AutoModelDto> defaultMethod() {
		return autoModelService.findAll();
	}

	@GetMapping("{id}")
	// http://localhost:8080/automodel/3
	public AutoModelDto getById(@PathVariable Long id) {
		
		return autoModelService.findById(id);
		
	}
	
	@PostMapping
	// fetch('/automodel', { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ markId: '1', briefName: 'brief', fullName: 'full',  year: 2021, category: 'sedan' }) } ).then(console.log)
	public Long create(@RequestBody AutoModelDto model) {

		return autoModelService.create(model);
		
	}
	
	@PutMapping("{id}")
	// fetch('/automodel/1', { method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ markId: '1', briefName: 'brief', fullName: 'fullname',  year: 2022, category: 'hatch' }) } ).then(console.log)
	public Long modify(@PathVariable Long id, @RequestBody AutoModelDto model) {
		
		log.debug("modify started. id=", id, "params=", model);
		return autoModelService.updateWithCheck(model, id);

	}
	
	@DeleteMapping("{id}")
	// fetch('/automodel/1', { method: 'DELETE' } ).then(console.log)
	public void delete(@PathVariable Long id) {
		
		autoModelService.deleteById(id);
		
	}
	
	
}
