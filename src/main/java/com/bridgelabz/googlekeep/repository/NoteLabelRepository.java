package com.bridgelabz.googlekeep.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bridgelabz.googlekeep.model.NoteLabel;
public interface NoteLabelRepository extends JpaRepository<NoteLabel, Integer>{

	List<NoteLabel> findAllByNoteid(int noteid);

	List<NoteLabel> findByLabelid(int label_id);
     
}
