package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.RoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserPasswordDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.InputStaffDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class UserDataController {

    private static final Logger logger = LoggerFactory.getLogger(UserDataController.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private UserPasswordDao userPasswordDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/data")
    public void data(Model model, Authentication currentUser) {
        logger.debug("Authentication class : {}", currentUser.getClass().getName());

        if (currentUser == null) {
            logger.warn("Current user is null");
            return;
        }

        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);
        logger.debug("User ID : {}", u.getId());
        if (u == null) {
            logger.warn("Username {} not found in database ", username);
            return;
        }

        Staff p = staffDao.findByUser(u);
        logger.debug("Nama Pendaftar : " + p.getEmployeeName());
        if (p == null) {
            logger.warn("Pendaftar not found for username {} ", username);
            return;
        }

        User user = userDao.findByUsername(p.getUser().getUsername());
        UserPassword up = userPasswordDao.findByUser(user);

        InputStaffDto isd = new InputStaffDto();
        isd.setEmployeeName(p.getEmployeeName());
        isd.setEmployeeNumber(p.getEmployeeNumber());
        isd.setDepartment(p.getDepartment());
        isd.setJobTitle(p.getJobTitle());
        isd.setJobLevel(p.getJobLevel());
        isd.setArea(p.getArea());
        isd.setPassword(up.getPassword());
        isd.setEmail(user.getEmail());
        isd.setUsername(user.getUsername());
        isd.setSecurityRole(user.getRole().getId());
        isd.setId(p.getId());
        isd.setUser(user);
        isd.setUserPassword(up);
        isd.setRoles(p.getRoles());

        model.addAttribute("staff",isd);


    }

    @PostMapping("/user/data")
    public String prosesUpdate(@ModelAttribute @Valid InputStaffDto isd, BindingResult errors, SessionStatus status) {
        Role role = roleDao.findById(isd.getSecurityRole()).get();
        User user = userDao.findById(isd.getUser().getId()).get();
        user.setId(isd.getUser().getId());
        user.setUsername(isd.getUsername());
        user.setRole(role);
        user.setEmail(isd.getEmail());
        user.setActive(Boolean.TRUE);

        Staff staff = staffDao.findByUser(user);
        staff.setId(isd.getId());
        staff.setArea(isd.getArea());
        staff.setEmployeeName(isd.getEmployeeName());
        staff.setEmployeeNumber(isd.getEmployeeNumber());
        staff.setJobTitle(isd.getJobTitle());
        staff.setJobLevel(isd.getJobLevel());
        staff.setUser(user);

        UserPassword userPassword = userPasswordDao.findByUser(user);
        userPassword.setPassword(passwordEncoder.encode(isd.getPassword()));
        userPassword.setUser(user);
        userPassword.setId(isd.getUserPassword().getId());

        Set<StaffRole> roles = isd.getRoles();

        for (StaffRole r : roles) {
            logger.debug("role name" + r.getRoleName());
        }

        staff.setRoles(roles);


        userDao.save(user);
        staffDao.save(staff);
        userPasswordDao.save(userPassword);

        return "redirect:/user/data";

    }
}
