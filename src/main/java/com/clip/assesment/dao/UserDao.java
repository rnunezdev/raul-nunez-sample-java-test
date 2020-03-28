package com.clip.assesment.dao;

import com.clip.assesment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

}
