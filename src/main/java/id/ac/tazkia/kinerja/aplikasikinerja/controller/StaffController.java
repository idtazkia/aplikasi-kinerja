package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StaffRoleForm;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class StaffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    @Autowired
    KpiDao kpiDao;



    @ModelAttribute("daftarKpi")
    public Iterable<Kpi> daftarKpi() {
        return kpiDao.findAll();
    }

    @GetMapping("staff/kpi")
    public void staffKpi() {
    }



    @GetMapping("/staff/list")
        public void daftarStaff(Model m,@PageableDefault(size = 10) Pageable page, String search ) throws Exception{

        if (StringUtils.hasText(search)) {
            m.addAttribute("search", search);
            m.addAttribute("daftarStaff", staffDao.findByEmployeeNameContainingIgnoreCaseOrderByEmployeeName(search,page));
        } else {
            m.addAttribute("daftarStaff",staffDao.findAll(page));

        }

    }


    @RequestMapping(value = "/staff/form", method = RequestMethod.GET)
    public void tampilkanForm(Model model){
        model.addAttribute("staff", new Staff());
    }


    @RequestMapping(value = "/staff/form", method = RequestMethod.POST)
    public String prosesForm(@Valid Staff s, BindingResult errors){
        if(errors.hasErrors()){
            return "/staff/form";
        }
        staffDao.save(s);
        return "redirect:list";
    }


    @GetMapping("/staff/role")
    public void formPageRole(Model m2,Staff staff) throws Exception{
        StaffRoleForm srf = new StaffRoleForm();
        srf.setStaff(staff);
        m2.addAttribute("staffRoleForm",srf);
        m2.addAttribute("pilihanRole",staffRoleDao.findAll());
        m2.addAttribute("roleSekarang",staff.getRoles());
    }

    @PostMapping("/staff/role")
    public String prosesFormRole(@ModelAttribute @Valid StaffRoleForm srs, BindingResult errors)throws Exception {

        Staff staff = srs.getStaff();
        Set<StaffRole> roles = srs.getRoles();

        LOGGER.debug("Staff : {}" , staff.getEmployeeName());
        LOGGER.debug(" === Roles === ");
        for(StaffRole r : roles){
            LOGGER.debug("Role : {}",r.getRoleName());
        }
        LOGGER.debug(" === Roles === ");

        staff.setRoles(roles);
        staffDao.save(staff);

        if(errors.hasErrors()){
            return "/staff/role";
        }
        return "redirect:list";
    }

}
