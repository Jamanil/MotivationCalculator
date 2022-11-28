package com.arttek.MotivationCalculator.services;

import com.arttek.MotivationCalculator.dao.ConstantsDao;
import com.arttek.MotivationCalculator.models.Constants;
import com.arttek.MotivationCalculator.models.MotivationCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.SortedMap;

/**
 * @author Victor Datsenko
 * 28.11.2022
 * В сервис вынесены все методы расчета
 */
@Component
@RequiredArgsConstructor
public class MotivationCalculatorServices {
    private static final Logger logger = LoggerFactory.getLogger(MotivationCalculatorServices.class);
    private final ConstantsDao constantsDao;
    private byte workingDaysInMonth;
    private float netMarginProfitableSubtracted;
    private float netMarginFuelCoefficient;
    private SortedMap<Float, Float> bonusRateGrades;
    private SortedMap<Float, Float> planCompletionGrades;

    // Заполнение полей констант
    private void fillConstants() {
        Constants constants = constantsDao.readConstants();
        workingDaysInMonth = constants.getWorkingDaysInMonth();
        netMarginProfitableSubtracted = constants.getNetMarginProfitableSubtracted();
        netMarginFuelCoefficient = constants.getNetMarginFuelCoefficient();
        bonusRateGrades = constants.getBonusRateGrades();
        planCompletionGrades = constants.getPlanCompletionGrades();

        if (bonusRateGrades.isEmpty())
        {
            String msg = "Не заполнены коэффициенты премирования";
            logger.error(msg);
            throw new NullPointerException(msg);
        }
        if (planCompletionGrades.isEmpty()) {
            String msg = "Не заполнены коэффициенты выполнения плана";
            logger.error(msg);
            throw new NullPointerException(msg);
        }
        if (workingDaysInMonth == 0) {
            String msg = "Количество рабочих дней в месяце не может быть равным 0";
            logger.error(msg);
            throw new NullPointerException(msg);
        }
        logger.debug("Константы заполнены");
    }

    // Заполнение вычисляемых полей
    public void fillCalculatedFields(MotivationCalculator motivationCalculator) {
        fillConstants();
        motivationCalculator.setPaymentPerDay(calculatePaymentPerDay(motivationCalculator));
        motivationCalculator.setNetMargin(calculateNetMargin(motivationCalculator));
        motivationCalculator.setMotivation(calculateMotivation(motivationCalculator));
        motivationCalculator.setSalaryOnWorkingDays(calculateSalaryOnWorkingDays(motivationCalculator));
        motivationCalculator.setTotal(calculateTotal(motivationCalculator));
        logger.debug("Расчеты значений вычисляемых полей проведены");
    }

    // Ставка (Вычисляемое значение=Оклад/22)
    private float calculatePaymentPerDay(MotivationCalculator motivationCalculator) {
        float salary = motivationCalculator.getSalary();
        float paymentPerDay = salary / workingDaysInMonth;
        logger.debug("Расчет дневной ставки сотрудника: Оклад {} / Рабочих дней в месяце {} = Ставка {}",
                paymentPerDay, salary, workingDaysInMonth);
        return paymentPerDay;
    }

    // Оклад по рабочим дням (Ставка*Рабочих дней)
    private float calculateSalaryOnWorkingDays(MotivationCalculator motivationCalculator) {
        float workingDays = motivationCalculator.getWorkingDays();
        float paymentPerDay = motivationCalculator.getPaymentPerDay();
        float salaryOnWorkingDays = paymentPerDay * workingDays;
        logger.debug("Расчет оклада по количеству отработанных дней: Ставка {} * Отработано дней {} = Оклад по рабочим дням {}",
                paymentPerDay, workingDays, salaryOnWorkingDays);
        return salaryOnWorkingDays;
    }

    // Чистая Маржа=Выручка *(Рентабельность -16,67%)+ГСМ Сумма *16,67%-Скидка на ГСМ+НДС Перевозчика к Возмещению
    private float calculateNetMargin(MotivationCalculator motivationCalculator) {
        float revenue = motivationCalculator.getRevenue();
        float profitable = motivationCalculator.getProfitable();
        float fuel = motivationCalculator.getFuel();
        float fuelDiscount = motivationCalculator.getFuelDiscount();
        float carrierNdsReimbursable = motivationCalculator.getCarrierNdsReimbursable();
        float netMargin = revenue * (profitable - netMarginProfitableSubtracted)
                + fuel * netMarginFuelCoefficient - fuelDiscount
                + carrierNdsReimbursable;
        logger.debug("Расчет чистой маржи : Выручка {} * (Рентабельность {} - Вычетаемое из рентабельности {}) + ГСМ {} * Коэффициент ГСМ {} - Скидка на ГСМ {} + НДС перевозчика к возмещению {} = {}",
                revenue, profitable, netMarginProfitableSubtracted, fuel, netMarginFuelCoefficient, fuelDiscount, carrierNdsReimbursable, netMargin);
        return netMargin;
    }

