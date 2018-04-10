package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleController {

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private KpiDao kpiDao;

    @GetMapping("/role/list")
    private ModelMap roleList(Pageable page) {
        return new ModelMap()
                .addAttribute("roleList", staffRoleDao.findAll(page));
    }


}
