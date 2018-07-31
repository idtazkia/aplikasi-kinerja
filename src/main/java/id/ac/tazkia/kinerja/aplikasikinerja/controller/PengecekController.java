package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;


@Controller
public class PengecekController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PengecekController.class);
    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private UserDao userDao;

    @GetMapping("/pengecek/list")
    public String role(Model model, @PageableDefault(size = 10) Pageable page,  Authentication currentUser, String search) throws Exception {

        if (currentUser == null) {
            LOGGER.warn("Current user is null");
            return "redirect:/404";
        }

        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);

        if (u == null) {
            LOGGER.warn("Username {} not found in database " + username);
            return "redirect:/404";
        }

        Staff p = staffDao.findByUser(u);


        if (p == null) {
            LOGGER.warn("Staff not found for username {} " + username);
            return "redirect:/404";
        }


        /*StringUtils.hasText(search);
        model.addAttribute("search", search);
        model.addAttribute("role", staffRoleDao.findById(search, page));*/

        Iterable<StaffRole> daftarRoleBawahan = staffRoleDao.findAll(page);
        model.addAttribute("role", daftarRoleBawahan);


        return "pengecek/list";


    }


}
