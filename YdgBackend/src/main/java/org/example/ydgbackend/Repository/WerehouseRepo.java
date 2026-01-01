package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Werehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WerehouseRepo extends JpaRepository<Werehouse,Long> {
    Werehouse findByWerehouseId(Long werehouseId);
}
