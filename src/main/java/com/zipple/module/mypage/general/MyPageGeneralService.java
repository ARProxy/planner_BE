package com.zipple.module.mypage.general;

import com.zipple.common.utils.GetMember;
import com.zipple.module.member.common.entity.GeneralUser;
import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.entity.category.HousingType;
import com.zipple.module.member.common.repository.GeneralUserRepository;
import com.zipple.module.member.common.repository.UserRepository;
import com.zipple.module.mypage.general.domain.MyPageResponse;
import com.zipple.module.mypage.general.domain.MyPageUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageGeneralService {
    private final GetMember getMember;
    private final GeneralUserRepository generalUserRepository;
    private final UserRepository userRepository;

    public MyPageResponse getMyPageInformation() {
        Long userId = getMember.getCurrentMember().getId();
        User users = userRepository.findById(userId).orElse(null);
        GeneralUser user = generalUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        return new MyPageResponse(
                user.getUser().getEmail(),
                user.getGeneralName(),
                user.getGeneralAddress(),
                user.getHousingType(), // 자동으로 한글로 변환됨 (위에서 추가한 생성자 사용)
                users.getCreatedAt()
        );
    }

    @Transactional
    public void updateMyPageInformation(MyPageUpdateRequest myPageUpdateRequest) {
        // 현재 로그인한 사용자 가져오기
        User currentUser = getMember.getCurrentMember();
        Long userId = currentUser.getId();
        User user = userRepository.findById(userId).orElse(null);

        GeneralUser generalUser = generalUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (myPageUpdateRequest.getEmail() != null) {
            user.setEmail(myPageUpdateRequest.getEmail());
        }

        if (myPageUpdateRequest.getGeneralName() != null) {
            generalUser.setGeneralName(myPageUpdateRequest.getGeneralName());
        }

        if (myPageUpdateRequest.getGeneralAddress() != null) {
            generalUser.setGeneralAddress(myPageUpdateRequest.getGeneralAddress());
        }

        if (myPageUpdateRequest.getHousingType() != null) {
            generalUser.setHousingType(HousingType.fromValue(myPageUpdateRequest.getHousingType()));
        }

        userRepository.save(user);
        generalUserRepository.save(generalUser);
    }

    @Transactional
    public void deleteMyPageUser() {
        User currentUser = getMember.getCurrentMember();
        Long userId = currentUser.getId();

        userRepository.findById(userId).ifPresentOrElse(
                userRepository::delete,
                () -> {
                    throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                }
        );
    }
}
