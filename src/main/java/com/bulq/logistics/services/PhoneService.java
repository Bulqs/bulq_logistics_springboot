package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Phone;
import com.bulq.logistics.repositories.PhoneRepository;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    public Phone save(Phone phone){
        return phoneRepository.save(phone);
    }

    public List<Phone> findAll(){
        return phoneRepository.findAll();
    }

    public Optional<Phone> findById(Long id){
        return phoneRepository.findById(id);
    }

    public List<Phone> findByHub_id(Long id) {
        return phoneRepository.findByHub_id(id);
    }

    public void deleteById (Long id){
        phoneRepository.deleteById(id);
    }
}
