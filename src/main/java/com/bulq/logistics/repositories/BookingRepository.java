package com.bulq.logistics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bulq.logistics.models.Booking;
import com.bulq.logistics.util.constants.PickupType;
import com.bulq.logistics.util.constants.ShipmentType;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByAccount_id(Long id);
    Optional<Booking> findByDeliveryId(String deliveryId);

    @Query("SELECT b FROM Booking b WHERE b.account.id = :accountId AND b.delivery_status = 'PENDING'")
    List<Booking> findPendingBookingsByAccountId(@Param("accountId") Long accountId);


    @Query("SELECT a FROM Booking a WHERE " +
    "(:sender IS NULL OR a.sender_firstname LIKE CONCAT('%', :sender, '%') OR a.sender_lastname LIKE CONCAT('%', :sender, '%')) " +
    "OR (:receiver IS NULL OR a.receiver_firstname LIKE CONCAT('%', :receiver, '%') OR a.receiver_lastname LIKE CONCAT('%', :receiver, '%')) " +
    "OR (:deliveryId IS NULL OR a.deliveryId LIKE CONCAT('%', :deliveryId, '%')) " +
    "OR (:phoneNumber IS NULL OR a.sender_phoneNumber LIKE CONCAT('%', :phoneNumber, '%') OR a.receiver_phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) " +
    "AND (:email IS NULL OR a.sender_email LIKE CONCAT('%', :email, '%') OR a.receiver_email LIKE CONCAT('%', :email, '%')) " +
    "AND a.shipment_type = :shipment_type " +  // No null check for shipment_type
    "AND a.pickupType = :pickupType")  // No null check for pickupType
Page<Booking> findByBookingInfo(
    @Param("sender") String sender,
    @Param("receiver") String receiver,
    @Param("deliveryId") String deliveryId,
    @Param("shipment_type") ShipmentType shipment_type,
    @Param("pickupType") PickupType pickupType,
    @Param("phoneNumber") String phoneNumber,
    @Param("email") String email,
    Pageable pageable
);


// @Query("SELECT a FROM Booking a WHERE " +
//     "(" +
//     "(:sender IS NULL OR a.sender_firstname LIKE CONCAT('%', :sender, '%') OR a.sender_lastname LIKE CONCAT('%', :sender, '%')) " +
//     "OR (:receiver IS NULL OR a.receiver_firstname LIKE CONCAT('%', :receiver, '%') OR a.receiver_lastname LIKE CONCAT('%', :receiver, '%')) " +
//     "OR (:delivery_code IS NULL OR a.delivery_id LIKE CONCAT('%', :delivery_code, '%')) " +
//     "OR (:phoneNumber IS NULL OR a.sender_phoneNumber LIKE CONCAT('%', :phoneNumber, '%') OR a.receiver_phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) " +
//     "OR (:email IS NULL OR a.sender_email LIKE CONCAT('%', :email, '%') OR a.receiver_email LIKE CONCAT('%', :email, '%')) " +
//     ")" +
//     "AND (:type IS NULL OR a.shipment_type LIKE CONCAT('%', :type, '%') OR a.pickupType LIKE CONCAT('%', :type, '%'))")
//     Page<Booking> findByBookingInfo(
//     @Param("sender") String sender,
//     @Param("receiver") String receiver,
//     @Param("delivery_code") String delivery_code,
//     @Param("shipment_type") ShipmentType shipment_type,
//     @Param("pickupType") PickupType pickupType,
//     @Param("phoneNumber") String phoneNumber,
//     @Param("email") String email,
//     Pageable pageable
// );



}
