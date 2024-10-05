package com.bulq.logistics.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bulq.logistics.models.Complaint;
import com.bulq.logistics.util.constants.ComplaintType;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    @Query("SELECT c FROM Complaint c WHERE " +
    "(:id IS NULL OR c.id = :id) " +  // Direct comparison for id (Long)
    "AND (:complaint IS NULL OR c.complaint LIKE CONCAT('%', :complaint, '%')) " +  // Filter by complaint
    "AND (:complainant IS NULL OR c.complainant LIKE CONCAT('%', :complainant, '%')) " +  // Filter by complainant
    "AND (:delivery_code IS NULL OR c.delivery_code LIKE CONCAT('%', :delivery_code, '%')) " +  // Filter by delivery_code
    "AND (:complaint_status IS NULL OR c.complaint_status = :complaint_status)")  // Filter by complaint_status (enum)
Page<Complaint> findByComplaintInfo(
    @Param("id") Long id,
    @Param("complaint") String complaint,
    @Param("complainant") String complainant,
    @Param("delivery_code") String delivery_code,
    @Param("complaint_status") ComplaintType complaint_status,
    Pageable pageable
);

}

/**
 * 
 * 
 * private long id;

    private String complaint;

    private String complainant;

    private String delivery_code;
 */
