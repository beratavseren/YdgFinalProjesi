package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.WerehouseWorker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WerehouseWorkerRepo extends JpaRepository<WerehouseWorker,Long> {
    WerehouseWorker findWerehouseWorkerByWerehouseWorkerId(Long werehouseWorkerId);
    WerehouseWorker findByEmail(String email);
}
