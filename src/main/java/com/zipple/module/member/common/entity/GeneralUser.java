package com.zipple.module.member.common.entity;

import com.zipple.module.member.common.entity.category.HousingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "general_users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralUser {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "general_name")
    private String generalName;

    @Column(name = "general_address")
    private String generalAddress;

    @Column(name = "general_number")
    private String generalNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "housing_type")
    private HousingType housingType;

    @Column(name = "mandatory_terms")
    private Boolean mandatoryTerms;

    @Column(name = "optional_terms")
    private Boolean optionalTerms;
}
