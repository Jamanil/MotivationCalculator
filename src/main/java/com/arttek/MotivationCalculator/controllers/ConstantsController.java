package com.arttek.MotivationCalculator.controllers;

import com.arttek.MotivationCalculator.dto.ConstantsDto;
import com.arttek.MotivationCalculator.models.Constants;
import com.arttek.MotivationCalculator.services.ConstantsServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Victor Datsenko
 * 25.11.2022
 * Контроллер, отвечающий за работу с порогами, коэффициентами и константами.
 * Согласно условиям задания, пороги, коэффициенты (и, наверняка, константы) должны быть настраиваемыми
 * Настройка порогов и коэффициентов подразумевает под собой, что этот функционал будет вынесен в административную панель, недоступную пользователю.
 * На текущем этапе реализация разграничения ролей пользователей не требуется
 * В "боевом" проекте я бы подключил Spring Security, доступ к "/constants" был бы только после авторизации
 * Методы класса ConstantServices были бы с аннотацией @PreAuthorize
 */
@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/constants")
@RequiredArgsConstructor
public class ConstantsController {
    private static final Logger logger = LoggerFactory.getLogger(ConstantsController.class);
    private final ConstantsServices constantsServices;
    private final ModelMapper modelMapper;

    @GetMapping
    private String showCoefficientPage(Model model) {
        logger.debug("Вывод страницы редактирования коэффициентов");
        Constants constants = constantsServices.getConstants();
        model.addAttribute("constants", modelMapper.map(constants, ConstantsDto.class));
        return "admin/constants";
    }

    @PostMapping
    private String saveConstants(@ModelAttribute("constantsDto") @Valid ConstantsDto constantsDto,
                                 BindingResult bindingResult,
                                 Model model) {
        logger.debug("Сохранение переданных пользователем значений констант");
        if (!bindingResult.hasErrors()) {
            logger.debug("Поля заполнены корректно, обновляются значения констант");
            constantsServices.updateOtherConstants(modelMapper.map(constantsDto, Constants.class));
            return "redirect:/constants";
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
            return "admin/constants";
        }
    }

    @GetMapping("/editBonusEntry/{key}")
    private String showEditBonusEntryPage(@PathVariable("key") float key, Model model) {
        logger.debug("Отрисовка страницы редактирования бонуса");
        ConstantsDto.ConstantsMapEntry entry =
                new ConstantsDto.ConstantsMapEntry(key, key, constantsServices.getBonusEntryValue(key));
        model.addAttribute("entry", entry);
        return "admin/editBonusEntry";
    }

    @PostMapping("/editBonusEntry")
    private String editBonusEntry(@ModelAttribute("entry") @Valid ConstantsDto.ConstantsMapEntry entry,
                                  BindingResult bindingResult) {
        logger.debug("Обновление записи в таблице коэффициентов премирования");
        if (!bindingResult.hasErrors()) {
            logger.debug("Поля заполнены корректно");
            constantsServices.updateBonusEntry(entry);
            return "redirect:/constants";
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
            return "admin/editBonusEntry";
        }
    }

    @DeleteMapping("/editBonusEntry")
    private String deleteBonusEntry(@ModelAttribute("entry") ConstantsDto.ConstantsMapEntry entry) {
        logger.debug("Удаление записи из таблицы коэффициентов премирования");
        constantsServices.deleteBonusEntry(entry);
        return "redirect:/constants";
    }

    @GetMapping("/addBonusEntry")
    private String showAddBonusEntryPage(Model model) {
        logger.debug("Отрисовка страницы добавления пары 'порог=коэффициент премирования'");
        model.addAttribute("entry", new ConstantsDto.ConstantsMapEntry());
        return "admin/addBonusEntry";
    }

    @PutMapping("/editBonusEntry")
    private String showAddBonusEntryPage(@ModelAttribute("entry") @Valid ConstantsDto.ConstantsMapEntry entry,
                                 BindingResult bindingResult) {
        logger.debug("Добавление записи в таблицу коэффициентов премирования");
        if (!bindingResult.hasErrors()) {
            logger.debug("Поля заполнены корректно");
            constantsServices.addBonusEntry(entry);
            return "redirect:/constants";
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
            return "admin/addBonusEntry";
        }
    }

    @GetMapping("/editPlanCompletionEntry/{key}")
    private String editPlanCompletionEntryPage(@PathVariable("key") float key, Model model) {
        logger.debug("Отрисовка страницы редактирования коэффициента выполнения плана");
        ConstantsDto.ConstantsMapEntry entry =
                new ConstantsDto.ConstantsMapEntry(key, key, constantsServices.getPlanCompletionEntryValue(key));
        model.addAttribute("entry", entry);
        return "admin/editPlanCompletionEntry";
    }

    @PostMapping("/editPlanCompletionEntry")
    private String editPlanCompletionEntryPage(@ModelAttribute("entry") @Valid ConstantsDto.ConstantsMapEntry entry,
                                 BindingResult bindingResult) {
        logger.debug("Обновление записи в таблице коэффициентов выполнения плана");
        if (!bindingResult.hasErrors()) {
            logger.debug("Поля заполнены корректно");
            constantsServices.updatePlaneCompletionEntry(entry);
            return "redirect:/constants";
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
            return "admin/editPlanCompletionEntry";
        }
    }

    @DeleteMapping("/editPlanCompletionEntry")
    private String deletePlanCompletionEntry(@ModelAttribute("entry") @Valid ConstantsDto.ConstantsMapEntry entry,
                                   BindingResult bindingResult) {
        logger.debug("Удаление записи из таблицы коэффициентов выполнения плана");
        if (!bindingResult.hasErrors()) {
            logger.debug("Поля заполнены корректно");
            constantsServices.deletePlanCompletionEntry(entry);
            return "redirect:/constants";
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
            return "admin/editPlanCompletionEntry";
        }
    }

    @GetMapping("/addPlanCompletionEntry")
    private String showAddPlanCompletionEntryPage(Model model) {
        logger.debug("Отрисовка страницы добавления пары 'порог=коэффициент выполнения плана'");
        model.addAttribute("entry", new ConstantsDto.ConstantsMapEntry());
        return "admin/addPlanCompletionEntry";
    }

    @PutMapping("/editPlanCompletionEntry")
    private String addPlanCompletionEntry(@ModelAttribute("entry") @Valid ConstantsDto.ConstantsMapEntry entry,
                                BindingResult bindingResult) {
        logger.debug("Добавление записи в таблицу коэффициентов выполнения плана");
        if (!bindingResult.hasErrors()) {
            logger.debug("Поля заполнены корректно");
            constantsServices.addPlanCompletionEntry(entry);
            return "redirect:/constants";
        } else {
            logger.debug("Ошибка заполнения полей, ожидание корректировки ввода");
            logger.warn(bindingResult.toString());
            return "admin/addPlanCompletionEntry";
        }
    }
}
