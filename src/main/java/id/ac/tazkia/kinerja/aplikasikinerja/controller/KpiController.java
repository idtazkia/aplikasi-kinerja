package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KpiController {
    @Autowired
    private KpiDao kpiDao;

    @GetMapping("/kpi/list")
    public void list( Model model,Pageable page,String keyresult) {
        if (StringUtils.hasText(keyresult)) {
            model.addAttribute("keyresult", keyresult);
            model.addAttribute("data", kpiDao.findByKeyResultContainingIgnoreCaseOrderByKeyResult(keyresult, page));
        } else {
            model.addAttribute("data", kpiDao.findAll(page));
        }
    }

    @GetMapping("/kpi/form")
    public void form() {

    }
}
