package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.api.mapper.QuestionsMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Questions;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.enums.Role;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.QuestionsRepository;
import com.yelman.advertisementserver.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionsServices {
    private final QuestionsRepository questionsRepository;
    private final QuestionsMapper questionsMapper;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    public QuestionsServices(QuestionsRepository questionsRepository, QuestionsMapper questionsMapper, UserRepository userRepository, AdvertisementRepository advertisementRepository) {
        this.questionsRepository = questionsRepository;
        this.questionsMapper = questionsMapper;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
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
        Page<Questions> data = questionsRepository.findAllByAdvertisement_Id(pageable, advertisementId);
        if (data.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        List<QuestionsDto> dtoList = data.stream()
                .map(questionsMapper::toDto)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(dtoList, pageable, data.getTotalElements()));

    }
}
