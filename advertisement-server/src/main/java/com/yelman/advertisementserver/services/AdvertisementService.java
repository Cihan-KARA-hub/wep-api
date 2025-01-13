package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.dto.AdvertisementDtoSecond;
import com.yelman.advertisementserver.api.mapper.AdvertisementMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Category;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.CategoryRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import com.yelman.advertisementserver.utils.SeoSlug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final UserStoreRepository userStoreRepository;
    private final CategoryRepository categoryRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository, AdvertisementMapper advertisementMapper, UserStoreRepository userStoreRepository, CategoryRepository categoryRepository) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementMapper = advertisementMapper;
        this.userStoreRepository = userStoreRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<String> addAdvertisement(AdvertisementDtoSecond ad) {

        Optional<UserStore> user = userStoreRepository.findById(ad.getUserStories());
        Optional<Category> category = categoryRepository.findById(ad.getCategories());
        if (user.isPresent() && user.get().getIsActive() == ActiveEnum.ACTIVE) {
            Advertisement entity = advertisementMapper.toEntites(ad);
            entity.setUserStore(user.get());
            entity.setCategory(category.get());
            entity.setIsActive(ActiveEnum.ACTIVE);
            entity.setSeoSlug(generateSeoSlug(ad.getTitle()));
            advertisementRepository.save(entity);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.notFound().build();
    }

    public Advertisement getAllAdvertisements(long id) {
        Advertisement dt = advertisementRepository.findById(id).orElse(null);
        if (dt == null) return null;
        return dt;
    }

    public ResponseEntity<Page<AdvertisementDto>> findByStateAndIsActive(
            StateEnum state, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Advertisement> ad = advertisementRepository.findByStateAndIsActive(state, ActiveEnum.ACTIVE);
        if (ad.isEmpty()) return ResponseEntity.notFound().build();
        List<AdvertisementDto> dtoList = ad.stream()
                .map(advertisementMapper::toDto)
                .toList();
        Page<AdvertisementDto> resultPage = new PageImpl<>(dtoList, pageable, ad.size());
        return ResponseEntity.ok(resultPage);
    }

    public ResponseEntity<Page<AdvertisementDto>> findBySellerTypeAndIsActive(
            SellerTypeEnum sellerType, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Advertisement> ad = advertisementRepository.findBySellerTypeAndIsActive(sellerType, ActiveEnum.ACTIVE);
        if (ad.isEmpty()) return ResponseEntity.notFound().build();
        List<AdvertisementDto> dtoList = ad.stream()
                .map(advertisementMapper::toDto)
                .toList();

        Page<AdvertisementDto> resultPage = new PageImpl<>(dtoList, pageable, ad.size());

        return ResponseEntity.ok(resultPage);

    }

    //stoktan düşmesi için method
    @Transactional
    public ResponseEntity<String> getUserStockDis(long advertisementId,int stock) {
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
    public ResponseEntity<String> getUserStockIcrement(long advertisementId,int stock) {
        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        ad.setStock(ad.getStock() +stock);
        advertisementRepository.save(ad);
        return ResponseEntity.ok(" Stok :" + ad.getStock() + " name : " + ad.getTitle());
    }

    //yıldızlama işleminde  kullanıcıların yıldız vermesi
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


    private int updateAverage(int oldAverage, int voteCount, int newVote) {
        return (oldAverage * voteCount + newVote) / (voteCount + 1);
    }
    public static String generateSeoSlug(String text) {
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
}




