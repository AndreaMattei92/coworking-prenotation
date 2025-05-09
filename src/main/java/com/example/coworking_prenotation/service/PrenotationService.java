package com.example.coworking_prenotation.service;

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

    public PrenotationService(PrenotazioneRepository prenotazioneRepository, UserRepository userRepository, PostazioneRepository postazioneRepository, PostazioneMapper postazioneMapper, UserMapper userMapper) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.userRepository = userRepository;
        this.postazioneRepository = postazioneRepository;
        this.postazioneMapper = postazioneMapper;
        this.userMapper = userMapper;
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
                .data(LocalDateTime.now())
                .oraInizio(LocalTime.of(9, 0))
                .oraFine(LocalTime.of(17, 0))
                .build();

        prenotazioneRepository.save(prenotazione);

        PrenotazioneResponseDTO responseDTO = PrenotazioneResponseDTO.builder()
                .userId(user.getId())
                .postazioneId(postazione.getId())
                .data(prenotazione.getData())
                .oraInizio(prenotazione.getOraInizio())
                .oraFine(prenotazione.getOraFine())
                .build();

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
        prenotazione.setData(LocalDateTime.now());
        prenotazione.setOraInizio(LocalTime.of(9, 0));
        prenotazione.setOraFine(LocalTime.of(17, 0));

        prenotazioneRepository.save(prenotazione);

        return ResponseEntity.ok("Prenotazione modificata con successo");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> cancellaPrenotazione(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione not found"));

        prenotazioneRepository.delete(prenotazione);

        return ResponseEntity.ok("Prenotazione cancellata con successo");
    }
}




