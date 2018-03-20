package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DataResetPassword {
    private String code;
    private String email;
    private String nama;
}
