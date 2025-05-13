package com.rishi.book.manager.repository;


import com.rishi.book.manager.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByName(String name);

}
