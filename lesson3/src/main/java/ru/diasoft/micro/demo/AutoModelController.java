package ru.diasoft.micro.demo;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("automodel")
public class AutoModelController {

	private List<Map<String,Object>> models = new ArrayList<Map<String,Object>>(){{
		add(new HashMap<String,Object>(){{ put("id", "1"); put("name", "Mazda 5"); }});
		add(new HashMap<String,Object>(){{ put("id", "2"); put("name", "Hodna civic"); }});
		add(new HashMap<String,Object>(){{ put("id", "3"); put("name", "Renault logan"); }});
	}};

	private Integer count = 4;
	
	@GetMapping
	public List<Map<String,Object>> defaultMethod() {
		return models;
	}

	@GetMapping("{id}")
	// http://localhost:8080/automodel/3
	public Map<String,Object> getById(@PathVariable String id) {
		return getModel(id);
	}

	private Map<String, Object> getModel(String id) {
		return models.stream()
			.filter(model -> model.get("id").equals(id))
			.findFirst()
			.orElseThrow(NotFoundException::new);
	}
	
	@PostMapping
	// fetch('/automodel', { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ name: 'Lada niva' }) } ).then(console.log)
	public Map<String,Object> create(@RequestBody Map<String,Object> model) {
		
		model.put("id", String.valueOf(count++));
		models.add(model);
		
		return model;
	}
	
	@PutMapping("{id}")
	// fetch('/automodel/4', { method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ name: 'Lada niva tour', id: 11 }) } ).then(console.log)
	public Map<String,Object> modify(@PathVariable String id, @RequestBody Map<String,Object> model) {

		Map<String,Object> modelExists = getModel(id);
		modelExists.putAll(model);
		modelExists.put("id", id);
		
		return modelExists;
	}
	
	@DeleteMapping("{id}")
	// fetch('/automodel/3', { method: 'DELETE' } ).then(console.log)
	public void delete(@PathVariable String id) {
		
		Map<String,Object> modelExists = getModel(id);
		models.remove(modelExists);
		
	}
	
	
}
