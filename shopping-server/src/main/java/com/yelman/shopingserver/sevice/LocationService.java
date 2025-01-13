package com.yelman.shopingserver.sevice;

import com.yelman.shopingserver.api.dto.LocationDto;
import com.yelman.shopingserver.model.Countries;
import com.yelman.shopingserver.model.Ilceler;
import com.yelman.shopingserver.model.Sehirler;
import com.yelman.shopingserver.repository.CountriesRepository;
import com.yelman.shopingserver.repository.IlcelerRepository;
import com.yelman.shopingserver.repository.SehirlerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {
    private final SehirlerRepository sehirlerRepository;
    private final CountriesRepository countriesRepository;
    private final IlcelerRepository ilcelerRepository;

    public LocationService(SehirlerRepository sehirlerRepository, CountriesRepository countriesRepository, IlcelerRepository ilcelerRepository) {
        this.sehirlerRepository = sehirlerRepository;
        this.countriesRepository = countriesRepository;
        this.ilcelerRepository = ilcelerRepository;
    }

    @Transactional
    public LocationDto getLocation(String countryName, String sehirName, String ilceName) {
        Countries country = countriesRepository.findByTitleIgnoreCase(countryName);
        Sehirler sehir = sehirlerRepository.findByTitleIgnoreCase(sehirName);
        Ilceler ilceler = null;
        if (ilceName != null) {
            ilceler = ilcelerRepository.findByTitleIgnoreCase(ilceName);
        }

        if (sehir == null && country == null) {
            return null;
        }
        LocationDto locationDto = new LocationDto();
        locationDto.setCountries(country);
        locationDto.setSehirler(sehir);
        locationDto.setIlceler(ilceler);
        return locationDto;
    }
}
