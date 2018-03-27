package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDataController {

    private static final Logger logger = LoggerFactory.getLogger(UserDataController.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private StaffDao staffDao;


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

        model.addAttribute("data", staffDao.findById(p.getId()));
    }
}
