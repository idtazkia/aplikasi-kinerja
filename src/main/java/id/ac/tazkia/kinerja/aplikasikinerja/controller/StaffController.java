package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StaffController {

    @Autowired
    StaffDao staffDao;

    @Autowired
    KpiDao kpiDao;

    @ModelAttribute("daftarKpi")
    public Iterable<Kpi> daftarKpi() {
        return kpiDao.findAll();
    }

    @GetMapping("/staff/list")
    public void daftarStaff(@RequestParam(required = false) String staff, Model m, Pageable page) {
        m.addAttribute("daftarStaff", staffDao.findAll());

    }


    @GetMapping("/staff/form")
    public void formPageStaf() {

    }

    @GetMapping("staff/kpi")
    public void staffKpi() {
    }

}