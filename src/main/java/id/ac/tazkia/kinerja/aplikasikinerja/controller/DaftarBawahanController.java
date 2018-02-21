package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
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

    @Autowired private KpiDao kpiDao;

    @Autowired
    private StaffKpiDao staffKpiDao;

    @Autowired
    private IndicatorsDao indicatorsDao;

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

    @RequestMapping("/daftarbawahan/form")
    public void  daftarKpi(String id, Model m, Pageable page){

        m.addAttribute("individual", staffKpiDao.findAllByStaff_IdAndKpi_Category_Id(id,"001"));
        m.addAttribute("tazkiaValue", staffKpiDao.findAllByStaff_IdAndKpi_Category_Id(id,"002"));

    }

    @GetMapping("/daftarbawahan/detail")
    public void detail(@RequestParam(required = false)String id, Model m, Pageable page) {
        if (StringUtils.hasText(id)) {
            m.addAttribute("nama", id);
            m.addAttribute("detailStaff", staffDao.findById(id, page));
        } else {
            m.addAttribute("detailStaff", staffDao.findAll(page));
        }

    }

    @GetMapping("/daftarbawahan/komen")
    public void komens(){}

}
