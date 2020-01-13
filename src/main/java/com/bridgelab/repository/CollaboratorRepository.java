package com.bridgelab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelab.model.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator,Integer> {

}
