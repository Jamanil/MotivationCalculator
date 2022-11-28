package com.arttek.MotivationCalculator.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * @author Victor Datsenko
 * 23.11.2022
 * DTO класс с валидацией введенных данных
 */
@Getter
@Setter
public class MotivationCalculatorDto {
    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String revenue;

    @Pattern(regexp = "\\d{1,4}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 4 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String profitable;

    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String revenuePlan;

    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String fuel;

    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String fuelDiscount;

    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String duty;

    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String carrierNdsReimbursable;

    @Pattern(regexp = "\\d{1,12}([.,]\\d{1,4})?", message = "Введите только числовое значение, до 12 знаков в целой части, до 4 после точки")
    @NotNull(message = "Значение не может быть пустым")
    private String salary;

    @Min(value = 0, message = "Значение не может быть отрицательным")
    @Max(value = 31, message = "В месяце не может быть более 31 дня")
    @NotNull(message = "Значение не может быть пустым")
    private Byte workingDays;

    private String paymentPerDay;
    private String netMargin;
    private String motivation;
    private String salaryOnWorkingDays;
    private String total;
}
