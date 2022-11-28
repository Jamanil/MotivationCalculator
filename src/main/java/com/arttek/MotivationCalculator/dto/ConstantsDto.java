package com.arttek.MotivationCalculator.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Victor Datsenko
 * 24.11.2022
 * DTO класс с валидацией введенных значений.
 * Вложенный класс ConstantsMapEntry используется для передачи старого ключа, нового ключа и значения в методы контроллера, изменяющие записи в мапах
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConstantsDto {
    private SortedMap<Float, Float> bonusRateGrades = new TreeMap<>();
    private SortedMap<Float, Float> planCompletionGrades  = new TreeMap<>();

    @Min(value = 0, message = "Значение не может быть отрицательным")
    @Max(value = 31, message = "В месяце не может быть более 31 дня")
    @NotNull(message = "Значение не может быть пустым")
    private Byte workingDaysInMonth;

    @Digits(integer = 4, fraction = 4, message = "Введите только числовое значение, до 4 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float netMarginProfitableSubtracted;

    @Digits(integer = 4, fraction = 4, message = "Введите только числовое значение, до 4 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float netMarginFuelCoefficient;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConstantsMapEntry {
        private Float oldKey;

        @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
        @Min(value = 0, message = "Значение не может быть отрицательным")
        @NotNull(message = "Значение не может быть пустым")
        private Float key;

        @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
        @Min(value = 0, message = "Значение не может быть отрицательным")
        @NotNull(message = "Значение не может быть пустым")
        private Float value;
    }
}
