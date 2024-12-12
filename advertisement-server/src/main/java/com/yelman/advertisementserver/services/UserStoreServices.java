package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.api.mapper.UserStoreMapper;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.model.enums.Role;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.repository.UserRepository;
import com.yelman.advertisementserver.repository.UserStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserStoreServices {

    private static final Logger log = LoggerFactory.getLogger(UserStoreServices.class);

    private final UserRepository userRepository;
    private final UserStoreMapper userStoreMapper;
    private final UserStoreRepository userStoreRepository;

    public UserStoreServices(UserRepository userRepository, UserStoreMapper userStoreMapper, UserStoreRepository userStoreRepository) {
        this.userRepository = userRepository;
        this.userStoreMapper = userStoreMapper;
        this.userStoreRepository = userStoreRepository;
    }

    public ResponseEntity<UserStore> CreateUserStore(UserStoreDto userStoredto) {
        User user = userRepository.findByEmail(userStoredto.getEmail()).orElse(null);
        if (user != null) {
            if (user.getAuthorities().contains(Role.ROLE_OWNER)) {
                UserStore userStore = userStoreMapper.dtoToEntity(userStoredto);
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

}
