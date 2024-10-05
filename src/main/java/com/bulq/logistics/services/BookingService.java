package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Booking;
import com.bulq.logistics.repositories.BookingRepository;
import com.bulq.logistics.util.constants.PickupType;
import com.bulq.logistics.util.constants.ShipmentType;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> findByAccount_id(Long id) {
        return bookingRepository.findByAccount_id(id);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> findByDeliveryId(String deliveryId) {
        return bookingRepository.findByDeliveryId(deliveryId);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> findPendingBookingsByAccountId(Long userId) {
        return bookingRepository.findPendingBookingsByAccountId(userId);
    }

    public Page<Booking> findByBookingInfo(int page, int pageSize, String sortBy, String sender, String receiver,
            String delivery_code, ShipmentType shipment_type, PickupType pickupType, String phoneNumber, String email) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return bookingRepository.findByBookingInfo(sender, receiver, delivery_code, shipment_type, pickupType,
                phoneNumber, email, pageable);
    }
}
