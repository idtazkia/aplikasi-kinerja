package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class InputKpiDto {

    @NotNull
    private String id;

    @NotNull 
    private String keyResult;

    @NotNull
    private Category category;

    @NotNull
    private String baseLine;

    @NotNull
    private String weight;

    @NotNull
    private String target;

    @NotNull
    private String score5;

    @NotNull
    private String score4;

    @NotNull
    private String score3;

    @NotNull
    private String score2;

    @NotNull
    private String score1;

    @NotNull 
    private String indicator5;

    @NotNull 
    private String indicator4;

    @NotNull 
    private String indicator3;

    @NotNull 
    private String indicator2;

    @NotNull 
    private String indicator1;
}
