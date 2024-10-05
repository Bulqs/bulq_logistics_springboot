package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.WorkingHour;
import com.bulq.logistics.repositories.WorkingHourRepository;

@Service
public class WorkHourService {

    @Autowired
    private WorkingHourRepository workingHourRepository;

    public WorkingHour save(WorkingHour workingHour){
        return workingHourRepository.save(workingHour);
    }

    public List<WorkingHour> findAll(){
        return workingHourRepository.findAll();
    }

    public Optional<WorkingHour> findById(Long id){
        return workingHourRepository.findById(id);
    }

    public List<WorkingHour> findByHub_id(Long id) {
        return workingHourRepository.findByHub_id(id);
    }

    public void deleteById (Long id){
        workingHourRepository.deleteById(id);
    }
}
