package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.ScoreDao;
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
public class LihatPenilaianController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LihatPenilaianController.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private ScoreDao scoreDao;

    @GetMapping("/lihatpenilaian/list")
    public void list(Model model, Authentication currentUser){
        LOGGER.debug("username" + currentUser.getClass().getName());

        if(currentUser == null){
            LOGGER.warn("Current user is null");
            return;
        }

        String username = ((UserDetails)currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);

        if(u == null){
            LOGGER.warn("Username {} not found in database " + username);
            return;
        }

        Staff p = staffDao.findByUser(u);


        if(p == null){
            LOGGER.info("Employee not found for username {} " + username);
            return;
        }

        model.addAttribute("individual",scoreDao.findAllByStaffKpi_Staff_IdAndStaffKpi_Kpi_Category_id(u.getId(),"001"));
        model.addAttribute("tazkiaValue",scoreDao.findAllByStaffKpi_Staff_IdAndStaffKpi_Kpi_Category_id(u.getId(),"002"));
    }
}
