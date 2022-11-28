package com.arttek.MotivationCalculator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Victor Datsenko
 * 24.11.2022
 * Класс, хранящий пороги, коэффициенты и константы
 */
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Constants implements Serializable {
    private SortedMap<Float, Float> bonusRateGrades = new TreeMap<>();
    private SortedMap<Float, Float> planCompletionGrades = new TreeMap<>();
    private byte workingDaysInMonth;
    private float netMarginProfitableSubtracted;
    private float netMarginFuelCoefficient;

    @Override
    public String toString() {
        return "Constants{" +
                "bonusRateGrades=" + Arrays.toString(bonusRateGrades.entrySet().toArray()) +
                ", planCompletionGrades=" + Arrays.toString(planCompletionGrades.entrySet().toArray()) +
                ", workingDaysInMonth=" + workingDaysInMonth +
                ", netMarginProfitableSubtracted=" + netMarginProfitableSubtracted +
                ", netMarginFuelCoefficient=" + netMarginFuelCoefficient +
                '}';
    }
}
