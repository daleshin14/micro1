package ru.diasoft.micro.demo.repository;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="automodel")
public class AutoModel implements Serializable {

	private static final long serialVersionUID = 5667195697021163181L;

	public AutoModel(String briefName){
		this.briefName = briefName;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "automodel_seq")
    @SequenceGenerator(name="automodel_seq", sequenceName = "automodel_seq", initialValue=1, allocationSize=1)
    @Column(name="modelId")
    protected Long modelId;
	
    @Column(name="markId")
    protected Long markId;

    @Column(name="briefName")
    protected String briefName;

    @Column(name="fullName")
    protected String fullName;

    @Column(name="year")
    protected Integer year;

    @Column(name="category")
    protected String category;
    
}


