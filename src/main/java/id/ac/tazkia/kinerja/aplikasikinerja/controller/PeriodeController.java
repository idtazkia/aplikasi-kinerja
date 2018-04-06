package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.PeriodeDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PeriodeController {
    @Autowired
    private PeriodeDao periodeDao;

    @GetMapping("/periode/list")
    public ModelMap list(@PageableDefault(sort = "active", direction = Sort.Direction.ASC) Pageable page) {
        return new ModelMap()
                .addAttribute("list", periodeDao.findAll(page));
    }

    @PostMapping("/periode/aktif")
    public String processAktifForm(@RequestParam Periode id) {
        if (id == null) {
            return "redirect:/404";
        }

        Iterable<Periode> p = periodeDao.findAll();
        for (Periode periode : p) {
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


}
