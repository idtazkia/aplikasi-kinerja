package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.PeriodeDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private PeriodeDao periodeDao;

    @GetMapping("/index")
    public void indexPage(Model model){
        model.addAttribute("periode",periodeDao.findByStatusOrderByActiveAsc(StatusKpi.AKTIF));
    }

    @GetMapping("/")
    public String formAwal(){
        return "redirect:/login";
    }

}
