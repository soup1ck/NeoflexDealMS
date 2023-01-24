package ru.neoflex.deal.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.repository.ApplicationRepository;
import ru.neoflex.deal.repository.ClientRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/deal")
public class DealController {

    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;

    @PostMapping(value = "/application")
    public void insertClient(@RequestBody Client client) {
        clientRepository.save(client);
    }
}
