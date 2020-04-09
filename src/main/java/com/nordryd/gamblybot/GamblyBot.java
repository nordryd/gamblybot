package com.nordryd.gamblybot;

import java.util.Random;

import com.nordryd.gamblybot.cardgames.entities.Deck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Main class for <i>gamblybot</i>.
 * </p>
 *
 * @author Nordryd
 */
@SpringBootApplication
public class GamblyBot
{
    /**
     * Main method.
     *
     * @param args command line arguments.
     */
    public static void main(final String... args) {
        SpringApplication.run(GamblyBot.class, args);
    }

    /**
     * Configuration for <i>gamblybot</i>
     */
    @Configuration
    @EnableAutoConfiguration
    @ComponentScan
    public static class Config
    {
        @Bean
        public Random rng() {
            return new Random();
        }

        @Bean
        public Deck deck() {
            return new Deck(rng());
        }
    }
}
