package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.LocationDto;
import com.yelman.advertisementserver.model.Countries;
import com.yelman.advertisementserver.model.Ilceler;
import com.yelman.advertisementserver.model.Sehirler;
import com.yelman.advertisementserver.repository.CountriesRepository;
import com.yelman.advertisementserver.repository.IlcelerRepository;
import com.yelman.advertisementserver.repository.SehirlerRepository;
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
