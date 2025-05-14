package com.example.coworking_prenotation.service;

import com.example.coworking_prenotation.entity.Prenotazione;
import com.example.coworking_prenotation.repository.PrenotazioneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EmailService emailService;

    public ReminderService(PrenotazioneRepository prenotazioneRepository, EmailService emailService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.emailService = emailService;
    }

    // Esegui ogni ora
    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        LocalDate now = LocalDate.now();
        LocalTime inOneHour = LocalTime.now().plusHours(1);
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByDataAndOraInizio(now, inOneHour);
        for (Prenotazione p : prenotazioni) {
            emailService.sendEmail(
                p.getUser().getEmail(),
                "Reminder Prenotazione",
                "Hai una prenotazione tra un'ora!"
            );
        }
    }
}