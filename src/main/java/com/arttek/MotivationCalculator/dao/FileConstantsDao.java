package com.arttek.MotivationCalculator.dao;

import com.arttek.MotivationCalculator.models.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.TreeMap;

/**
 * @author Victor Datsenko
 * 24.11.2022
 * Реализация интерфейса, обеспечивающего доступа к константам. В данном случае константы сериализуются в файл.
 * Может быть создана реализация, в которой константы хранятся в БД или каким-то другим способом.
 */
@Component
public class FileConstantsDao implements ConstantsDao {
    private static final Logger logger = LoggerFactory.getLogger(FileConstantsDao.class);

    @Value("${storageFile.path}")
    private String STORAGE_FILE_PATH;
    @Value("#{${defaultBonusRateGrades}}")
    private TreeMap<Float, Float> defaultBonusRateGrades;
    @Value("#{${defaultPlanCompletionGrades}}")
    private TreeMap<Float, Float> defaultPlanCompletionGrades;
    @Value("${defaultWorkingDaysInMonth}")
    private byte defaultWorkingDaysInMonth;
    @Value("${defaultNetMarginProfitableSubtracted}")
    private float defaultNetMarginProfitableSubtracted;
    @Value("${defaultNetMarginFuelCoefficient}")
    private float defaultNetMarginFuelCoefficient;

    @Override
    public Constants readConstants() {
        logger.debug("Чтение констант из файла");
        try (
                var fis = new FileInputStream(STORAGE_FILE_PATH);
                var ois = new ObjectInputStream(fis)
        ){
            Constants constants = (Constants) ois.readObject();
            logger.debug("Константы успешно считаны");
            return constants;
        } catch (Exception e) {
            logger.error("Ошибка чтения констант, {}", e.getLocalizedMessage());
            logger.error("Создаем новый объект с константами, значения по умолчанию из application.properties");

            return new Constants(defaultBonusRateGrades,
                    defaultPlanCompletionGrades,
                    defaultWorkingDaysInMonth,
                    defaultNetMarginProfitableSubtracted,
                    defaultNetMarginFuelCoefficient);
        }
    }

    @Override
    public void saveConstants(Constants constants) {
        logger.debug("Сохранение констант в файл");
        try (
                var fos = new FileOutputStream(STORAGE_FILE_PATH);
                var oos = new ObjectOutputStream(fos)
        ){
            oos.writeObject(constants);
            logger.debug("Константы записаны в файл");
        } catch (Exception e) {
            logger.error("Не удалось сохранить файл с константами, {}", e.getLocalizedMessage());
        }
    }
}
