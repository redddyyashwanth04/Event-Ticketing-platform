package com.example.Ticketing_project.repository;

import com.example.Ticketing_project.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Fetch all bookings made by a specific customer (for the "My Bookings" portal)
    List<Booking> findByAttendeeId(Long attendeeId);
}
