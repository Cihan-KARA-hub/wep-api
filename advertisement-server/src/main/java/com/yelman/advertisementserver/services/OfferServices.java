package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.EmailSendDto;
import com.yelman.advertisementserver.api.dto.OfferDto;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Offer;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.enums.CurrencyEnum;
import com.yelman.advertisementserver.model.enums.Role;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.OfferRepository;
import com.yelman.advertisementserver.repository.UserRepository;
import com.yelman.advertisementserver.utils.email.EmailServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OfferServices {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final EmailServices emailServices;

    public OfferServices(OfferRepository offerRepository,
                         UserRepository userRepository,
                         AdvertisementRepository advertisementRepository, EmailServices emailServices) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;

        this.emailServices = emailServices;
    }

    @Transactional
    public ResponseEntity<HttpStatus> postOffer(OfferDto offer) {
        Optional<User> userOptional = userRepository.findById(offer.getUser());
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(offer.getAdvertisement());

        if (userOptional.isEmpty() || advertisementOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        Advertisement advertisement = advertisementOptional.get();
        if (!user.getAuthorities().contains(Role.ROLE_SUBSCRIBE)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Offer newOffer = createOffer(user, advertisement, offer.getPrice(), offer.getCurrency());
        offerRepository.save(newOffer);
        CompletableFuture.runAsync(() -> sendOfferEmails(user, advertisement, offer.getPrice(), offer.getCurrency()));


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Offer createOffer(User user, Advertisement advertisement, BigDecimal price, CurrencyEnum currency) {
        Offer offer = new Offer();
        offer.setUser(user);
        offer.setPrice(price);
        offer.setAdvertisement(advertisement);
        offer.setCurrency(currency);
        return offer;
    }

    private void sendOfferEmails(User user, Advertisement advertisement, BigDecimal price, CurrencyEnum currencyEnum) {
        // Teklif gönderen kullanıcıya mail
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
        String formattedDate = sdf.format(new Date());
        EmailSendDto userEmail = new EmailSendDto();
        userEmail.setRecipient(user.getEmail());
        userEmail.setSubject(advertisement.getTitle() + " için teklif verdiniz");
        //hangi tip para teklifi verilidigi eklenebilir
        userEmail.setMsgBody("Bir Teklif Verdiniz: " +
                price + " "
                + currencyEnum +
                " - Kullanıcı: "
                + user.getName()
                + "\n" + "kalan stok adedi :" + advertisement.getStock() +
                "\n" + "Ürünün fiyatı" + advertisement.getPrice() +
                "\n" + formattedDate);
        emailServices.sendSimpleMail(userEmail);
        // İlan sahibine mail
        EmailSendDto ownerEmail = new EmailSendDto();
        ownerEmail.setRecipient(advertisement.getUserStore().getEmail());
        ownerEmail.setSubject("Ürününüze Yeni Bir Teklif Geldi: " + advertisement.getTitle());
        ownerEmail.setMsgBody("Ürüne verilen teklif: " + price + " " + currencyEnum +
                "\nKoyduğunuz fiyat: " + advertisement.getPrice() + " " + advertisement.getCurrency() +
                "\nKalan stok: " + advertisement.getStock() +
                "\n" + formattedDate);
        emailServices.sendSimpleMail(ownerEmail);
    }


}
