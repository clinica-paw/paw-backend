package com.clinicapaw.backend_clinicapaw.service.interfaces;

public interface ISendEmailService {

    void sendEmail(String toUser, String subject, String messageBody);

}
