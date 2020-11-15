package com.manager.fuel.repositories;

import com.manager.fuel.entities.FuelHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuelHistoryRepository  extends CrudRepository<FuelHistory, Long> {

    List<FuelHistory> findByRegion(@Param("region") String region);

    List<FuelHistory> findByCity(@Param("city") String city);

    List<FuelHistory> findByBrand(@Param("brand") String brand);

    Iterable<FuelHistory> findAll(Sort sort);
}
