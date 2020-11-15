package com.manager.fuel.services;

import com.manager.fuel.entities.FuelHistory;
import com.manager.fuel.repositories.FuelHistoryRepository;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

@Service
public class FuelHistoryService {

    @Autowired
    private FuelHistoryRepository fuelHistoryRepository;

    public void importCsv(InputStream imInputStream) throws IOException {

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(imInputStream, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);

        for (CSVRecord record: csvParser.getRecords()) {

            String[] fuel = record.get(0).split(";");

            if (fuel[7].isEmpty()) {
                fuel[7] = "0";
            }

            if (fuel[8].isEmpty()) {
                fuel[8] = "0";
            }

            FuelHistory fuelHistory = new FuelHistory(fuel[0], fuel[1], fuel[2], fuel[3], fuel[4], fuel[5], new Date(fuel[6]),
                    new BigDecimal(fuel[7]), new BigDecimal(fuel[8]), fuel[9], fuel[10]);

            fuelHistoryRepository.save(fuelHistory);
        }
    }

    public FuelHistory update(FuelHistory lFuelHistory, FuelHistory newFuelHistory) {
        if (newFuelHistory.getRegion() != null) {
            lFuelHistory.setRegion(newFuelHistory.getRegion());
        }

        if (newFuelHistory.getState() != null) {
            lFuelHistory.setState(newFuelHistory.getState());
        }

        if (newFuelHistory.getCity() != null) {
            lFuelHistory.setCity(newFuelHistory.getCity());
        }

        if (newFuelHistory.getResale() != null) {
            lFuelHistory.setResale(newFuelHistory.getResale());
        }

        if (newFuelHistory.getCodInstallation() != null) {
            lFuelHistory.setCodInstallation(newFuelHistory.getCodInstallation());
        }

        if (newFuelHistory.getProduct() != null) {
            lFuelHistory.setProduct(newFuelHistory.getProduct());
        }

        if (newFuelHistory.getDateCollect() != null) {
            lFuelHistory.setDateCollect(newFuelHistory.getDateCollect());
        }

        if (newFuelHistory.getValuePurchase() != null) {
            lFuelHistory.setValuePurchase(newFuelHistory.getValuePurchase());
        }

        if (newFuelHistory.getValueSale() != null) {
            lFuelHistory.setValueSale(newFuelHistory.getValueSale());
        }

        if (newFuelHistory.getUnit() != null) {
            lFuelHistory.setUnit(newFuelHistory.getUnit());
        }

        if (newFuelHistory.getBrand() != null) {
            lFuelHistory.setBrand(newFuelHistory.getBrand());
        }

        return fuelHistoryRepository.save(lFuelHistory);
    }

    public BigDecimal findAverageFuelPriceByCity(String city) {
        BigDecimal average = new BigDecimal(0);

        List<FuelHistory> infosByCity = fuelHistoryRepository.findByCity(city);

        Stream<FuelHistory> stream = infosByCity.stream();
        DoubleStream doubleStream = stream.mapToDouble(fh -> fh.getValuePurchase().doubleValue());
        OptionalDouble optionalDouble = doubleStream.average();
        double media = optionalDouble.orElse(0.0);

        return new BigDecimal(media);
    }

    public Map<BigDecimal, BigDecimal> findAverageFuelValueByCity(String city) {
        BigDecimal mediaPurchase, mediaSale;
        List<FuelHistory> infosByCity = fuelHistoryRepository.findByCity(city);
        Map<BigDecimal, BigDecimal> result = null;

        Stream<FuelHistory> stream = infosByCity.stream();
        DoubleStream doubleStream = stream.mapToDouble(fh -> fh.getValuePurchase().doubleValue());
        OptionalDouble optionalDouble = doubleStream.average();
        mediaPurchase = new BigDecimal(optionalDouble.orElse(0.0));

        doubleStream = stream.mapToDouble(fh -> fh.getValueSale().doubleValue());
        optionalDouble = doubleStream.average();
        mediaSale = new BigDecimal(optionalDouble.orElse(0.0));

        result.put(mediaPurchase, mediaSale);

        return result;
    }

    public Map<BigDecimal, BigDecimal> findAverageFuelValueByBrand(String brand) {
        BigDecimal mediaPurchase, mediaSale;
        List<FuelHistory> infosByCity = fuelHistoryRepository.findByBrand(brand);
        Map<BigDecimal, BigDecimal> result = null;

        Stream<FuelHistory> stream = infosByCity.stream();
        DoubleStream doubleStream = stream.mapToDouble(fh -> fh.getValuePurchase().doubleValue());
        OptionalDouble optionalDouble = doubleStream.average();
        mediaPurchase = new BigDecimal(optionalDouble.orElse(0.0));

        doubleStream = stream.mapToDouble(fh -> fh.getValueSale().doubleValue());
        optionalDouble = doubleStream.average();
        mediaSale = new BigDecimal(optionalDouble.orElse(0.0));

        result.put(mediaPurchase, mediaSale);

        return result;
    }

    public List<FuelHistory> fuelHistoryListByRegion(String region) {
        return fuelHistoryRepository.findByRegion(region);
    }

    public Iterable<FuelHistory> fuelHistoryListOrderByResale() {
        return fuelHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "resale"));
    }

    public Iterable<FuelHistory> fuelHistoryListOrderByDateCollect() {
        return fuelHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "dateCollect"));
    }
}
