package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BawahanController {

    @GetMapping("/daftarbawahan/list")
    public void list(){}
}
