package com.bulq.logistics.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulq.logistics.models.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByHub_id(Long id);
}
