package com.yelman.shopingserver.repository;

import com.querydsl.core.types.dsl.StringPath;
import com.yelman.shopingserver.model.QStore;
import com.yelman.shopingserver.model.Store;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, QuerydslPredicateExecutor<Store>, QuerydslBinderCustomizer<QStore> {
    @Override
    default void customize(QuerydslBindings bindings, QStore product) {
        bindings.bind(product.name).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
    Page<Store> findById(long id,
                         Pageable pageable);
    Optional<Store> findByStoreName(String storeName);

    List<Store> findByIsActive(ActiveEnum isActive);

    Page<Store> findByCategoryAndIsActive(String category, ActiveEnum activeEnum, Pageable pageable);

}
