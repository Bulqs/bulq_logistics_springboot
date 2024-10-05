package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Hub;
import com.bulq.logistics.repositories.HubsRepository;

@Service
public class HubService {

    @Autowired
    private HubsRepository hubsRepository;

    public Hub save(Hub hub){
        return hubsRepository.save(hub);
    }

    public List<Hub> findAll(){
        return hubsRepository.findAll();
    }

    public Optional<Hub> findById(Long id){
        return hubsRepository.findById(id);
    }

    public void deleteById (Long id){
        hubsRepository.deleteById(id);
    }

    public Page<Hub> findByHubInfo(int page, int pageSize, String sortBy, Long id, String state, String city, String country) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return hubsRepository.findByHubInfo(id, state, city, country, pageable);
    }
}
