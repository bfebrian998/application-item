package com.application.storage.storage.Repository;

import com.application.storage.storage.Entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    StorageEntity findByNomorSeriBarang(String nama);
}