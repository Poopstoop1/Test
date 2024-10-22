package com.project.Mesa.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.project.Mesa.Model.Users;

import jakarta.transaction.Transactional;



@Repository
@Transactional
public interface UserRepository extends CrudRepository<Users, Long>{

	@Query("select u from Users u where u.login = ?1 ")
	public Users findByUsername(String login);
	
	@Modifying
    @Query("UPDATE Users u SET u.id = u.id - 1 WHERE u.id > ?1")
    public void adjustIDs(Long deletedId);
	
	@Query("SELECT MAX(u.id) FROM Users u")
	public Long getMaxId();
	
	@Query("SELECT u.id FROM Users u")
	List<Long> findAllIds();
	
	@Query("SELECT MIN(u.id) FROM Users u WHERE u.id > 0")
	public Long findMinAvailableId();
	
	@Modifying
	@Transactional
	@Query("UPDATE Users u SET u.id = :newId WHERE u.id = :oldId")
	void updateUserId(Long oldId, Long newId);
	

}
