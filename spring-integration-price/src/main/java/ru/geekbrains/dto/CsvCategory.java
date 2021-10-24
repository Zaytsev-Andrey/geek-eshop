package ru.geekbrains.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvCategory {

    @CsvBindByName(column = "category_id", required = true)
    private Long id;

    @CsvBindByName(column = "category_title")
    private String title;
}
