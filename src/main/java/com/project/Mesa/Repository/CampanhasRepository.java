package com.project.Mesa.Repository;

import java.util.List;

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
        /*Query para central*/	
		@Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Comida Premiada'")
	    List<campanhas> findCampanhasByPaginaComidaPremiada();
	    
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Melhores Empresas'")
	    List<campanhas> findCampanhasByPaginaMelhoresEmpresas();
	
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Come Melhor'")
	    List<campanhas> findCampanhasByPaginaComeMelhor();
	    
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Destaque'")
	    List<campanhas> findCampanhasByPaginaDestaque();
	    
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Destaque Corporativo'")
	    List<campanhas> findCampanhasByPaginaDestaqueCorporativo();
	    
	    /*Query para Filiais */
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Comida Premiada' AND f.cnpj = :cnpj")
	    List<campanhas> findCampanhasByPaginaComidaPremiadaAndCnpj(String cnpj);
	    
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Melhores Empresas' AND f.cnpj = :cnpj")
	    List<campanhas> findCampanhasByPaginaMelhoresEmpresasAndCnpj(String cnpj);
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Come Melhor' AND f.cnpj = :cnpj")
	    List<campanhas> findCampanhasByPaginaComeMelhorAndCnpj(String cnpj);
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Destaque' AND f.cnpj = :cnpj")
	    List<campanhas> findCampanhasByPaginaDestaqueAndCnpj(String cnpj);
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Destaque Corporativo' AND f.cnpj = :cnpj")
	    List<campanhas> findCampanhasByPaginaDestaqueCorporativoAndCnpj(String cnpj);
	    
	    /*Query para Carla, pois ela pode acessar dados de 2 empresas*/
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Comida Premiada' AND f.cnpj IN (:cnpj, '555555555.0001-55')")
	    List<campanhas> findCampanhasByPaginaComidaPremiadaAndCnpjAndEssencia(String cnpj);
	    
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Melhores Empresas' AND f.cnpj IN (:cnpj, '555555555.0001-55')")
	    List<campanhas> findCampanhasByPaginaMelhoresEmpresasAndCnpjAndEssencia(String cnpj);
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Come Melhor' AND f.cnpj IN (:cnpj, '555555555.0001-55')")
	    List<campanhas> findCampanhasByPaginaComeMelhorAndCnpjAndEssencia(String cnpj);
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Destaque' AND f.cnpj IN (:cnpj, '555555555.0001-55')")
	    List<campanhas> findCampanhasByPaginaDestaqueAndCnpjAndEssencia(String cnpj);
	    @Query("SELECT c FROM campanhas c JOIN c.empresa f WHERE c.paginas = 'Destaque Corporativo' AND f.cnpj IN (:cnpj, '555555555.0001-55')")
	    List<campanhas> findCampanhasByPaginaDestaqueCorporativoAndCnpjAndEssencia(String cnpj);
	    
}
