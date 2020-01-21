package com.bridgelabz.googlekeep.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bridgelabz.googlekeep.model.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer> {

	List<Collaborator> findAllByNoteid(int note_id);

	List<Collaborator> findAllBySenderid(int id);

}
