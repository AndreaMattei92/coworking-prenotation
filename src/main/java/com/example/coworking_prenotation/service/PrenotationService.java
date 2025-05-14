package com.example.coworking_prenotation.service;

import com.example.coworking_prenotation.dto.postazionedto.PostazioneDTO;
import com.example.coworking_prenotation.dto.prenotazionedto.PrenotazioneRequestDTO;
import com.example.coworking_prenotation.dto.prenotazionedto.PrenotazioneResponseDTO;
import com.example.coworking_prenotation.entity.Postazione;
import com.example.coworking_prenotation.entity.Prenotazione;
import com.example.coworking_prenotation.entity.User;
import com.example.coworking_prenotation.mapper.PostazioneMapper;
import com.example.coworking_prenotation.mapper.UserMapper;
import com.example.coworking_prenotation.repository.PostazioneRepository;
import com.example.coworking_prenotation.repository.PrenotazioneRepository;
import com.example.coworking_prenotation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotationService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final UserRepository userRepository;
    private final PostazioneRepository postazioneRepository;
    private final PostazioneMapper postazioneMapper;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public PrenotationService(PrenotazioneRepository prenotazioneRepository, UserRepository userRepository, PostazioneRepository postazioneRepository, PostazioneMapper postazioneMapper, UserMapper userMapper, EmailService emailService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.userRepository = userRepository;
        this.postazioneRepository = postazioneRepository;
        this.postazioneMapper = postazioneMapper;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PrenotazioneResponseDTO> createPrenotation(PrenotazioneRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Postazione postazione = postazioneRepository.findById(requestDTO.getPostazioneId())
                .orElseThrow(() -> new RuntimeException("Postazione not found"));

        Prenotazione prenotazione = Prenotazione.builder()
                .user(user)
                .postazione(postazione)
                .data(requestDTO.getData()) // Assicurati che il DTO abbia il campo data
                .oraInizio(requestDTO.getOraInizio()) // Assicurati che il DTO abbia il campo oraInizio
                .oraFine(requestDTO.getOraFine())     // Assicurati che il DTO abbia il campo oraFine
                .build();

        prenotazioneRepository.save(prenotazione);

        PrenotazioneResponseDTO responseDTO = PrenotazioneResponseDTO.builder()
                .userId(user.getId())
                .postazioneId(postazione.getId())
                .data(prenotazione.getData())
                .oraInizio(prenotazione.getOraInizio())
                .oraFine(prenotazione.getOraFine())
                .build();

        emailService.sendEmail(
                user.getEmail(),
                "Conferma Prenotazione",
                "La tua prenotazione è stata confermata!"
        );

        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<PrenotazioneResponseDTO> getDettaglioPrenotazione(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Prenotazione> prenotazione= prenotazioneRepository.findByUserId(id);

        if (prenotazione.isPresent()) {
            Prenotazione prenotazioneEntity = prenotazione.get();
            PrenotazioneResponseDTO responseDTO = PrenotazioneResponseDTO.builder()
                    .id(prenotazioneEntity.getId())
                    .userId(user.getId())
                    .postazioneId(prenotazioneEntity.getPostazione().getId())
                    .data(prenotazioneEntity.getData())
                    .oraInizio(prenotazioneEntity.getOraInizio())
                    .oraFine(prenotazioneEntity.getOraFine())
                    .build();

            return List.of(responseDTO);
        } else {
            throw new RuntimeException("Prenotazione not found");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PrenotazioneResponseDTO> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        return prenotazioni.stream()
                .map(prenotazione -> PrenotazioneResponseDTO.builder()
                        .id(prenotazione.getId())
                        .userId(prenotazione.getUser().getId())
                        .postazioneId(prenotazione.getPostazione().getId())
                        .data(prenotazione.getData())
                        .oraInizio(prenotazione.getOraInizio())
                        .oraFine(prenotazione.getOraFine())
                        .build())
                .toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> modificaPrenotazione(Long id, PrenotazioneRequestDTO requestDTO) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione not found"));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Postazione postazione = postazioneRepository.findById(requestDTO.getPostazioneId())
                .orElseThrow(() -> new RuntimeException("Postazione not found"));

        prenotazione.setUser(user);
        prenotazione.setPostazione(postazione);
        prenotazione.setData(requestDTO.getData());
        prenotazione.setOraInizio(requestDTO.getOraInizio());
        prenotazione.setOraFine(requestDTO.getOraFine());

        prenotazioneRepository.save(prenotazione);

        emailService.sendEmail(
                user.getEmail(),
                "modifica Prenotazione",
                "La tua prenotazione è stata modificata!"
        );

        return ResponseEntity.ok("Prenotazione modificata con successo");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> cancellaPrenotazione(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione not found"));

        User user = prenotazione.getUser();

        prenotazioneRepository.delete(prenotazione);
        emailService.sendEmail(
                user.getEmail(),
                "Cancellazione Prenotazione",
                "La tua prenotazione è stata cancellata."
        );

        return ResponseEntity.ok("Prenotazione cancellata con successo");
    }

    public List<PostazioneDTO> getDisponibilita(Long postazioneId,LocalDate Data, LocalTime oraInizio, LocalTime oraFine) {
        Postazione postazione = postazioneRepository.findById(postazioneId)
                .orElseThrow(() -> new RuntimeException("Postazione not found"));

        List<Prenotazione> prenotazioni = prenotazioneRepository.findByPostazioneAndData(postazione, Data);

        boolean disponibile = true;

        for (Prenotazione p : prenotazioni) {
            if (!(oraFine.isBefore(p.getOraInizio()) || oraInizio.isAfter(p.getOraFine()))) {
                disponibile = false;
                break;
            }
        }

        PostazioneDTO postazioneDTO = postazioneMapper.toPostazioneDTO(postazione);
        postazioneDTO.setDisponibile(disponibile);

        return List.of(postazioneDTO);
    }
}




