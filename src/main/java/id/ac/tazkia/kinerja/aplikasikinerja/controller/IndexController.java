package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.PeriodeDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Controller
public class IndexController {

    @Autowired
    private PeriodeDao periodeDao;

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    @Autowired
    KpiDao kpiDao;


    @GetMapping("/index")
    public void indexPage(Model model){

        Date date = new Date();
        String customFormat = "EEE, MMM dd yyyy";
        String timeZone = "zzz ";
        DateFormat dateFormat = new SimpleDateFormat(customFormat);
        DateFormat dfTZ = new SimpleDateFormat(timeZone);
        String waktuFormat= dateFormat.format(date);
        String zonaFormat= dfTZ.format(date);


        model.addAttribute("waktuSekarang",waktuFormat);
        model.addAttribute("zoneSekarang",zonaFormat);
        model.addAttribute("periode",periodeDao.findByStatusOrderByActiveAsc(StatusKpi.AKTIF));
        model.addAttribute("jmlStaff",staffDao.countStaffByStatus("AKTIF"));
        model.addAttribute("jmlRole",staffRoleDao.countStaffRoleByStatus("AKTIF"));


    }

    @GetMapping("/")
    public String formAwal(){
        return "redirect:/login";
    }

}
