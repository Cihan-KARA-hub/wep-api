package com.yelman.shopingserver.sevice;

import com.yelman.shopingserver.api.dto.ProductDto;
import com.yelman.shopingserver.api.mapper.ProductMapperImpl;
import com.yelman.shopingserver.model.Product;
import com.yelman.shopingserver.model.Store;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import com.yelman.shopingserver.model.enums.CurrencyEnum;
import com.yelman.shopingserver.model.enums.SellerTypeEnum;
import com.yelman.shopingserver.repository.CategoryRepository;
import com.yelman.shopingserver.repository.ProductRepository;
import com.yelman.shopingserver.repository.StoreRepository;
import com.yelman.shopingserver.utils.SeoSlug;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductServices {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ProductMapperImpl productMapper;
    private final CategoryRepository categoryRepository;


    public ProductServices(ProductRepository productRepository, StoreRepository storeRepository, ProductMapperImpl productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @CacheEvict(value = {"getStoreList","searchProducts","filter","priceRange","currency","storeProducts","sellerTypeProducts","storeLocation"}, allEntries = true)
    public ResponseEntity<String> addAdvertisement(ProductDto ad) {
        Optional<Store> user = storeRepository.findById(ad.getShoppingStoreId());
        if (user.isPresent() && user.get().getIsActive() == ActiveEnum.ACTIVE) {
            Product entity = productMapper.dtoToEntity(ad);
            entity.setSeoSlug(SeoSlug.generateSeoSlug(ad.getTitle()));
            entity.setShoppingStore(storeRepository.findById(ad.getShoppingStoreId()).orElse(null));
            entity.setShoppingCategory(categoryRepository.findById(ad.getShoppingCategoryId()).orElse(null));
            entity.setCurrency(ad.getCurrency());
            entity.setRateCount(ad.getRateCount());

            productRepository.save(entity);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().build();
    }
    @Cacheable(value = "sellerTypeProducts", key = "#sellerType + '-' + #page + '-' + #size")
    @Transactional
    public Page<ProductDto> findBySellerTypeAndIsActive(
            SellerTypeEnum sellerType, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> ad = productRepository.findBySellerType(sellerType, pageable);
        if (ad.isEmpty()) return null;
        Page<ProductDto> dtoList = ad
                .map(productMapper::entityToDto);
        return dtoList;

    }

    //stoktan düşmesi için method
    @CacheEvict(value = {"getStoreList","searchProducts","filter","priceRange","currency","storeProducts","sellerTypeProducts","storeLocation"}, allEntries = true)

    @Transactional
    public ResponseEntity<String> getUserStockDeIcrement(long advertisementId, int size) {
        Product ad = productRepository.findById(advertisementId).orElse(null);
        if (ad == null && size < 0) return ResponseEntity.notFound().build();
        ad.setStock(ad.getStock() - size);
        if (ad.getStock() <= 0) {
            ad.setStock(0);
            return ResponseEntity.ok("stock tükendi ");
        }
        productRepository.save(ad);
        return ResponseEntity.ok("kalan Stok :" + ad.getStock() + " name : " + ad.getTitle());
    }
    @CacheEvict(value = {"getStoreList","searchProducts","filter","priceRange","currency","storeProducts","sellerTypeProducts","storeLocation"}, allEntries = true)
    @Transactional
    public ResponseEntity<String> getUserStockIcrement(long advertisementId, int size) {
        Product ad = productRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        ad.setStock(ad.getStock() + size);
        if (ad.getStock() <= 0) return ResponseEntity.notFound().build();
        productRepository.save(ad);
        return ResponseEntity.ok("kalan Stok :" + ad.getStock() + " name : " + ad.getTitle());
    }
    //yıldızlama işleminde  kullanıcıların yıldız vermesi
    @CacheEvict(value = {"getStoreList","searchProducts","filter","priceRange","currency","storeProducts","sellerTypeProducts","storeLocation"}, allEntries = true)
    @Transactional
    public ResponseEntity<Void> rateProduct(long advertisementId, int rate) {
        Product ad = productRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        if (rate > 5) {
            rate = rate % 5;
        }
        int newAverage = updateAverage(ad.getRate(), ad.getRateCount(), rate);
        System.out.println("Yeni Ortalama: " + newAverage);
        ad.setRate(newAverage);
        ad.setRateCount(ad.getRateCount() + 1);
        productRepository.save(ad);
        return ResponseEntity.ok().build();

    }
    @Cacheable(value = "getStoreList", key = "#storeId + '-' + #page + '-' + #size")
    @Transactional
    public ResponseEntity<Page<ProductDto>> getStoreList(int size, int page, long storeId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> store = productRepository.findByShoppingStore_Id(storeId, pageable);
        if (store.isEmpty()) return ResponseEntity.notFound().build();
        Page<ProductDto> dto = store.map(productMapper::entityToDto);
        return ResponseEntity.ok(dto);

    }
    // para birimine göre listeleme
    @Cacheable(value = "currency", key = "#currencyEnum + '-' + #page + '-' + #size")
    @Transactional
    public ResponseEntity<Page<ProductDto>> getCurrency(CurrencyEnum currency, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByCurrency(currency, pageable);
        if (products.isEmpty()) return ResponseEntity.notFound().build();
        Page<ProductDto> dto = products.map(productMapper::entityToDto);
        return ResponseEntity.ok(dto);

    }


    private int updateAverage(int oldAverage, int voteCount, int newVote) {
        return (oldAverage * voteCount + newVote) / (voteCount + 1);
    }

    @CacheEvict(value = {"getStoreList","searchProducts","filter","priceRange","currency","storeProducts","sellerTypeProducts","storeLocation"}, allEntries = true)

    public ResponseEntity<String> deleteProduct(long id, long userId) {
        Product ad = productRepository.findById(id).orElse(null);
        if (ad != null && ad.getShoppingStore().getAuthor().getId() == userId) {
            productRepository.delete(ad);
            return ResponseEntity.ok("success delete product" + ad.getId() + "\n Product name : " + ad.getTitle());
        }
        return ResponseEntity.notFound().build();
    }


}
