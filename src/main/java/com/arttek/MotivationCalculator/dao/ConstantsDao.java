package com.arttek.MotivationCalculator.dao;

import com.arttek.MotivationCalculator.models.Constants;

/**
 * @author Victor Datsenko
 * 24.11.2022
 * Так как пороги и коэффициенты настраиваемые, будем их где-то хранить.
 * Можно было бы просто заполнять значениями из application.properties, но тогда при каждом перезапуске нужно будет корректировать константы заново
 * Поэтому принято решение организовать какое-либо внешние хранилище для значений порогов, коэффициентов и констант.
 * Интерфейс, обеспечивающий доступ к классу с константами
 */
public interface ConstantsDao {
    Constants readConstants();
    void saveConstants(Constants constants);
}
