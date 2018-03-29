package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KpiController {
    @Autowired
    private KpiDao kpiDao;


    @GetMapping("/kpi/list")
    public void list(Model model, Pageable page, String keyresult) {
        if (StringUtils.hasText(keyresult)) {
            model.addAttribute("keyresult", keyresult);
            model.addAttribute("data", kpiDao.findByStatusAndKeyResultContainingIgnoreCaseOrderByKeyResult(StatusKpi.AKTIF,keyresult, page));
        } else {
            model.addAttribute("data", kpiDao.findByStatus(StatusKpi.AKTIF,page));
        }
    }

    @GetMapping("/kpi/form")
    public void form() {

    }

}
