package com.arttek.MotivationCalculator.services;

import com.arttek.MotivationCalculator.dao.ConstantsDao;
import com.arttek.MotivationCalculator.dto.ConstantsDto;
import com.arttek.MotivationCalculator.models.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.SortedMap;

/**
 * @author Victor Datsenko
 * 25.11.2022
 * В классе реализованы все методы, необходимые для CRUD констант
 * Согласно условиям задания, пороги, коэффициенты (и, наверняка, константы) должны быть настраиваемыми
 * Настройка порогов и коэффициентов подразумевает под собой, что этот функционал будет вынесен в административную панель, недоступную пользователю.
 * На текущем этапе реализация разграничения ролей пользователей не требуется
 * В "боевом" проекте я бы подключил Spring Security, доступ к "/constants" был бы только после авторизации
 * Методы класса ConstantServices были бы с аннотацией @PreAuthorize
 */
@Component
@RequiredArgsConstructor
public class ConstantsServices {
    private final ConstantsDao constantsDAO;
    private final Logger logger = LoggerFactory.getLogger(ConstantsServices.class);

    public Constants getConstants() {
        Constants constants = constantsDAO.readConstants();
        logger.debug("Считан класс констант со значениями {}", constants);
        return constants;
    }

    private void saveConstants(Constants constants) {
        constantsDAO.saveConstants(constants);
        logger.debug("Сохранены констаны со значениями {}", constants);
    }

    public float getBonusEntryValue(float key) {
        SortedMap<Float, Float> bonusRateGrades = getConstants().getBonusRateGrades();
        float value;
        if (bonusRateGrades.containsKey(key)) {
            value = bonusRateGrades.get(key);
            logger.debug("Для ключа {} считано значение {}", key, value);
            return value;
        } else {
            logger.error("В мапе bonusRateGrades не найдено ключа {}", key);
            throw new NoSuchElementException("Не найден ключ " + key);
        }
    }

    public float getPlanCompletionEntryValue(float key) {
        SortedMap<Float, Float> planCompletionGrades = getConstants().getPlanCompletionGrades();
        float value;
        if (planCompletionGrades.containsKey(key)) {
            value = planCompletionGrades.get(key);
            logger.debug("Для ключа {} считано значение {}", key, value);
            return value;
        } else {
            logger.error("В мапе planCompletionGrades не найдено ключа {}", key);
            throw new NoSuchElementException("Не найден ключ " + key);
        }
    }

    public void updateOtherConstants(Constants updatedConstants) {
        byte workingDaysInMonth = updatedConstants.getWorkingDaysInMonth();
        float netMarginProfitableSubtracted = updatedConstants.getNetMarginProfitableSubtracted();
        float netMarginFuelCoefficient = updatedConstants.getNetMarginFuelCoefficient();
        Constants constants = getConstants();
        constants.setWorkingDaysInMonth(workingDaysInMonth);
        constants.setNetMarginProfitableSubtracted(netMarginProfitableSubtracted);
        constants.setNetMarginFuelCoefficient(netMarginFuelCoefficient);
        saveConstants(constants);
        logger.debug("Константы обновлены, рабочих дней {}, вычитаемое из прибыльности {}, коэффициет для топлива {}",
                workingDaysInMonth, netMarginProfitableSubtracted, netMarginFuelCoefficient);
    }

    public void addBonusEntry(ConstantsDto.ConstantsMapEntry entry) {
        Constants constants = getConstants();
        SortedMap<Float, Float> updatedBonusRateGrades = addEntryToMap(constants.getBonusRateGrades(), entry);
        constants.setBonusRateGrades(updatedBonusRateGrades);
        saveConstants(constants);
    }

    public void updateBonusEntry(ConstantsDto.ConstantsMapEntry entry) {
        Constants constants = getConstants();
        SortedMap<Float, Float> updatedBonusRateGrades = updateEntryInMap(constants.getBonusRateGrades(), entry);
        constants.setBonusRateGrades(updatedBonusRateGrades);
        saveConstants(constants);
    }

    public void deleteBonusEntry(ConstantsDto.ConstantsMapEntry entry) {
        Constants constants = getConstants();
        SortedMap<Float, Float> updatedBonusRateGrades = deleteEntryFromMap(constants.getBonusRateGrades(), entry);
        constants.setBonusRateGrades(updatedBonusRateGrades);
        saveConstants(constants);
    }

    public void addPlanCompletionEntry(ConstantsDto.ConstantsMapEntry entry) {
        Constants constants = getConstants();
        SortedMap<Float, Float> updatedPlanCompletionGrades = addEntryToMap(constants.getPlanCompletionGrades(), entry);
        constants.setPlanCompletionGrades(updatedPlanCompletionGrades);
        saveConstants(constants);
    }

    public void updatePlaneCompletionEntry(ConstantsDto.ConstantsMapEntry entry) {
        Constants constants = getConstants();
        SortedMap<Float, Float> updatedPlanCompletionGrades = updateEntryInMap(constants.getPlanCompletionGrades(), entry);
        constants.setPlanCompletionGrades(updatedPlanCompletionGrades);
        saveConstants(constants);
    }

    public void deletePlanCompletionEntry(ConstantsDto.ConstantsMapEntry entry) {
        Constants constants = getConstants();
        SortedMap<Float, Float> updatedPlanCompletionGrades = deleteEntryFromMap(constants.getPlanCompletionGrades(), entry);
        constants.setPlanCompletionGrades(updatedPlanCompletionGrades);
        saveConstants(constants);
    }

    private SortedMap<Float, Float> addEntryToMap(SortedMap<Float, Float> map, ConstantsDto.ConstantsMapEntry entry) {
        float key = entry.getKey();
        float value = entry.getValue();
        map.put(key, value);
        logger.debug("В мапу добавлена пара {}={}", key, value);
        return map;
    }

    private SortedMap<Float, Float> updateEntryInMap(SortedMap<Float, Float> map, ConstantsDto.ConstantsMapEntry entry) {
        float oldKey = entry.getOldKey();
        float key = entry.getKey();
        float value = entry.getValue();
        if (map.containsKey(oldKey)) {
            float oldValue = map.get(oldKey);
            map.remove(entry.getOldKey());
            map.put(entry.getKey(), entry.getValue());
            logger.debug("Пара {}={} заменена на {}={}", oldKey, oldValue, key, value);
        } else {
            logger.error("В мапе не найдено ключа {}", oldKey);
            throw new NoSuchElementException("Не найден ключ " + oldKey);
        }
        return map;
    }

    private SortedMap<Float, Float> deleteEntryFromMap(SortedMap<Float, Float> map, ConstantsDto.ConstantsMapEntry entry) {
        float oldKey = entry.getOldKey();
        if (map.containsKey(oldKey)) {
            float oldValue = map.get(oldKey);
            map.remove(entry.getOldKey());
            logger.debug("Пара {}={} удалена", oldKey, oldValue);
        } else {
            logger.error("В мапе не найдено ключа {}", oldKey);
            throw new NoSuchElementException("Не найден ключ " + oldKey);
        }
        return map;
    }
}
