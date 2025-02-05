package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import com.yelman.shopingserver.sevice.QueryServices;
import com.yelman.shopingserver.sevice.StoreServices;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//Magaza islemleri için bir controller
@RestController
@RequestMapping("/shopping/store")
public class StoreController {
    private final StoreServices storeServices;


    public StoreController(StoreServices storeServices) {
        this.storeServices = storeServices;


    }
    //owner market açarken hasrole =owner
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/create")
    public ResponseEntity<UserStoreDto> addStore(@RequestBody UserStoreDto userStoredto) {
        return storeServices.addStore(userStoredto);
    }
    //owner market güncellerken hasrole =owner
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<HttpStatus> UpdateUserStore(@RequestBody UserStoreDto userStoredto) {
        return storeServices.UpdateUserStore(userStoredto);
    }
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<HttpStatus> DeleteUserStore(@PathVariable long id,@PathVariable long userId) {
        return storeServices.DeleteUserStore(id,userId);
    }

    //admin için aktiflik durumlarına göre listele
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{activeEnum}")
    public ResponseEntity<List<UserStoreDto>> getUserStore(@PathVariable ActiveEnum activeEnum) {
        return storeServices.getUserStore(activeEnum);
    }



    //admin verilen magzanın idsini alıp onu aktif eder  isterse wait isterse Reject ve bunu magazaya mail atar
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/set-active")
    public ResponseEntity<Void> GetUserStoreIsActive(@RequestParam long storeId, @RequestParam ActiveEnum isActive) {
        return storeServices.GetUserStoreIsActive(storeId, isActive);
    }



}

