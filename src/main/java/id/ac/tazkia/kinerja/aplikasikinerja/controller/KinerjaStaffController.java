package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/kinerjastaf")
@Controller
public class KinerjaStaffController {

    @GetMapping("/form")
    public void listPageStaf(){

    }
}
