package com.yelman.advertisementserver.repository;

import com.querydsl.core.types.dsl.NumberPath;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.QAdvertisement;
import com.yelman.advertisementserver.model.enums.*;
import org.apache.commons.math3.util.MathArrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>,
        QuerydslPredicateExecutor<Advertisement>,
        QuerydslBinderCustomizer<QAdvertisement> {
    @Override
    default void customize(QuerydslBindings bindings, QAdvertisement advertisement) {
        bindings.bind(advertisement.id).first((NumberPath<Long> path, Long value) -> path.eq(value));
    }
    Page<Advertisement> findByStateAndIsActive(StateEnum state, ActiveEnum isActive,
                                               Pageable pageable);
    List<Advertisement> findBySellerTypeAndIsActive(SellerTypeEnum sellerType, ActiveEnum isActive);
    List<Advertisement> findByUserStoreId(Long userStore_id);
    Page<Advertisement> findByUserStore_Id(long userStore_id,
                                           Pageable pageable);
    void deleteAllByUserStore_Id(long storeId);


}

