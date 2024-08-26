package com.ua.project.android_homework_telephony.interfaces;

@FunctionalInterface
public interface SmsHandler {
    void sendSms(String phoneNumber, String text);
}