    // Получение коэффициента премирования
    private float getBonusRate(MotivationCalculator motivationCalculator) {
        float netMargin = motivationCalculator.getNetMargin();
        Map.Entry<Float, Float> bonusRateEntry = bonusRateGrades.entrySet().stream()
                .takeWhile(a -> a.getKey() < netMargin).max((a, b) -> (int) (a.getKey() - b.getKey()))
                .orElseGet(() ->bonusRateGrades.entrySet().stream().findFirst().get());

        logger.debug("Чистой марже {} соответствует коэффициент премирования {}", netMargin, bonusRateEntry.getValue());
        return bonusRateEntry.getValue();

        /* Альтернативный вариант получения коэффициента премирования, без лямбд, но чуть более оптимален в плане производительности
        Float bonusRate = null;
        // Т.к. мапа сортированная, пробегаемся по ключам, если ключ меньше чистой маржи, присваиваем переменной значение коэффициента премирования
        for (var entry : bonusRateGrades.entrySet()) {
            if (entry.getKey() < netMargin)
                bonusRate = entry.getValue();
            else
                break;
        }
        // Если переменная осталась пустой, возвращаем минимальный коэффициент премирования.
        bonusRate =  Optional.ofNullable(bonusRate).orElseGet(() -> bonusRateGrades.get(bonusRateGrades.firstKey()));*/
    }

    private float getPlanCompletionRate(MotivationCalculator motivationCalculator) {
        float revenue = motivationCalculator.getRevenue();
        float revenuePlan = motivationCalculator.getRevenuePlan();
        float planCompletion = revenue / revenuePlan;
        logger.debug("Выручка {} / План {} = Процент выполнения плана {}", revenue, revenuePlan, planCompletion);

        Map.Entry<Float, Float> planCompletionEntry = planCompletionGrades.entrySet().stream()
                .takeWhile(a -> a.getKey() < planCompletion).max((a, b) -> (int) (a.getKey() - b.getKey()))
                .orElseGet(() ->planCompletionGrades.entrySet().stream().findFirst().get());

        logger.debug("Проценту выполнения плана {} соответсвует коэффициент выполнения плана {}", planCompletion, planCompletionEntry.getValue());
        return planCompletionEntry.getValue();
    }

    // Мотивация= Чистая маржа*Коэффициент премирования*Коэффициент выполнения плана
    private float calculateMotivation(MotivationCalculator motivationCalculator) {
        float netMargin = motivationCalculator.getNetMargin();
        float bonusRate = getBonusRate(motivationCalculator);
        float planCompletionRate = getPlanCompletionRate(motivationCalculator);
        float result = netMargin * bonusRate * planCompletionRate;
        logger.debug("Расчет мотивационной части: Чистая маржа {} * Коэффициент премирования {} * Коэффициент выполнения плана {} = Мотивация {}",
                netMargin, bonusRate, planCompletionRate, result);
        return result;
    }

    // Проверяем, превышает ли мотивационная часть ЗП оклад (рассчитанный от рабочих дней) или нет,
    // если мотивационная часть не превышает оклад, то сотрудник получает оклад, если мотивационная часть
    // превышает оклад, то сотрудник получает мотивационную часть
    // + сумма за дежурства
    private float calculateTotal(MotivationCalculator motivationCalculator) {
        float motivation = motivationCalculator.getMotivation();
        float salaryOnWorkingDays = motivationCalculator.getSalaryOnWorkingDays();
        float sumToPay = Math.max(motivation, salaryOnWorkingDays);
        float duty = motivationCalculator.getDuty();
        float total = sumToPay + duty;
        logger.debug("Размер мотивации {}, Оклад по дням {}, К оплате {} + Дежурства {}, Итого {}", motivation, salaryOnWorkingDays, sumToPay, duty, total);
        return total;
    }
}
