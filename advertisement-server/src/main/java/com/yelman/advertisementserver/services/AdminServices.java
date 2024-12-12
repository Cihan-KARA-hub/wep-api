package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServices {
    private final UserStoreRepository userStoreRepository;
    private final AdvertisementRepository advertisementRepository;

    public AdminServices(UserStoreRepository userStoreRepository, AdvertisementRepository advertisementRepository) {
        this.userStoreRepository = userStoreRepository;
        this.advertisementRepository = advertisementRepository;
    }

    public ResponseEntity<HttpEntity<String>> getStoreAccept(long id) {
        UserStore store = userStoreRepository.findById(id).orElse(null);
        if (store != null) {
            store.setIsActive(ActiveEnum.ACTIVE);
            userStoreRepository.save(store);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpEntity<String>> getAdvertisementAccept(long id) {
        Optional<Advertisement> store = advertisementRepository.findById(id);
        if (store.isPresent()) {
            store.get().setIsActive(ActiveEnum.ACTIVE);
            advertisementRepository.save(store.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
