package com.arttek.MotivationCalculator.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Victor Datsenko
 * 23.11.2022
 * DTO класс с валидацией введенных данных
 */
@Getter
@Setter
public class MotivationCalculatorDto {
    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float revenue;

    @Digits(integer = 4, fraction = 4, message = "Введите только числовое значение, до 4 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float profitable;

    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float revenuePlan;

    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float fuel;

    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float fuelDiscount;

    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float duty;

    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float carrierNdsReimbursable;

    @Digits(integer = 12, fraction = 4, message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @Min(value = 0, message = "Значение не может быть отрицательным")
    @NotNull(message = "Значение не может быть пустым")
    private Float salary;

    @Min(value = 0, message = "Значение не может быть отрицательным")
    @Max(value = 31, message = "В месяце не может быть более 31 дня")
    @NotNull(message = "Значение не может быть пустым")
    private Byte workingDays;

    private float paymentPerDay;
    private float netMargin;
    private float motivation;
    private float salaryOnWorkingDays;
    private float total;
}
