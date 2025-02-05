package com.yelman.advertisementserver.api.dto;

import com.yelman.advertisementserver.model.Countries;
import com.yelman.advertisementserver.model.Ilceler;
import com.yelman.advertisementserver.model.Sehirler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto implements Serializable {
    private Countries countries;
    private Sehirler sehirler;
    private Ilceler ilceler;
}
