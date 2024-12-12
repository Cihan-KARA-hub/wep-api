package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.mapper.AdvertisementMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.CategoryRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseEntity<String> addAdvertisement(AdvertisementDto ad) {
        Optional<UserStore> user = userStoreRepository.findById(ad.getUserStore().getId());
        if (user.isPresent()) {
            Advertisement entity = advertisementMapper.toEntity(ad);
            entity.setUserStore(userStoreRepository.findById(ad.getUserStore().getId()).orElse(null));
            entity.setCategory(categoryRepository.findById(ad.getCategory().getId()).orElse(null));
            entity.setIsActive(ActiveEnum.WAITING);
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

    public List<Advertisement> getCategoryAdvertisements(long id) {
        return advertisementRepository.findByCategory_Id(id);
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
    public ResponseEntity<String> getUserStockDis(long advertisementId) {
        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);
        if (ad == null) return ResponseEntity.notFound().build();
        ad.setStock(ad.getStock() - 1);
        if (ad.getStock() <= 0) return ResponseEntity.notFound().build();
        advertisementRepository.save(ad);
        return ResponseEntity.ok("kalan Stok :" + ad.getStock() + " name : " + ad.getTitle());
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
}




