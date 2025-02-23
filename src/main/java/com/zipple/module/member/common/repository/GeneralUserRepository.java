package com.zipple.module.member.common.repository;

import com.zipple.module.member.common.entity.GeneralUser;
import com.zipple.module.mypage.general.domain.MyPageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GeneralUserRepository extends JpaRepository<GeneralUser, Long> {

    @Query("SELECT new com.zipple.module.mypage.general.domain.MyPageResponse(u.email, g.generalName, g.generalAddress, g.housingType, g.user.createdAt) " +
            "FROM GeneralUser g JOIN g.user u WHERE u.id = :userId")
    MyPageResponse findUserInformationByUserId(@Param("userId") Long userId);
}
