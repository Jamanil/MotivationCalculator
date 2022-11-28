package com.arttek.MotivationCalculator.controllers;

import com.arttek.MotivationCalculator.dto.MotivationCalculatorDto;
import com.arttek.MotivationCalculator.models.MotivationCalculator;
import com.arttek.MotivationCalculator.services.MotivationCalculatorServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author Victor Datsenko
 * 23.11.2022
 * Контроллер, ответственный за ввод данных для расчета и запуск самого процесса расчета.
 */
@SuppressWarnings("SameReturnValue")
@Controller
@RequiredArgsConstructor
public class MotivationCalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(MotivationCalculatorController.class);
    private final ModelMapper modelMapper;
    private final MotivationCalculatorServices motivationCalculatorServices;
    @GetMapping
    private String showCalculationPage(Model model) {
        logger.debug("Вывод чистой страницы для расчета мотивации");
        model.addAttribute("motivationCalculatorDto", new MotivationCalculatorDto());
        return "calculation";
    }

    @PostMapping
    private String doCalc(@ModelAttribute("motivationCalculatorDto") @Valid MotivationCalculatorDto motivationCalculatorDto,
                          BindingResult bindingResult,
                          Model model) {
        logger.debug("Получены значения полей от пользователя");
        if (!bindingResult.hasErrors()) {
            try {
                MotivationCalculator motivationCalculator = modelMapper.map(motivationCalculatorDto, MotivationCalculator.class);
                motivationCalculatorServices.fillCalculatedFields(motivationCalculator);
                motivationCalculatorDto = modelMapper.map(motivationCalculator, MotivationCalculatorDto.class);
                model.addAttribute("motivationCalculatorDto", motivationCalculatorDto);
            } catch (NullPointerException npe) {
                model.addAttribute("constantsError", npe);
                logger.error(npe.getMessage());
            }
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
        }
        return "calculation";
    }
}
