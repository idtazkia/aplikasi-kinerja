package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffKpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StaffRoleForm;
import id.ac.tazkia.kinerja.aplikasikinerja.constants.CategoryConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StaffKpiDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
import org.springframework.web.bind.support.SessionStatus;

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

    @Autowired
    UserDao userDao;

    @Autowired
    StaffKpiDao staffKpiDao;


    private Category individualCategory;
    private Category tazkiaValueCategory;

    public StaffController() {
              individualCategory = new Category();
              individualCategory.setId(CategoryConstants.INDIVIDUAL_INDICATOR_ID);


              tazkiaValueCategory = new Category();
              tazkiaValueCategory.setId(CategoryConstants.TAZKIA_INDICATOR_ID);
        }


    @GetMapping("staff/kpi")
    public String staffKpi(@RequestParam(required = true) Staff staff, Model model) {



          if (staff == null) {
          return "redirect:/404";
        }

        model.addAttribute("daftarKpi", kpiDao.findByCategoryAndStatus(individualCategory, StatusKpi.AKTIF));
        model.addAttribute("tazKpi", kpiDao.findByCategoryAndStatus(tazkiaValueCategory, StatusKpi.AKTIF));

       StaffKpi sk = new StaffKpi();
       sk.setStaff(staff);
       model.addAttribute("staffKpi", sk);



       return null;

    }


    @PostMapping("staff/kpi")
    public String proses(@RequestParam Staff staff, @ModelAttribute @Valid StaffKpiDto s, BindingResult errors, SessionStatus status){

        System.out.println("Staff : " + s.getStaff().getEmployeeName());
          System.out.println("KPIs : ");
          for(Kpi k : s.getKpi()){
          System.out.println("KPI : " + k.getKeyResult());
          StaffKpi staffKpi = new StaffKpi();
          staffKpi.setKpi(k);
          staffKpi.setStaff(staff);
          staffKpiDao.save(staffKpi);
        }

        status.setComplete();
        return "redirect:list";
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

    @GetMapping("/staff/detail")
    public String detail(@RequestParam(required = false) Staff staff, Model m, Pageable page, Authentication currentUser) {
        if (staff == null) {
            return "redirect:/404";
        }


        m.addAttribute("detail", staffDao.findById(staff.getId()));
        m.addAttribute("kpi",staffKpiDao.findByStaff(staff));


        return null;

    }



}
