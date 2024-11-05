package com.project.Mesa.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.Mesa.Model.campanhas;
import com.project.Mesa.Model.filial;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CampanhasRepository extends CrudRepository<campanhas, Long>{
	boolean existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresa(String periodo, String grupo,String cargoParticipante,String nomeParticipante, filial empresa);
	
	/*@Query("SELECT SUM(CAST(c.meta AS numeric)) FROM campanhas c WHERE c.grupo = ?1 AND c.meta NOT LIKE '%-%'")
	Double sumMetaByCnpjAndGrupo(String grupo);*/
	
}
