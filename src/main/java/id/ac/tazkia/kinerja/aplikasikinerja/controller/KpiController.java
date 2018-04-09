package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.CategoryDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.IndicatorsDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.InputKpiDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Indicators;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Optional;

@Controller
public class KpiController {
    @Autowired
    private KpiDao kpiDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private IndicatorsDao indicatorsDao;

    @ModelAttribute("daftarCategory")
    public Iterable<Category> daftarCategory() {
        return categoryDao.findAll();
    }

    @ModelAttribute("daftarKpi")
    public Iterable<Kpi> daftarKpi() {
        return kpiDao.findAll();
    }

    @ModelAttribute("daftarIndicators")
    public Iterable<Indicators> daftarIndicators() {
        return indicatorsDao.findAll();
    }

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
    public void form(Model m, @RequestParam(value = "id", required = false) String id) {
        Kpi k = new Kpi();
        Indicators indicators = new Indicators();
        m.addAttribute("upda", k);
        m.addAttribute("ind", indicators);

    }

    @PostMapping(value = "/kpi/form")
    public String proses(@ModelAttribute @Valid InputKpiDto dto, BindingResult errors, SessionStatus status) {
        Kpi kpi = new Kpi();
        BeanUtils.copyProperties(dto, kpi);
        kpi.setKeyResult(dto.getKeyResult());

        Indicators i1 = new Indicators();
        i1.setKpi(kpi);
        i1.setScore(dto.getScore1());
        i1.setContent(dto.getIndicator1());
        Indicators i2 = new Indicators();
        i2.setKpi(kpi);
        i2.setScore(dto.getScore2());
        i2.setContent(dto.getIndicator2());
        Indicators i3 = new Indicators();
        i3.setKpi(kpi);
        i3.setScore(dto.getScore3());
        i3.setContent(dto.getIndicator3());
        Indicators i4 = new Indicators();
        i4.setKpi(kpi);
        i4.setScore(dto.getScore4());
        i4.setContent(dto.getIndicator4());
        Indicators i5 = new Indicators();
        i5.setKpi(kpi);
        i5.setScore(dto.getScore5());
        i5.setContent(dto.getIndicator5());


        kpiDao.save(kpi);
        indicatorsDao.save(i1);
        indicatorsDao.save(i2);
        indicatorsDao.save(i3);
        indicatorsDao.save(i4);
        indicatorsDao.save(i5);
        return "redirect:list";
    }

    @GetMapping("/kpi/hapus")
    public void displayHapusForm(@RequestParam String kpi,Model m) {

        Optional<Kpi> staffKpi = kpiDao.findById(kpi);
        m.addAttribute("kpi", staffKpi);
    }

    @PostMapping("/kpi/hapus")
    public String processHapusForm(@RequestParam Kpi kpi) {
        if (kpi == null) {
            return "redirect:list";
        }
        kpi.setStatus(StatusKpi.NONAKTIF);
        kpiDao.save(kpi);
        return "redirect:list";
    }

    @GetMapping("kpi/update")
    public void update(@RequestParam Kpi id,Model model){
        Indicators i5 = indicatorsDao.findByKpiAndScore(id, BigInteger.valueOf(5));
        Indicators i4 = indicatorsDao.findByKpiAndScore(id, BigInteger.valueOf(4));
        Indicators i3 = indicatorsDao.findByKpiAndScore(id, BigInteger.valueOf(3));
        Indicators i2 = indicatorsDao.findByKpiAndScore(id, BigInteger.valueOf(2));
        Indicators i1 = indicatorsDao.findByKpiAndScore(id,BigInteger.valueOf(1));

        InputKpiDto in = new InputKpiDto();
        in.setBaseLine(id.getBaseLine());
        in.setCategory(id.getCategory());
        in.setWeight(id.getWeight());
        in.setKeyResult(id.getKeyResult());
        in.setTarget(id.getTarget());
        in.setIndicator5(i5.getContent());
        in.setIndicator4(i4.getContent());
        in.setIndicator3(i3.getContent());
        in.setIndicator2(i2.getContent());
        in.setIndicator1(i1.getContent());
        in.setId(id.getId());
        model.addAttribute("kpi",in);

    }

    @PostMapping("/kpi/update")
    public String prosesUpdate(@ModelAttribute @Valid InputKpiDto dto, BindingResult errors, SessionStatus status){
        Optional<Kpi> kpi = kpiDao.findById(dto.getId());
        kpi.get().setBaseLine(dto.getBaseLine());
        kpi.get().setCategory(dto.getCategory());
        kpi.get().setKeyResult(dto.getKeyResult());
        kpi.get().setWeight(dto.getWeight());
        kpi.get().setTarget(dto.getTarget());

        Indicators i1 = indicatorsDao.findByKpiAndScore(kpi.get(), BigInteger.valueOf(1));
        i1.setContent(dto.getIndicator1());
        Indicators i2 = indicatorsDao.findByKpiAndScore(kpi.get(), BigInteger.valueOf(2));
        i2.setContent(dto.getIndicator2());
        Indicators i3 = indicatorsDao.findByKpiAndScore(kpi.get(), BigInteger.valueOf(3));
        i3.setContent(dto.getIndicator3());
        Indicators i4 = indicatorsDao.findByKpiAndScore(kpi.get(), BigInteger.valueOf(4));
        i4.setContent(dto.getIndicator4());
        Indicators i5 = indicatorsDao.findByKpiAndScore(kpi.get(), BigInteger.valueOf(5));
        i5.setContent(dto.getIndicator5());

        kpiDao.save(kpi.get());
        indicatorsDao.save(i1);
        indicatorsDao.save(i2);
        indicatorsDao.save(i3);
        indicatorsDao.save(i4);
        indicatorsDao.save(i5);
        return "redirect:list";

    }



}