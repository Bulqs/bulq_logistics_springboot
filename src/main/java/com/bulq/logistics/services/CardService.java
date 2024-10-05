package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Card;
import com.bulq.logistics.repositories.CardRepository;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card save(Card category){
        return cardRepository.save(category);
    }

    public List<Card> findByWallet_id(Long id) {
        return cardRepository.findByWallet_id(id);
    }

    public List<Card> findAll(){
        return cardRepository.findAll();
    }

    public Optional<Card> findById(Long id){
        return cardRepository.findById(id);
    }

    public void deleteById (Long id){
        cardRepository.deleteById(id);
    }
}
