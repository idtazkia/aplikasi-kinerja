package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.constants.CategoryConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.InputStaffDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    UserPasswordDao userPasswordDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private Category individualCategory;
    private Category tazkiaValueCategory;

    public StaffController() {
        individualCategory = new Category();
        individualCategory.setId(CategoryConstants.INDIVIDUAL_INDICATOR_ID);


        tazkiaValueCategory = new Category();
        tazkiaValueCategory.setId(CategoryConstants.TAZKIA_INDICATOR_ID);
    }


    @GetMapping("/staff/list")
    public void daftarStaff(Model m, @PageableDefault(size = 10) Pageable page, String search) throws Exception {

        if (StringUtils.hasText(search)) {
            m.addAttribute("search", search);
            m.addAttribute("daftarStaff", staffDao.findByEmployeeNameContainingIgnoreCaseOrderByEmployeeName(search, page));
        } else {
            m.addAttribute("daftarStaff", staffDao.findAll(page));

        }

    }

    @GetMapping("/staff/form")
    public void tampilkanForm(Model model) {
        model.addAttribute("staff", new InputStaffDto());
        model.addAttribute("pilihanRole", staffRoleDao.findByStatus(AktifConstants.Aktif));

    }


    @RequestMapping(value = "/staff/form", method = RequestMethod.POST)
    public String prosesForm(@ModelAttribute @Valid InputStaffDto inputStaffDto, BindingResult errors) {
        if (errors.hasErrors()) {
            return "/staff/form";
        }
        Staff staff = new Staff();
        User user = new User();
        UserPassword userPassword = new UserPassword();

        Role role = roleDao.findById(inputStaffDto.getSecurityRole()).get();

        BeanUtils.copyProperties(inputStaffDto, staff);
        user.setUsername(inputStaffDto.getUsername());
        user.setRole(role);
        user.setEmail(inputStaffDto.getEmail());
        user.setActive(Boolean.TRUE);
        staff.setUser(user);
        userPassword.setPassword(passwordEncoder.encode(inputStaffDto.getPassword()));
        userPassword.setUser(user);

        Set<StaffRole> roles = inputStaffDto.getRoles();

        for (StaffRole r : roles) {
            LOGGER.debug("role name" + r.getRoleName());
        }

        staff.setRoles(roles);

        userDao.save(user);
        staffDao.save(staff);
        userPasswordDao.save(userPassword);
        return "redirect:list";
    }


    @GetMapping("/staff/detail")
    public String detail(@RequestParam(required = false) Staff staff, Model m, Pageable page, Authentication currentUser) {
        if (staff == null) {
            return "redirect:/404";
        }

        m.addAttribute("detail", staffDao.findById(staff.getId()));
        return null;

    }



}
