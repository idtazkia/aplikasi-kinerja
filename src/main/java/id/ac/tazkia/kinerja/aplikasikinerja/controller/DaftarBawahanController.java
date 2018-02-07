package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DaftarBawahanController {

    @GetMapping("/daftarbawahan/list")
    public void list(){}

    @GetMapping("/daftarbawahan/detail")
    public void detail(){}

    @GetMapping("/daftarbawahan/komen")
    public void komens(){}

}
