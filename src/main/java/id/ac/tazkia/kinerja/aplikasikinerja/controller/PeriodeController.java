package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PeriodeController {
    @GetMapping("/periode/list")
    public void list(){}
}
