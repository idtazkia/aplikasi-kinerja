package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Role;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class RoleController {

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private KpiDao kpiDao;

    @GetMapping("/role/list")
    private void roleList(Model model,String role,@PageableDefault(size = 10) Pageable page) {
        if (StringUtils.hasText(role)) {
            model.addAttribute("role", role);
            model.addAttribute("roleList", staffRoleDao.findByStatusAndAndRoleNameContainingIgnoreCaseOrderByRoleName(AktifConstants.Aktif,role,page));
        } else {
            model.addAttribute("roleList", staffRoleDao.findByStatus(AktifConstants.Aktif,page));
        }
    }

    @GetMapping("/role/form")
    private void displayForm(Model m,@RequestParam(value="role", required = false) String role){

        m.addAttribute("role", new StaffRole());
        m.addAttribute("staffRole",staffRoleDao.findByStatus(AktifConstants.Aktif));
        m.addAttribute("pilihanKpi", kpiDao.findByStatus(StatusKpi.AKTIF));/*mengambil data kpi*/

            if (role != null && !role.isEmpty()) {
                StaffRole staffRole = staffRoleDao.findById(role).get();
                if (staffRole != null) {
                    m.addAttribute("role", staffRole);
                    m.addAttribute("kpiSekarang", staffRole.getKpi());
                    m.addAttribute("kpiSekarang2", staffRole.getKpi());
                }
            }

    }

    @PostMapping("/role/form")
    public String proses(@ModelAttribute @Valid StaffRole staffRole, BindingResult error){
        staffRole.setStatus(AktifConstants.Aktif);
        staffRoleDao.save(staffRole);

        return "redirect:/daftarbawahan/role";
    }

    @PostMapping("/role/delete")
    public String deleteStaff(@RequestParam StaffRole staffRole) {
        if (staffRole == null) {
            return "redirect:/404";
        }

        staffRole.setStatus(AktifConstants.Nonaktif);
        staffRoleDao.save(staffRole);

        return "redirect:list";
    }

}
