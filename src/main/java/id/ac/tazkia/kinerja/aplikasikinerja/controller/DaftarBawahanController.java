package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DaftarBawahanController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StaffDao staffDao;

    @RequestMapping("/daftarbawahan/list")
    public void  daftarStaff(Model model,String user, Authentication currentUser)throws Exception{
        System.out.println("username" + currentUser.getClass().getName());

        if(currentUser == null){
            System.out.println("Current user is null");
            return;
        }

        String username = ((UserDetails)currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);

        if(u == null){
            System.out.println("Username {} not found in database " + username);
            return;
        }

        Staff p = staffDao.findByUser(u);


        if(p == null){
            System.out.println("Pendaftar not found for username {} " + username);
            return;
        }

        model.addAttribute("list", staffDao.findAllBySuperiorIdOrderByEmployeeName(p.getId()));


    }


    @GetMapping("/daftarbawahan/detail")
    public void detail(){}

    @GetMapping("/daftarbawahan/komen")
    public void komens(){}

}
