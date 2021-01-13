package com.example.Startup.repo;

import com.example.Startup.models.Trip;
import com.example.Startup.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends CrudRepository<Trip,Long> {
    Trip getAllById(Long id);
    List<Trip> getAllByDepartureIsGreaterThanOrderByDepartureAsc(LocalDateTime date);
    List<Trip> getAllByUserAndDepartureLessThan(User user,LocalDateTime date);
    List<Trip> getAllByUserAndDepartureGreaterThan(User user,LocalDateTime date);
    List<Trip> getAllByUserOrderByIdDesc(User user);
    @Query(value = "SELECT * FROM trips WHERE from_city LIKE  '%:fromCity%' AND where_city LIKE '%:whereCity%' AND departure< :dateDeparture order by departure asc",nativeQuery = true)
    List<Trip> searchTrip(@Param("fromCity") String fromCity,@Param("whereCity") String whereCity,@Param("dateDeparture") LocalDateTime date);
    List<Trip> findAllByFromCityLikeAndWhereCityLikeAndDepartureGreaterThanOrderByDepartureAsc(String fromCity,String toCity,LocalDateTime date);
    int countAllByDepartureIsGreaterThan(LocalDateTime date);
    int countAllByUser(User user);
}
