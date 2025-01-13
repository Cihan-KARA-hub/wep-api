package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.LocationDto;
import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.api.mapper.UserStoreMapper;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.enums.Role;
import com.yelman.advertisementserver.repository.UserRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserStoreServices {

    private static final Logger log = LoggerFactory.getLogger(UserStoreServices.class);

    private final UserRepository userRepository;
    private final UserStoreMapper userStoreMapper;
    private final UserStoreRepository userStoreRepository;
    private final LocationService locationService;

    public UserStoreServices(UserRepository userRepository, UserStoreMapper userStoreMapper, UserStoreRepository userStoreRepository, LocationService locationService) {
        this.userRepository = userRepository;
        this.userStoreMapper = userStoreMapper;
        this.userStoreRepository = userStoreRepository;
        this.locationService = locationService;
    }

    public ResponseEntity<UserStore> CreateUserStore(UserStoreDto userStoredto) {
        User user = userRepository.findByEmail(userStoredto.getEmail()).orElse(null);
        if (user != null) {
            if (user.getAuthorities().contains(Role.ROLE_OWNER) && user.isEnabled()) {
                LocationDto location = locationService.getLocation(userStoredto.getCountry(), userStoredto.getCity(), userStoredto.getDistrict());
                UserStore userStore = userStoreMapper.dtoToEntity(userStoredto);
                userStore.setCountry(location.getCountries().getTitle());
                userStore.setCity(location.getSehirler().getTitle());
                userStore.setDistrict(location.getIlceler().getTitle());
                if (userStoreRepository.findByStoreName(userStoredto.getStoreName()).isPresent()) {
                    userStore.setStoreName(userStoredto.getStoreName() + "1");
                }
                userStore.setIsActive(ActiveEnum.WAITING);
                userStore.setAuthor(user);
                log.info(userStore.getStoreName());
                userStoreRepository.save(userStore);
                return ResponseEntity.status(HttpStatus.CREATED).body(userStore);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<HttpStatus> UpdateUserStore(UserStoreDto userStoredto) {
        UserStore userStore = userStoreRepository.findById(userStoredto.getId()).orElse(null);
        if (userStore != null) {
            UserStore userStoreToUpdate = userStoreMapper.dtoToEntity(userStoredto);
            userStoreRepository.save(userStoreToUpdate);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<HttpStatus> DeleteUserStore(long id) {
        UserStore userStore = userStoreRepository.findById(id).orElse(null);
        if (userStore != null) {
            userStoreRepository.delete(userStore);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<Page<UserStoreDto>> GetUserStoreIsCategory(String category, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserStore> userStore = userStoreRepository.findByCategory(category, pageable);
        Page<UserStoreDto> dto = userStore.map(userStoreMapper::entityToDto);
        return ResponseEntity.ok(dto);
    }

}
