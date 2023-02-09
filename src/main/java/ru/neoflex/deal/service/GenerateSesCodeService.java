package ru.neoflex.deal.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerateSesCodeService {

    private static final int min = 1000;
    private static final int max = 9999;

    public int getRandomNumberInts() {
        Random random = new Random();
        return random.ints(min, max).findFirst().orElseThrow();
    }
}
