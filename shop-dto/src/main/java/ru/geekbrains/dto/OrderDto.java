package ru.geekbrains.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends AbstractPersistentDto {

    private String creationDate;

    private String price;

    private String status;

}
