package com.zipple.module.member.email;

import com.zipple.common.oauth.OAuthProvider;
import com.zipple.module.member.common.entity.AgentUser;
import com.zipple.module.member.common.entity.GeneralUser;
import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.entity.category.AgentSpecialty;
import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.member.common.entity.category.HousingType;
import com.zipple.module.member.common.repository.AgentUserRepository;
import com.zipple.module.member.common.repository.GeneralUserRepository;
import com.zipple.module.member.common.repository.UserRepository;
import com.zipple.module.member.email.model.EmailAgentRequest;
import com.zipple.module.member.email.model.EmailGeneralRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailRegisterService {
    private final String URL = "http://44.203.190.167:8081/zipple/";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeneralUserRepository generalUserRepository;
    private final AgentUserRepository agentUserRepository;

    @Transactional
    public void emailRegisterGeneral(EmailGeneralRequest emailGeneralRequest) {
        String email = emailGeneralRequest.getEmail();
        if(userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        String encodedPassword = passwordEncoder.encode(emailGeneralRequest.getPassword());
        OAuthProvider oAuthProvider = OAuthProvider.STANDALONE;
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .oAuthProvider(oAuthProvider)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        HousingType housingType = HousingType.fromValue(emailGeneralRequest.getHousingType());
        GeneralUser generalUser = GeneralUser.builder()
                .user(user)
                .generalName(emailGeneralRequest.getGeneralName())
                .generalAddress(emailGeneralRequest.getGeneralAddress())
                .generalNumber(emailGeneralRequest.getGeneralNumber())
                .housingType(housingType)
                .mandatoryTerms(true)
                .optionalTerms(emailGeneralRequest.getMarketingNotificationTerms())
                .build();

        generalUserRepository.save(generalUser);
        log.info("이메일 회원가입 완료");
    }

    @Transactional
    public void emailRegisterAgent(EmailAgentRequest emailAgentRequest, List<MultipartFile> agentCertificationDocuments, MultipartFile agentImage) {
        String email = emailAgentRequest.getEmail();
        if(userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        String encodedPassword = passwordEncoder.encode(emailAgentRequest.getPassword());
        OAuthProvider oAuthProvider = OAuthProvider.STANDALONE;

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .oAuthProvider(oAuthProvider)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        AgentType agentType = AgentType.fromValue(emailAgentRequest.getAgentType());
        AgentSpecialty agentSpecialty = AgentSpecialty.getByDescription(emailAgentRequest.getAgentSpecialty());

        String agentName = null;
        String agentContactNumber = null;

        if (agentType == AgentType.AFFILIATED_AGENT) {
            agentName = emailAgentRequest.getAgentName();
            agentContactNumber = emailAgentRequest.getAgentContactNumber();
        }

        AgentUser agentUser = AgentUser.builder()
                .user(user)
                .agentType(agentType)
                .agentSpecialty(agentSpecialty)
                .businessName(emailAgentRequest.getBusinessName())
                .agentRegistrationNumber(emailAgentRequest.getAgentRegistrationNumber())
                .primaryContactNumber(emailAgentRequest.getPrimaryContactNumber())
                .officeAddress(emailAgentRequest.getOfficeAddress())
                .ownerName(emailAgentRequest.getOwnerName())
                .ownerContactNumber(emailAgentRequest.getOwnerContactNumber())
                .singleHouseholdExpertRequest(emailAgentRequest.getSingleHouseholdExpertRequest())
                .agentOfficeRegistrationCertificate(
                        URL + documentsPath(agentCertificationDocuments, 0, user.getId())
                )
                .businessRegistrationCertification(
                        URL + documentsPath(agentCertificationDocuments, 1, user.getId())
                )
                .agentLicense(
                        URL + documentsPath(agentCertificationDocuments, 2, user.getId())
                )
                .agentImage(URL + imagePath(agentImage, user.getId()))
                .agentName(agentName)
                .agentContactNumber(agentContactNumber)
                .introductionTitle(emailAgentRequest.getIntroductionTitle())
                .introductionContent(emailAgentRequest.getIntroductionContent())
                .build();

        agentUserRepository.save(agentUser);

        log.info("공인중개사 회원가입 성공");
    }

    private String documentsPath(List<MultipartFile> agentCertificationDocuments, int count, Long userId) {
        int index = 0;
        for (MultipartFile file : agentCertificationDocuments) {
            if (index == count) {
                try {
                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String baseDir = "/home/ubuntu/zipple/upload";
                    String directoryPath = baseDir + "/" + userId + "/" + date;

                    Path directory = Paths.get(directoryPath);
                    if (!Files.exists(directory)) {
                        Files.createDirectories(directory);
                    }

                    String fileName = file.getOriginalFilename();
                    if (fileName == null || fileName.isEmpty()) {
                        fileName = "default_filename";
                    }
                    Path filePath = directory.resolve(fileName);

                    file.transferTo(filePath.toFile());

                    return directoryPath + "/" + fileName;
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            index++;
        }
        return "";
    }

    private String imagePath(MultipartFile agentCertificationDocuments, Long userId) {
        return "";
    }
}
