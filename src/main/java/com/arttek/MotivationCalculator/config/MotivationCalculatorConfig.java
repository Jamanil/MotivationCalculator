package com.arttek.MotivationCalculator.config;

import com.arttek.MotivationCalculator.dto.MotivationCalculatorDto;
import com.arttek.MotivationCalculator.models.MotivationCalculator;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Victor Datsenko
 * 23.11.2022
 * В этом классе конфигурируем все необходимое, создаем требуемые бины
 * В данном случае требуется только бин ModelMapper
 * И его настройка так, чтобы он мог распарсить строку с разделителем в виде запятой
 */
@Configuration
public class MotivationCalculatorConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(MotivationCalculatorDto.class, MotivationCalculator.class)
                .addMappings( m -> {
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getRevenue, MotivationCalculator::setRevenue);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getProfitable, MotivationCalculator::setProfitable);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getRevenuePlan, MotivationCalculator::setRevenuePlan);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getFuel, MotivationCalculator::setFuel);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getFuelDiscount, MotivationCalculator::setFuelDiscount);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getDuty, MotivationCalculator::setDuty);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getCarrierNdsReimbursable, MotivationCalculator::setCarrierNdsReimbursable);
                    m.using(parseFloatFromStringWithCommas).map(MotivationCalculatorDto::getSalary, MotivationCalculator::setSalary);
                });
        return modelMapper;
    }

    private final Converter<String, Float> parseFloatFromStringWithCommas = new AbstractConverter<>() {
        @Override
        protected Float convert(String s) {
            return s == null ? null : Float.parseFloat(s.replaceAll(",", "\\."));
        }
    };
}
