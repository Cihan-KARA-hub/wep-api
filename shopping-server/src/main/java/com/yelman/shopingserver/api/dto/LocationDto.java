package com.yelman.shopingserver.api.dto;

import com.yelman.shopingserver.model.Countries;
import com.yelman.shopingserver.model.Ilceler;
import com.yelman.shopingserver.model.Sehirler;

public class LocationDto {
    private Countries countries;
    private Sehirler sehirler;
    private Ilceler ilceler;

    public LocationDto(Countries countries, Sehirler sehirler, Ilceler ilceler) {
        this.countries = countries;
        this.sehirler = sehirler;
        this.ilceler = ilceler;
    }

    public LocationDto() {
    }

    public Countries getCountries() {
        return countries;
    }

    public void setCountries(Countries countries) {
        this.countries = countries;
    }

    public Sehirler getSehirler() {
        return sehirler;
    }

    public void setSehirler(Sehirler sehirler) {
        this.sehirler = sehirler;
    }

    public Ilceler getIlceler() {
        return ilceler;
    }

    public void setIlceler(Ilceler ilceler) {
        this.ilceler = ilceler;
    }
}
