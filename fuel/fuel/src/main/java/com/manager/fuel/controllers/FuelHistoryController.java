package com.manager.fuel.controllers;

import com.manager.fuel.entities.FuelHistory;
import com.manager.fuel.entities.User;
import com.manager.fuel.repositories.FuelHistoryRepository;
import com.manager.fuel.services.FuelHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/history")
public class FuelHistoryController {

    @Autowired
    private FuelHistoryService fuelHistoryService;

    @Autowired
    private FuelHistoryRepository fuelHistoryRepository;

    @GetMapping
    public Iterable<FuelHistory> list() {
        return fuelHistoryRepository.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
            try {
                fuelHistoryService.importCsv(file.getInputStream());

                return ResponseEntity.status(HttpStatus.OK).body("Done!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail!");
            }
    }

    @RequestMapping(value = "/new", method =  RequestMethod.POST)
    public FuelHistory save(@RequestBody FuelHistory fuelHistory)
    {
        return fuelHistoryRepository.save(fuelHistory);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<FuelHistory> getById(@PathVariable(value = "id") long id)
    {
        Optional<FuelHistory> fuelHistory = fuelHistoryRepository.findById(id);

        if(fuelHistory.isPresent())
            return new ResponseEntity<FuelHistory>(fuelHistory.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<FuelHistory> update(@PathVariable(value = "id") long id, @RequestBody FuelHistory newFuelHistory)
    {
        Optional<FuelHistory> fuelHistory = fuelHistoryRepository.findById(id);

        if(fuelHistory != null){
            FuelHistory lFuelHistory = fuelHistoryService.update(fuelHistory.get(), newFuelHistory);
            return new ResponseEntity<FuelHistory>(lFuelHistory, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable(value = "id") long id)
    {
        Optional<FuelHistory> user = fuelHistoryRepository.findById(id);

        if(user.isPresent()){
            fuelHistoryRepository.delete(user.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/average/{city}")
    public ResponseEntity<String> getAverageByCity(@PathVariable(value = "city") String city) {
        BigDecimal average = fuelHistoryService.findAverageFuelPriceByCity(city);
        return ResponseEntity.status(HttpStatus.OK).body("Average= " + average);
    }

    @GetMapping("/averageValue/{city}")
    public ResponseEntity<String> getAveragePurchaseAndSaleByCity(@PathVariable(value = "city") String city) {
        Map<BigDecimal, BigDecimal> average = fuelHistoryService.findAverageFuelValueByCity(city);
        return ResponseEntity.status(HttpStatus.OK).body("Average= " + average);
    }

    @GetMapping("/averageByBrand/{brand}")
    public ResponseEntity<String> getAveragePurchaseAndSaleByBrand(@PathVariable(value = "brand") String brand) {
        Map<BigDecimal, BigDecimal> average = fuelHistoryService.findAverageFuelValueByBrand(brand);
        return ResponseEntity.status(HttpStatus.OK).body("Average= " + average);
    }

    @GetMapping("/getByRegion/{region}")
    public List<FuelHistory> getByRegion (@PathVariable(value = "region") String region) {
        return fuelHistoryService.fuelHistoryListByRegion(region);
    }

    @GetMapping("/getByResale")
    public Iterable<FuelHistory> getByResale () {
        return fuelHistoryService.fuelHistoryListOrderByResale();
    }

    @GetMapping("/getByDate")
    public Iterable<FuelHistory> getByDate() {
        return fuelHistoryService.fuelHistoryListOrderByDateCollect();
    }
}
