package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleStaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRoleStaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class StaffController {

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    @Autowired
    StaffRoleStaffDao staffRoleStaffDao;

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
    public void formPageRole(Model m2, String staffRole) throws Exception{
        m2.addAttribute("panggilRole",staffRoleDao.findAll());

    }

    @RequestMapping(value = "/staff/role", method = RequestMethod.POST)
    public String prosesFormRole(@ModelAttribute @Valid StaffRoleStaff srs, BindingResult errors){
        if(errors.hasErrors()){
            return "/staff/role";
        }
        staffRoleStaffDao.save(srs);
        return "redirect:list";
    }

}
