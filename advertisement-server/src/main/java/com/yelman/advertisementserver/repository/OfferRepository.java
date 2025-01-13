package com.yelman.advertisementserver.repository;

import com.yelman.advertisementserver.model.Offer;
import com.yelman.advertisementserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
