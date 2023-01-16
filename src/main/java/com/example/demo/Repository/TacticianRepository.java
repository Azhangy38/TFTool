package com.example.demo.Repository;

import com.example.demo.Entity.Tactician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacticianRepository extends JpaRepository<Tactician, Long> {
    List<Tactician> findTacticianById(Long id);
//    List<Tactician> findTacticianByName(String name);
}

