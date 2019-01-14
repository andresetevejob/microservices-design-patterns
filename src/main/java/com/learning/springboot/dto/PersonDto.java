package com.learning.springboot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private String id;
    private String fullName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private List<ChildrenDto> children;
    private Address address;
    private CreatedByUser createdByUser;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChildrenDto {
        private String name;
        private Integer age;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatedByUser {
        private String email;
        private String fullName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String id;
        private String address;
        private String city;
        private String stateOrProvince;
        private String country;
        private String postalCode;
    }
}
