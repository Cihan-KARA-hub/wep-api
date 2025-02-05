package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.dto.AdvertisementDtoSecond;
import com.yelman.advertisementserver.api.dto.AdvertisementUpdateDto;
import com.yelman.advertisementserver.api.mapper.AdvertisementMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Category;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.CategoryRepository;
import com.yelman.advertisementserver.repository.UserRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final UserStoreRepository userStoreRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository, AdvertisementMapper advertisementMapper, UserStoreRepository userStoreRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementMapper = advertisementMapper;
        this.userStoreRepository = userStoreRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private static String generateSeoSlug(String text) {
        // Küçük harfe dönüştür
        String slug = text.toLowerCase();

        // Unicode karakterlerini normalleştir
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("[^\\p{ASCII}]", "");

        // Boşlukları tire (-) ile değiştir
        slug = slug.replaceAll("\\s+", "-");

        // Yalnızca harf, rakam ve tire (-) karakterlerine izin ver
        slug = slug.replaceAll("[^a-z0-9-]", "");

        // Başta ve sonda tire varsa temizle
        slug = slug.replaceAll("^-+", "");
        slug = slug.replaceAll("-+$", "");

        return slug;
    }

    // ilan ekleme methodu
    @CacheEvict(value = {
            "advertisements_by_state",
            "advertisements_by_seller",
            "store_advertisements",
            "category_advertisements",
            "price_range_advertisements"
    }, allEntries = true)
    @Transactional
    public String addAdvertisement(AdvertisementDtoSecond ad) {
        Optional<UserStore> user = userStoreRepository.findById(ad.getUserStories());
        Optional<Category> category = categoryRepository.findById(ad.getCategories());
        if (user.isPresent() && user.get().getIsActive() == ActiveEnum.ACTIVE) {
            Advertisement entity = advertisementMapper.toEntites(ad);
            entity.setUserStore(user.get());
            entity.setCategory(category.get());
            entity.setIsActive(ActiveEnum.ACTIVE);
            entity.setSeoSlug(generateSeoSlug(ad.getTitle()));
            advertisementRepository.save(entity);
            return "success";
        }
        return null;
    }

    //duruma göre listeleme
    @Cacheable(value = "advertisements_by_state", key = "#state")
    @Transactional
    public Page<AdvertisementDto> findByStateAndIsActive(StateEnum state, int page , int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Advertisement> ad = advertisementRepository.findByStateAndIsActive(state, ActiveEnum.ACTIVE,pageable);
        return ad.map(advertisementMapper::toDto);
    }


    // satıcı tipine göre listele
    @Cacheable(value = "advertisements_by_seller", key = "#sellerType + '_' + #page + '_' + #size")
    @Transactional
    public Page<AdvertisementDto> findBySellerTypeAndIsActive(SellerTypeEnum sellerType, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Advertisement> ad = advertisementRepository.findBySellerTypeAndIsActive(sellerType, ActiveEnum.ACTIVE);
        if (ad.isEmpty()) return null;
        List<AdvertisementDto> dtoList = ad.stream()
                .map(advertisementMapper::toDto)
                .toList();
        Page<AdvertisementDto> resultPage = new PageImpl<>(dtoList, pageable, ad.size());
        return resultPage;

    }

    //stoktan düşmesi için method
    @Transactional
    public ResponseEntity<String> getUserStockDis(long advertisementId, int stock) {
        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        ad.setStock(ad.getStock() - stock);
        if (ad.getStock() <= 0) {
            ad.setStock(0);
            ad.setIsActive(ActiveEnum.NOT_STOCK);
            return ResponseEntity.ok("stock yeterli degil ");
        }
        advertisementRepository.save(ad);
        return ResponseEntity.ok("kalan Stok :" + ad.getStock() + " name : " + ad.getTitle());
    }

    @Transactional
    public ResponseEntity<String> getUserStockIcrement(long advertisementId, int stock) {
        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        ad.setStock(ad.getStock() + stock);
        advertisementRepository.save(ad);
        return ResponseEntity.ok(" Stok :" + ad.getStock() + " name : " + ad.getTitle());
    }

    //yıldızlama işleminde  kullanıcıların yıldız vermesi
    @CacheEvict(
            value = {"advertisement_state", "advertisement_dynamic_query_one", "advertisement_dynamic_query"},
            key = "#advertisementId"
    )
    @Transactional
    public ResponseEntity<Void> rateProduct(long advertisementId, int rate) {
        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        int newAverage = updateAverage(ad.getRate(), ad.getRateCount(), rate);
        System.out.println("Yeni Ortalama: " + newAverage);
        ad.setRate(newAverage);
        ad.setRateCount(ad.getRateCount() + 1);
        advertisementRepository.save(ad);
        return ResponseEntity.ok().build();

    }

    @CacheEvict(value = {
            "advertisements_by_state",
            "advertisements_by_seller",
            "store_advertisements",
            "category_advertisements",
            "price_range_advertisements"
    }, key = "#id")
    @Transactional
    public String deleteById(Long id) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        if (ad.isPresent()) {
            advertisementRepository.deleteById(id);
            return "deleted";
        }
        return "not found";

    }

    @CacheEvict(value = {
            "advertisements_by_state",
            "advertisements_by_seller",
            "store_advertisements",
            "category_advertisements",
            "price_range_advertisements"
    }, key = "#id")
    @Transactional
    public String  update(Long id, AdvertisementUpdateDto advertisementUpdate) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        Optional<User> user = userRepository.findById(ad.get().getUserStore().getAuthor().getId());
        if (ad.isPresent() && Objects.equals(ad.get().getId(), advertisementUpdate.getId()) && user.isPresent()) {
            Advertisement entity = ad.get();
            BeanUtils.copyProperties(advertisementUpdate, entity, getNullPropertyNames(advertisementUpdate));
            entity.setUpdatedAt(OffsetDateTime.now());
            advertisementRepository.save(entity);
            return "Updated";
        }
        return "bad request";
    }

    private int updateAverage(int oldAverage, int voteCount, int newVote) {
        return (oldAverage * voteCount + newVote) / (voteCount + 1);
    }

    private String[] getNullPropertyNames(Object source) {
        return java.util.Arrays.stream(source.getClass().getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(source) == null;
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })
                .map(field -> field.getName())
                .toArray(String[]::new);
    }

    @Cacheable(value = "store_advertisements", key = "#id + '_' + #page + '_' + #size")
    public Page<AdvertisementDto> getStoreAdvertisement(long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<UserStore> store = userStoreRepository.findById(id);
        if (store.isPresent()) {
            Page<Advertisement> advertisements = advertisementRepository.findByUserStore_Id(id, pageable);
            Page<AdvertisementDto> dto = advertisements.map(advertisementMapper::toDto);
            return dto;
        }
        return null;
    }

    public void deleteAllByShoppingStoreId(Long id) {
    }
}




