package com.dndn.backend.dndn.domain.interest.application;

import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestInfoResDto;
import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestListResDto;
import com.dndn.backend.dndn.domain.interest.domain.Interest;
import com.dndn.backend.dndn.domain.interest.domain.repository.InterestRepository;
import com.dndn.backend.dndn.domain.interest.exception.InterestException;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public InterestInfoResDto updateInterest(Long userId, Long welfareId, boolean interestStatus) {
        if (userId == null || welfareId == null) {
            throw new InterestException(ErrorStatus._BAD_REQUEST);
        }

        if (!userRepository.existsById(userId)) {
            throw new InterestException(ErrorStatus._USER_NOT_FOUND);
        }

        // 1) 존재 시: 상태만 변경
        var existing = interestRepository.findByUserIdAndWelfareId(userId, welfareId);
        if (existing.isPresent()) {
            Interest interest = existing.get();
            interest.updateStatus(interestStatus);
            return InterestInfoResDto.from(interest);
        }

        // 2) 미존재 시: 새로 생성 (엔티티 로딩 없이 reference로 연결)
        User user = em.find(User.class, userId);
        if (user == null) throw new InterestException(ErrorStatus._USER_NOT_FOUND);

        Welfare welfare = em.find(Welfare.class, welfareId);
        if (welfare == null) throw new InterestException(ErrorStatus._WELFARE_NOT_FOUND);

        try {
            Interest created = interestRepository.save(
                    Interest.builder()
                            .user(user)
                            .welfare(welfare)
                            .interestStatus(interestStatus)
                            .build()
            );
            em.flush();
            return InterestInfoResDto.from(created);

        } catch (DataIntegrityViolationException dup) {
            em.clear();

            interestRepository.updateStatus(userId, welfareId, interestStatus);

            Interest interest = interestRepository.findByUserIdAndWelfareId(userId, welfareId)
                    .orElseThrow(
                            () -> new InterestException(ErrorStatus._INTERNAL_SERVER_ERROR));

            return InterestInfoResDto.from(interest);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InterestListResDto getInterest(Long userId, Boolean interestStatus) {
        if (userId == null) {
            throw new InterestException(ErrorStatus._BAD_REQUEST);
        }
        if (!userRepository.existsById(userId)) {
            throw new InterestException(ErrorStatus._USER_NOT_FOUND);
        }

        List<Interest> interestList = interestRepository.findWithOptionalStatus(userId, interestStatus);
        return InterestListResDto.from(
                interestList.stream()
                        .map(InterestInfoResDto::from)
                        .toList()
        );
    }
}

