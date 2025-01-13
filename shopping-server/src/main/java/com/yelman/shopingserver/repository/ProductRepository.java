package com.yelman.shopingserver.repository;

import com.yelman.shopingserver.model.Product;
import com.yelman.shopingserver.model.QProduct;
import com.yelman.shopingserver.model.enums.CurrencyEnum;
import com.yelman.shopingserver.model.enums.SellerTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {
    @Override
    default void customize(QuerydslBindings bindings, QProduct root) {

        bindings.bind(root.id).first((path, value) -> path.eq(value));
        bindings.bind(root.title).first((path, value) -> path.containsIgnoreCase(value));  // Büyük/küçük harf duyarsız arama
        bindings.bind(root.description).first((path, value) -> path.containsIgnoreCase(value));
    }

    Page<Product> findBySellerType(SellerTypeEnum sellerType,
                                   Pageable pageable);

    Page<Product> findByShoppingStore_Id(long shoppingStore, Pageable pageable);

    Page<Product> findByCurrency(CurrencyEnum currency, Pageable pageable);

}
