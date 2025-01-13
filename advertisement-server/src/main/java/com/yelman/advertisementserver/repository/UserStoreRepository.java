package com.yelman.advertisementserver.repository;

import com.querydsl.core.types.dsl.StringPath;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.QUserStore;
import com.yelman.advertisementserver.model.UserStore;
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
public interface UserStoreRepository extends JpaRepository<UserStore, Long>,
        QuerydslPredicateExecutor<UserStore>, QuerydslBinderCustomizer<QUserStore> {
    @Override
    default void customize(QuerydslBindings bindings, QUserStore userStore) {
        bindings.bind(userStore.name).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
    Optional<UserStore> findById(long id);
    Optional<UserStore> findByStoreName(String storeName);

    Optional<List<UserStore>> findByIsActive(ActiveEnum isActive);
    Page<UserStore> findByCategory(String categoryName,
                                   Pageable pageable);
}
