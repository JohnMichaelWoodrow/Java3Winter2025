package org.example.spring2025demo3rest.dataaccess;

import org.example.spring2025demo3rest.pojos.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoRepository extends JpaRepository<Auto, Integer> {
    List<Auto> getAllByUserId(Integer userId);
}
