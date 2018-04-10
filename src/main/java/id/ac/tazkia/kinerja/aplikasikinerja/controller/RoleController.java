package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StaffKpiDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class RoleController {

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private KpiDao kpiDao;

    @GetMapping("/role/list")
    private ModelMap roleList(Pageable page) {
        return new ModelMap()
                .addAttribute("roleList", staffRoleDao.findAll(page));
    }

    @GetMapping("role/kpi")
    private void kpiInput(@RequestParam String role, Model model) {
        StaffRole staffRole = staffRoleDao.findById(role).get();

        StaffKpiDto staffKpiDto = new StaffKpiDto();

        model.addAttribute("role", staffRole);/*mengambil data role*/

        model.addAttribute("pilihanKpi", kpiDao.findAll());/*mengambil data kpi*/

        Set<Kpi> staffRoles = staffRole.getKpi();
        for (Kpi sr : staffRoles) {
        }
        model.addAttribute("kpiSekarang", staffRoles);
    }

    @PostMapping("role/kpi")
    public String proses(@RequestParam StaffRole role, @ModelAttribute @Valid StaffKpiDto s, BindingResult errors, SessionStatus status) {

        Set<StaffRole> staffRole = s.getRoles();
        System.out.println("kpiiiii : " + s);
        for (StaffRole sr : staffRole) {
            sr.setKpi(s.getKpi());

            System.out.println("isinya :" + sr);
            staffRoleDao.save(sr);
        }


        status.setComplete();
        return "redirect:list";
    }
}
