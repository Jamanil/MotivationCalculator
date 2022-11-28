package com.arttek.MotivationCalculator.models;

import com.arttek.MotivationCalculator.dao.ConstantsDao;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;

/**
 * @author Victor Datsenko
 * 23.11.2022
 * Класс, хранящий все необходимые поля для расчетов
 */
@Getter
@Setter
public class MotivationCalculator {
    public static final Logger logger = LoggerFactory.getLogger(MotivationCalculator.class);
    private ConstantsDao constantsDAO;
    private float revenue;
    private float profitable;
    private float revenuePlan;
    private float fuel;
    private float fuelDiscount;
    private float duty;
    private float carrierNdsReimbursable;
    private float salary;
    private byte workingDays;
    private float paymentPerDay;
    private float netMargin;
    private float motivation;
    private float salaryOnWorkingDays;
    private float total;
    private SortedMap<Float, Float> bonusRateGrades;
    private SortedMap<Float, Float> planCompletionGrades;
}
