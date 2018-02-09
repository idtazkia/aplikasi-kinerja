package id.ac.tazkia.kinerja.aplikasikinerja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(
		basePackageClasses = {AplikasiKinerjaApplication.class, Jsr310JpaConverters.class}
		)
public class AplikasiKinerjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplikasiKinerjaApplication.class, args);
	}
}
