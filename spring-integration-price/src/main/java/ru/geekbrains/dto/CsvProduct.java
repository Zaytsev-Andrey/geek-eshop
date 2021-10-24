package ru.geekbrains.dto;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CsvProduct {

    @CsvBindByName(column = "id")
    private Long id;

    @CsvBindByName(column = "title", required = true)
    private String title;

    @CsvBindByName(column = "cost", required = true)
    private BigDecimal cost;

    @CsvBindByName(column = "description")
    private String description;

    @CsvRecurse
    private CsvCategory csvCategory;

    @CsvRecurse
    private CsvBrand csvBrand;

    @CsvBindAndSplitByName(
            column = "pictures",
            elementType = String.class,
            splitOn = "\\|",
            collectionType = ArrayList.class)
    private List<String> pictureNames;

}
