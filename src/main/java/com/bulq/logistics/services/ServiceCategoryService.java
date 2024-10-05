package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.ServiceCategory;
import com.bulq.logistics.repositories.ServiceCategoryRepository;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategory save(ServiceCategory category){
        return serviceCategoryRepository.save(category);
    }

    public List<ServiceCategory> findAll(){
        return serviceCategoryRepository.findAll();
    }

    public Optional<ServiceCategory> findById(Long id){
        return serviceCategoryRepository.findById(id);
    }

    public void deleteById (Long id){
        serviceCategoryRepository.deleteById(id);
    }
}
