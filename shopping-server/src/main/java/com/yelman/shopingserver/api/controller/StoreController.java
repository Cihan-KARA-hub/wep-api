package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import com.yelman.shopingserver.sevice.QueryServices;
import com.yelman.shopingserver.sevice.StoreServices;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping/store")
public class StoreController {
    private final StoreServices storeServices;
    private final QueryServices queryServices;


    public StoreController(StoreServices storeServices, QueryServices queryServices) {
        this.storeServices = storeServices;
        this.queryServices = queryServices;

    }


    //owner market açarken hasrole =owner
    @PostMapping("/create")
    public ResponseEntity<UserStoreDto> addStore(@RequestBody UserStoreDto userStoredto) {
        return storeServices.addStore(userStoredto);
    }


    //owner market güncellerken hasrole =owner
    @PutMapping("/update")
    public ResponseEntity<HttpStatus> UpdateUserStore(@RequestBody UserStoreDto userStoredto) {
        return storeServices.UpdateUserStore(userStoredto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> DeleteUserStore(@PathVariable long id) {
        return storeServices.DeleteUserStore(id);
    }

    //admin için aktiflik durumlarına göre listele
    @GetMapping("/{activeEnum}")
    public ResponseEntity<List<UserStoreDto>> getUserStore(@PathVariable ActiveEnum activeEnum) {
        return storeServices.getUserStore(activeEnum);
    }

    //kategorilerine göre listele
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<UserStoreDto>> GetUserStoreIsCategory(@PathVariable String category,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "1") int size) {
        return storeServices.GetUserStoreIsCategory(category, size, page);
    }

    //admin verilen magzanın idsini alıp onu aktif eder  isterse wait isterse Reject ve bunu magazaya mail atar
    @PostMapping("/admin/set-active")
    public ResponseEntity<Void> GetUserStoreIsActive(@RequestParam long storeId, @RequestParam ActiveEnum isActive) {
        return storeServices.GetUserStoreIsActive(storeId, isActive);
    }

    // verilen ülke il ilçe şehirlere göre arama yapılır
    @GetMapping("/location")
    public ResponseEntity<Page<UserStoreDto>> getStoreCountryAndCity(
            @RequestParam(defaultValue = "Turkey") String country,
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size) {
        return queryServices.getCountOrCityOrDistrict(country, city, district, page, size);
    }


}

