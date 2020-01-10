package com.bridgelab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelab.model.Note;
@Repository
public interface FundooNodeRepository extends JpaRepository<Note,Integer>
{
  List<Note> findByUserId(int id);
}
