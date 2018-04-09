package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class InputKpiDto {

    @NotNull
    private String id;

    @NotNull
    private String keyResult;

    @NotNull
    private Category category;

    @NotNull @Min(0)
    private BigDecimal baseLine;

    @NotNull @Min(0)
    private BigDecimal weight;

    @NotNull @Min(0)
    private BigDecimal target;

    @NotNull @Min(0)
    private BigInteger score5;

    @NotNull @Min(0)
    private BigInteger score4;

    @NotNull @Min(0)
    private BigInteger score3;

    @NotNull @Min(0)
    private BigInteger score2;

    @NotNull @Min(0)
    private BigInteger score1;

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
