package com.bridgelab.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelab.model.User;
import com.bridgelab.model.UserLabel;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
  User findByEmail(String email);
  
  
 
}