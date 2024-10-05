package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Complaint;
import com.bulq.logistics.repositories.ComplaintRepository;
import com.bulq.logistics.util.constants.ComplaintType;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    public Complaint save(Complaint complaint){
        return complaintRepository.save(complaint);
    }

    public List<Complaint> findAll(){
        return complaintRepository.findAll();
    }

    public Optional<Complaint> findById(Long id){
        return complaintRepository.findById(id);
    }

    public void deleteById (Long id){
        complaintRepository.deleteById(id);
    }

    public Page<Complaint> findByComplaintInfo(int page, int pageSize, String sortBy, Long id,String complaint, String complainant, String delivery_code, ComplaintType complaint_status ) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return complaintRepository.findByComplaintInfo(id, complaint, complainant, delivery_code, complaint_status,  pageable);
    }
}
