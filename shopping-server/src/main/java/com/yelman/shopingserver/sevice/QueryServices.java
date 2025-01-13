package com.yelman.shopingserver.sevice;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yelman.shopingserver.api.dto.ProductDto;
import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.api.mapper.ProductMapper;
import com.yelman.shopingserver.api.mapper.UserStoreMapper;
import com.yelman.shopingserver.model.Product;
import com.yelman.shopingserver.model.QProduct;
import com.yelman.shopingserver.model.QStore;
import com.yelman.shopingserver.model.Store;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import com.yelman.shopingserver.model.enums.OrdersEnum;
import com.yelman.shopingserver.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class QueryServices {
    private final UserStoreMapper userStoreMapper;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public QueryServices(UserStoreMapper userStoreMapper, ProductMapper productMapper, ProductRepository productRepository) {

        this.userStoreMapper = userStoreMapper;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    // ülke sehir ilçe  için dinamik sorgu
    @Transactional
    public ResponseEntity<Page<UserStoreDto>> getCountOrCityOrDistrict(String country, String city, String district, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QStore store = QStore.store;
        BooleanBuilder whereClause = new BooleanBuilder();
        if (city != null) {
            whereClause.or(store.city.eq(city));
        }
        if (district != null) {
            whereClause.or(store.district.eq(district));
        }
        if (country != null) {
            whereClause.or(store.country.eq(country));
        }
        whereClause.and(store.isActive.eq(ActiveEnum.ACTIVE));
        List<Store> data = queryFactory.selectFrom(store).where(
                        whereClause)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        Long total = queryFactory.selectFrom(store).where(whereClause).fetchCount();
        List<UserStoreDto> dto = data.stream().map(userStoreMapper::entityToDto).toList();
        if (total == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(new PageImpl<>(dto, pageable, total));
    }

    /**
     * @author cihan
     * @apiNote ürünlerin filtrelerini burda oluşturacagım
     */

    //kategorisi verilem  ilanları en yüksekten en asagıya dogru fiyat sıralaması yapar
    @Transactional
    public ResponseEntity<Page<ProductDto>> getProductCategoryPriceOrder(long categoryId, OrdersEnum order, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct advertisement = QProduct.product;
        OrderSpecifier<?> selectedOrder = (order == null || order == OrdersEnum.desc)
                ? advertisement.price.desc()
                : advertisement.price.asc();
        long total = queryFactory.selectFrom(advertisement)
                .where(advertisement.shoppingCategory.id.eq(categoryId))
                .fetchCount();
        List<Product> ad = queryFactory.selectFrom(advertisement)
                .where(advertisement.shoppingCategory.id.eq(categoryId))
                .orderBy(selectedOrder)

                .fetch();
        List<ProductDto> dtoList = ad.stream()
                .map(productMapper::entityToDto)
                .toList();

        Page<ProductDto> resultPage = new PageImpl<>(dtoList, pageable, total);

        return ResponseEntity.ok(resultPage);
    }

    //istenilen fiyar aralıgına göre sırala
    @Transactional
    public ResponseEntity<Page<ProductDto>> getCategoryIdMinMaxPriceBetween(Long category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (category != null) {
            booleanBuilder.and(product.shoppingCategory.id.eq(category));
        }
        if (minPrice != null) {
            booleanBuilder.and(product.price.goe(minPrice)); // price >= minPrice
        }
        if (maxPrice != null) {
            booleanBuilder.and(product.price.loe(maxPrice)); // price <= maxPrice
        }
        long total = queryFactory.selectFrom(product)
                .where(booleanBuilder)
                .fetchCount();
        List<Product> ad = queryFactory.selectFrom(product)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<ProductDto> dtoList = ad.stream()
                .map(productMapper::entityToDto)
                .toList();

        Page<ProductDto> resultPage = new PageImpl<>(dtoList, pageable, total);

        return ResponseEntity.ok(resultPage);
    }

    // category ,yıldız, en düşük fiyattan,en yüksek fiyattan
    @Transactional
    public ResponseEntity<Page<ProductDto>> getCategoryAndBradAndRateAndOrder(
            Long categoryId, String brand, Integer rate,
            OrdersEnum ordersEnum, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;

        OrderSpecifier<?> selectedOrder = (ordersEnum == null || ordersEnum == OrdersEnum.desc)
                ? product.price.desc()
                : product.price.asc();
        BooleanBuilder whereClause = new BooleanBuilder();
        if (categoryId != null && !categoryId.describeConstable().isEmpty()) {
            whereClause.and(product.shoppingCategory.id.eq(categoryId));
        }
        if (brand != null && !brand.isEmpty()) {
            whereClause.and(product.brand.eq(brand));
        }
        if (rate != null && !rate.describeConstable().isEmpty()) {
            whereClause.and(product.rate.eq(rate));
        }
        List<Product> products = queryFactory.selectFrom(product)
                .where(whereClause)
                .orderBy(selectedOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(product.count()).from(product).where(whereClause).fetchOne();
        List<ProductDto> dto = products.stream().map(productMapper::entityToDto).toList();
        if (total == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(new PageImpl<>(dto, pageable, total));
    }

    @Transactional
    public List<ProductDto> searchProducts(String keyword) {
        QProduct qProduct = QProduct.product;


        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(qProduct.title.containsIgnoreCase(keyword));
            builder.or(qProduct.description.containsIgnoreCase(keyword));
        }
        List<Product> products = (List<Product>) productRepository.findAll(builder);

        return products.stream()
                .map(productMapper::entityToDto)
                .toList();
    }

}
