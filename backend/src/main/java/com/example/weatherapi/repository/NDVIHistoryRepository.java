package com.example.weatherapi.repository;

import com.example.weatherapi.model.NDVIHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NDVIHistoryRepository extends JpaRepository<NDVIHistory, Long> {
    List<NDVIHistory> findByFieldIdOrderByDateAsc(Long fieldId);
}
