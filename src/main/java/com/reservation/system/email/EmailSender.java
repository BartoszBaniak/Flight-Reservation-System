package com.reservation.system.email;

import org.springframework.stereotype.Repository;

@Repository
public interface EmailSender {
    void sendEmail(String to, String email, String subject);
}
