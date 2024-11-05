package com.project.Mesa.Repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.project.Mesa.Model.filial;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface FilialRepository extends CrudRepository<filial, String>{

	
	Optional<filial> findByNome(String nome);
}
