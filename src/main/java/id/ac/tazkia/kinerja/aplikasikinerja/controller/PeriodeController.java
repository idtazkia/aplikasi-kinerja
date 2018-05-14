package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.PeriodeDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


@Controller
public class PeriodeController {
    @Autowired private PeriodeDao periodeDao;

    @GetMapping("/periode/list")
    public ModelMap list(@PageableDefault(direction = Sort.Direction.ASC) Pageable page){
        return new ModelMap()
                .addAttribute("list",periodeDao.findByStatusOrderByActiveAsc(StatusKpi.AKTIF));
    }

    @PostMapping("/periode/aktif")
    public String processAktifForm(@RequestParam Periode id) {
        if (id == null) {
            return "redirect:/404";
        }

        Iterable<Periode> p = periodeDao.findAll();
        for (Periode periode : p){
            periode.setActive(AktifConstants.Nonaktif);
            periodeDao.save(periode);
        }

        id.setActive(AktifConstants.Aktif);
        periodeDao.save(id);

        return "redirect:/periode/list";
    }

    @PostMapping("/periode/nonaktif")
    public String processNonaktifForm(@RequestParam Periode id) {
        if (id == null) {
            return "redirect:/404";
        }

        id.setActive(AktifConstants.Nonaktif);
        periodeDao.save(id);

        return "redirect:/periode/list";
    }

    @GetMapping("/periode/form")
    public void tampilkanForm(@RequestParam(value = "id", required = false) String id,
                                Model m) {
        //defaultnya, isi dengan object baru
        m.addAttribute("periode", new Periode());

        if (id != null && !id.isEmpty()) {
            Optional<Periode> p = periodeDao.findById(id);
            if (p != null) {
                m.addAttribute("periode", p);
            }
        }
    }

    @PostMapping("/periode/form")
    public String proses(@ModelAttribute @Valid Periode periode, BindingResult error,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){

        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        periode.setActive(AktifConstants.Nonaktif);
        periode.setStartDate(start);
        periode.setEndDate(end);
        periode.setStatus(StatusKpi.AKTIF);
        periodeDao.save(periode);

        return "redirect:list";
    }

    @PostMapping("/delete/periode")
    public String delete(@RequestParam Periode delete){
        if (delete == null){
            return "redirect:/404";
        }

        delete.setStatus(StatusKpi.NONAKTIF);
        periodeDao.save(delete);

        return "redirect:/periode/list";
    }


}
