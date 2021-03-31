package ru.diasoft.micro.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoModelRepository extends JpaRepository<AutoModel, Long> {

	List<AutoModel> findByBriefName(String briefName);
	
	List<AutoModel> findByMarkId(Long markId);
	
    @Query("select t from AutoModel as t where t.modelId = :id or t.briefName = :briefName or t.fullName = :fullname")
    Optional<AutoModel> findByParams(Long id, String briefName, String fullname);

}
