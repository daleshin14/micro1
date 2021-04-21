package ru.diasoft.micro.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoModelDto {
	
	protected Long modelId;
	
    protected Long markId;

    protected String briefName;

    protected String fullName;

    protected Integer year;

    protected String category;
    
}


