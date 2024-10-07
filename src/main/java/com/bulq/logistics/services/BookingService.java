package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Booking;
import com.bulq.logistics.payload.booking.BookingSummaryAmountDTO;
import com.bulq.logistics.payload.booking.BookingSummaryDTO;
import com.bulq.logistics.repositories.BookingRepository;
import com.bulq.logistics.util.constants.PickupType;
import com.bulq.logistics.util.constants.ShipmentType;
import com.bulq.logistics.util.constants.Status;

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
    
    public List<BookingSummaryDTO> getBookingSummary(List<Status> statuses, Integer day, String month, Integer year){
        List<Object[]> results = bookingRepository.findBookingSummaryByDayMonthYearAndStatuses(statuses, day, month, year);
        return results.stream().map(result -> new BookingSummaryDTO(
            (String) result[0],   // month
                (Integer) result[1],  // year
                (Integer) result[2],  // day
                (Status) result[3],  // status
                (Long) result[4]   // total number of items
        )).collect(Collectors.toList());
    }

    public List<BookingSummaryAmountDTO> getBookingSummaryWithAmount(List<Status> statuses, Integer day, String month, Integer year) {
        List<Object[]> results = bookingRepository.findBookingSummary(month, year, day, statuses);
        
        // Mapping to BookingSummaryAmountDTO
        return results.stream().map(result -> new BookingSummaryAmountDTO(
            (String) result[0],   // month
            (Integer) result[1],  // year
            (Integer) result[2],  // day
            (Status) result[3],   // status
            (Long) result[4],     // total number of items
            (Double) result[5]    // total shipping amount
        )).collect(Collectors.toList());
    }
    



    public Page<Booking> findByBookingInfo(int page, int pageSize, String sortBy, String sender, String receiver,
            String delivery_code, ShipmentType shipment_type, PickupType pickupType, String phoneNumber, String email) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return bookingRepository.findByBookingInfo(sender, receiver, delivery_code, shipment_type, pickupType,
                phoneNumber, email, pageable);
    }
}
