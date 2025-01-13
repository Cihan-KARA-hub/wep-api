package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.api.mapper.UserStoreMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServices {
    private final UserStoreRepository userStoreRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserStoreMapper userStoreMapper;

    public AdminServices(UserStoreRepository userStoreRepository, AdvertisementRepository advertisementRepository, UserStoreMapper userStoreMapper) {
        this.userStoreRepository = userStoreRepository;
        this.advertisementRepository = advertisementRepository;
        this.userStoreMapper = userStoreMapper;
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

    public List<UserStoreDto> getUserStore(ActiveEnum active) {
        List<UserStore> store = userStoreRepository.findByIsActive(active).get();
        List<UserStoreDto> dtos = store.stream().map(userStoreMapper::entityToDto).toList();
        return dtos;
    }


}
