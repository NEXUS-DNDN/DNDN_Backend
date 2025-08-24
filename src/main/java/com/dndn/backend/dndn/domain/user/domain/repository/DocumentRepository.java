package com.dndn.backend.dndn.domain.user.domain.repository;

import com.dndn.backend.dndn.domain.user.domain.entity.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentFile, Long> {

    @Query("SELECT d FROM DocumentFile d JOIN FETCH d.user u WHERE u.id = :userId")
    List<DocumentFile> findAllByUserId(@Param("userId") Long userId);

    Optional<DocumentFile> findByIdAndUserId(Long documentId, Long userId);

}
