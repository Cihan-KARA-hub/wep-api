package com.yelman.shopingserver.sevice;

import com.yelman.shopingserver.api.dto.EmailSendDto;
import com.yelman.shopingserver.api.dto.LocationDto;
import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.api.mapper.UserStoreMapper;
import com.yelman.shopingserver.model.Store;
import com.yelman.shopingserver.model.User;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import com.yelman.shopingserver.model.enums.Role;
import com.yelman.shopingserver.repository.ProductRepository;
import com.yelman.shopingserver.repository.StoreRepository;
import com.yelman.shopingserver.repository.UserRepository;
import com.yelman.shopingserver.utils.email.EmailServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServices {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final LocationService locationService;
    private final UserStoreMapper userStoreMapper;
    private final EmailServices emailServices;
    private final ProductRepository productRepository;


    public StoreServices(UserRepository userRepository, StoreRepository storeRepository, LocationService locationService, UserStoreMapper userStoreMapper, EmailServices emailServices, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.locationService = locationService;
        this.userStoreMapper = userStoreMapper;
        this.emailServices = emailServices;
        this.productRepository = productRepository;
    }

    @Transactional
    public ResponseEntity<UserStoreDto> addStore(UserStoreDto userStoredto) {
        User user = userRepository.findByEmailAndEnabledIsTrue(userStoredto.getEmail()).orElse(null);
        if (user != null) {
            if (user.getAuthorities().contains(Role.ROLE_OWNER) && user.isEnabled()) {
                LocationDto location = locationService.getLocation(userStoredto.getCountry(), userStoredto.getCity(), userStoredto.getDistrict());
                Store userStore = userStoreMapper.dtoToEntity(userStoredto);
                userStore.setCountry(location.getCountries().getTitle());
                userStore.setCity(location.getSehirler().getTitle());
                userStore.setDistrict(location.getIlceler().getTitle());
                if (storeRepository.findByStoreName(userStoredto.getStoreName()).isPresent()) {
                    int a = (int) (Math.random() * 11);
                    userStore.setStoreName(userStoredto.getStoreName() + a);
                }
                userStore.setIsActive(ActiveEnum.WAITING);
                userStore.setAuthor(user);
                storeRepository.save(userStore);
                return ResponseEntity.status(HttpStatus.CREATED).body(userStoredto);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Transactional
    public ResponseEntity<HttpStatus> UpdateUserStore(UserStoreDto userStoredto) {
        Store userStore = storeRepository.findById(userStoredto.getId()).orElse(null);
        if (userStore != null) {
            Store userStoreToUpdate = userStoreMapper.dtoToEntity(userStoredto);
            storeRepository.save(userStoreToUpdate);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // magza ve ürünlerini siler
    @Transactional
    public ResponseEntity<HttpStatus> DeleteUserStore(long id, long userId) {
        Store userStore = storeRepository.findById(id).orElse(null);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userStore != null && userOptional.isPresent()) {
            if (userOptional.get().getAuthorities().contains(Role.ROLE_ADMIN) || userOptional.get().getId() == userStore.getAuthor().getId()) {
                productRepository.deleteAllByShoppingStoreId(userStore.getId());
                storeRepository.deleteById(userStore.getId());
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //admin için aktiflik durumlarına göre
    @Transactional
    public ResponseEntity<List<UserStoreDto>> getUserStore(ActiveEnum activeEnum) {
        List<Store> userStor = storeRepository.findByIsActive(activeEnum);
        if (!userStor.isEmpty()) {
            List<UserStoreDto> userStoreDtoList = userStor.stream().map(userStoreMapper::entityToDto).toList();
            return new ResponseEntity<>(userStoreDtoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // istenilen kategorideki sayfalı şekilde getir
    @Transactional
    public ResponseEntity<Page<UserStoreDto>> GetUserStoreIsCategory(String category, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> userStore = storeRepository.findByCategoryAndIsActive(category, ActiveEnum.ACTIVE, pageable);
        Page<UserStoreDto> dto = userStore.map(userStoreMapper::entityToDto);
        return ResponseEntity.ok(dto);
    }

    //admin verilen magzanın idsini alıp onu aktif eder  isterse wait isterse Reject
    @Transactional
    public ResponseEntity<Void> GetUserStoreIsActive(long storeId, ActiveEnum activeEnum) {
        Store userStore = storeRepository.findById(storeId).orElse(null);
        if (userStore != null) {
            userStore.setIsActive(activeEnum);
            storeRepository.save(userStore);
            EmailSendDto emailSendDto = new EmailSendDto();
            emailSendDto.setRecipient(userStore.getEmail());
            emailSendDto.setSubject("Marketiniz  : " + userStore.getStoreName());
            emailSendDto.setMsgBody("Market Adı :" + userStore.getStoreName() + "\n" +
                    "Market durumunuz " + userStore.getIsActive().name().toLowerCase() +
                    " Oluşturulma zamanı :" + userStore.getCreatedAt());
            emailServices.sendSimpleMail(emailSendDto);

            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
