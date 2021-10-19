package ru.geekbrains.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvBrand {

    @CsvBindByName(column = "brand_id", required = true)
    private Long id;

    @CsvBindByName(column = "brand_title")
    private String title;
}
