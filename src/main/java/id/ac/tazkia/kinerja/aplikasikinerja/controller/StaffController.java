package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class StaffController {

    @Autowired
    StaffRoleDao staffRoleDao;

    /*@GetMapping("/staff/list")
    public void daftarStaff(@RequestParam(required = false)String staff, Model m, Pageable page){
        if(StringUtils.hasText(staff)) {
            m.addAttribute("staff", staff);
            m.addAttribute("daftarStaff", staffRoleDao.findByEmployeeName(staff, page));
        } else {
            m.addAttribute("daftarStaff", staffRoleDao.findAll(page));
        }

    }
*/
    @GetMapping("/staff/list")
    public void listPageStaf(){

    }

    @GetMapping("/staff/form")
    public void formPageStaf(){

    }

}
