package com.project.Mesa.Repository;

import org.springframework.stereotype.Service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class SequenceService {
	 @PersistenceContext
	    private EntityManager entityManager;

	    @Transactional
	    public void restartSequence(Long nextId) {
	        String sql = "ALTER SEQUENCE users_seq RESTART WITH " + nextId;
	        entityManager.createNativeQuery(sql).executeUpdate();
	    }
}
