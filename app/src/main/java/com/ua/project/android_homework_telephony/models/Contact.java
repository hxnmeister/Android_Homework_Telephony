package com.ua.project.android_homework_telephony.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Serializable {
    private Integer id;
    private String lastName;
    private String firstName;
    private String phoneNumber;
}
