package com.yelman.advertisementserver.services.dinamic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.mapper.AdvertisementMapperImpl;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.QAdvertisement;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.enums.AdvertisementOrdersEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.yelman.advertisementserver.model.QAdvertisement.advertisement;

@Service
public class QueryServices {

    private final AdvertisementMapperImpl advertisementMapper;
    @PersistenceContext
    private final EntityManager entityManager;


    public QueryServices(AdvertisementMapperImpl advertisementMapper, EntityManager entityManager) {

        this.advertisementMapper = advertisementMapper;
        this.entityManager = entityManager;
    }

    @Cacheable(value = "category_advertisements",
            key = "#categoryId + '_' + #order + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    @Transactional
    public Page<AdvertisementDto> getAdvertisements(long categoryId, AdvertisementOrdersEnum order, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAdvertisement advertisement = QAdvertisement.advertisement;
        OrderSpecifier<?> selectedOrder = (order == null || order == AdvertisementOrdersEnum.desc)
                ? advertisement.price.desc()
                : advertisement.price.asc();
        long total = queryFactory.selectFrom(advertisement)
                .where(advertisement.category.id.eq(categoryId)
                        .and(advertisement.isActive.eq(ActiveEnum.ACTIVE)))
                .fetchCount();
        List<Advertisement> ad = queryFactory.selectFrom(advertisement)
                .where(advertisement.category.id.eq(categoryId)
                        .and(advertisement.isActive.eq(ActiveEnum.ACTIVE)))
                .orderBy(selectedOrder)

                .fetch();
        List<AdvertisementDto> dtoList = ad.stream()
                .map(advertisementMapper::toDto)
                .toList();

        Page<AdvertisementDto> resultPage = new PageImpl<>(dtoList, pageable, total);

        return resultPage;
    }

    @Cacheable(value = "price_range_advertisements",
            key = "#categoryId + '_' + #minPrice + '_' + #maxPrice + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    @Transactional
    public Page<AdvertisementDto> getCategoryIdMinMaxPriceBetween(
            long category, SellerTypeEnum sellerType, int rate,
            StateEnum state, BigDecimal minPrice,
            BigDecimal maxPrice, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAdvertisement product = advertisement;
        long total = queryFactory.selectFrom(advertisement)
                .where(advertisement.category.id.eq(category)
                        .and(advertisement.isActive.eq(ActiveEnum.ACTIVE)))
                .fetchCount();
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (state != null) {
            booleanBuilder.and(advertisement.state.eq(state));
        }
        if (sellerType != null) {
            booleanBuilder.and(advertisement.sellerType.eq(sellerType));
        }
        if (rate > 0) {
            booleanBuilder.and(advertisement.rate.eq(rate));
        }
        if (category > 0) {
            booleanBuilder.and(product.category.id.eq(category));
        }
        if (minPrice != null) {
            booleanBuilder.and(product.price.goe(minPrice)); // price >= minPrice
        }
        if (maxPrice != null) {
            booleanBuilder.and(product.price.loe(maxPrice)); // price <= maxPrice
        }
        List<Advertisement> ad = queryFactory.selectFrom(product)
                .where(booleanBuilder
                        .and(advertisement.isActive.eq(ActiveEnum.ACTIVE)))
                .fetch();
        List<AdvertisementDto> dtoList = ad.stream()
                .map(advertisementMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, total);


    }


}

