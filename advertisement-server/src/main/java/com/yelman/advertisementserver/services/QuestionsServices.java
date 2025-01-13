package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.EmailSendDto;
import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.api.mapper.QuestionsMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Questions;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.model.enums.Role;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.QuestionsRepository;
import com.yelman.advertisementserver.repository.UserRepository;
import com.yelman.advertisementserver.utils.email.EmailServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionsServices {
    private final QuestionsRepository questionsRepository;
    private final QuestionsMapper questionsMapper;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final EmailServices emailServices;

    public QuestionsServices(QuestionsRepository questionsRepository, QuestionsMapper questionsMapper, UserRepository userRepository, AdvertisementRepository advertisementRepository, EmailServices emailServices) {
        this.questionsRepository = questionsRepository;
        this.questionsMapper = questionsMapper;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.emailServices = emailServices;
    }

    @Transactional
    public ResponseEntity<HttpStatus> addQuestion(QuestionsDto dto) {
        User user = userRepository.findById(dto.getUserId()).get();
        if (!user.getAuthorities().contains(Role.ROLE_SUBSCRIBE)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Advertisement advertisement = advertisementRepository.findById(dto.getAdvertisementId()).get();
        if (advertisement == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Questions questions = new Questions();
        questions.setUser(user);
        questions.setAdvertisement(advertisement);
        questions.setTitle(dto.getTitle());
        questions.setContent(dto.getContent());

        questionsRepository.save(questions);
        sendEmail(advertisement, advertisement.getUserStore(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Transactional
    public ResponseEntity<HttpStatus> answerQuestion(long questionId, QuestionsDto dto) {
        User user = userRepository.findById(dto.getUserId()).get();
        if (!user.getAuthorities().contains(Role.ROLE_SUBSCRIBE)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Advertisement advertisement = advertisementRepository.findById(dto.getAdvertisementId()).get();
        if (advertisement == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Questions> questions1 = questionsRepository.findById(questionId);
        if (questions1.isPresent()) {
            Questions questions = new Questions();
            questions.setUser(user);
            questions.setParentId(questionId);
            questions.setAdvertisement(advertisement);
            questions.setTitle(dto.getTitle());
            questions.setContent(dto.getContent());
            questionsRepository.save(questions);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteQuestion(long questionId, long userId) {
        Questions data = questionsRepository.findById(questionId).get();
        if (data != null && data.getUser().getId() == userId) {
            if (data.getParentId() == 0) {
                List<Questions> questionsList = questionsRepository.findAllByParentId(data.getId());
                questionsList.forEach(questions -> questionsRepository.deleteById(questions.getId()));
            }
            questionsRepository.deleteById(data.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity<Page<QuestionsDto>> getParentQuestion(long advertisementId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Questions> data = questionsRepository.findAllByAdvertisement_IdAndParentId(pageable, advertisementId, 0);
        if (data.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Page<QuestionsDto> dtoList = data
                .map(questionsMapper::toDto);
        return ResponseEntity.ok(dtoList);

    }

    public ResponseEntity<Page<QuestionsDto>> getSubQuestions(long parentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (parentId == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Page<Questions> data = questionsRepository.findAllByParentId(parentId, pageable);
        if (data.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Page<QuestionsDto> dtoPage = data.map(questionsMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    private void sendEmail(Advertisement advertisement, UserStore user, QuestionsDto questionsDto) {
        EmailSendDto dto = new EmailSendDto();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
        String formattedDate = sdf.format(new Date());
        dto.setRecipient(user.getEmail());
        dto.setSubject("Yeni bir sorunuz var : " + questionsDto.getTitle());
        dto.setMsgBody(questionsDto.getContent() + "\n"
                + user.getName() + " size yorum yaptı " + "\n" +
                " ilan no: " + advertisement.getId() + "\n" + formattedDate);

    }
}
