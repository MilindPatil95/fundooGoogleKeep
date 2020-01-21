package com.bridgelabz.googlekeep.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bridgelabz.googlekeep.model.UserLabel;
@Repository
public interface LabelRepository extends JpaRepository<UserLabel,Integer>
{
	List<UserLabel> findAllByUserId(int id);
}
