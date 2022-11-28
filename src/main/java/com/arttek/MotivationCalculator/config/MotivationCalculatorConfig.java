package com.arttek.MotivationCalculator.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Victor Datsenko
 * 23.11.2022
 * В этом классе конфигурируем все необходимое, создаем требуемые бины
 * В данном случае требуется только бин ModelMapper
 */
@Configuration
public class MotivationCalculatorConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
