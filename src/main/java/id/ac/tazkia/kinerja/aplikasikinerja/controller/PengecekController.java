package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.KpiTerisi;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.RekapPengisianKpi;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StatusPengisian;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import id.ac.tazkia.kinerja.aplikasikinerja.helper.RekapKpiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.dialect.springdata.exception.InvalidObjectParameterException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private PeriodeDao periodeDao;

    @Autowired
    private EvidenceDao evidenceDao;

    @Autowired
    private ScoreDao scoreDao;


    @GetMapping("/pengecek/list")
    public void staffList(Model model, @PageableDefault(size = 300) Pageable page,String search) throws Exception {

        List<RekapPengisianKpi> rekap = new ArrayList<>();
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);
//        Staff staffnya = (Staff) staffDao.findByStatus(AktifConstants.Aktif);



        if (StringUtils.hasText(search)) {
            model.addAttribute("search", search);
            staffDao.findByStatusAndEmployeeNameContainingIgnoreCaseOrderByEmployeeName(AktifConstants.Aktif, search, page)
                    .forEach(staff -> {
                        for (StaffRole staffRole : staff.getRoles()) {

                            List<Evidence> evidence = evidenceDao.findByStaffAndPeriode(staff,periode);
                            List<Score> score = scoreDao.findByStaffAndPeriode(staff,periode);


                            Integer jumlah = staffRole.getKpi().size();
                            RekapPengisianKpi r = new RekapPengisianKpi();
                            r.setStaff(staff);
                            r.setId(staff.getId());
                            r.setNama(staff.getEmployeeName());
                            r.setArea(staff.getArea());
                            r.setDepartment(staff.getDepartment());
                            r.setIdRole(staffRole.getId());
                            r.setNamaRole(staffRole.getRoleName());
                            r.setJumlahKpi(jumlah.longValue());

                            for (Evidence evinya : evidence) {
                                if (evinya.getStaff() == r.getStaff()) {
                                    r.setStatusPengisian("DONE");
//                                                    System.out.println("Staff : " + evinya.getStaff().getId() + " | Kpi : " + evinya.getKpi().getId());
                                }
                            }
                            for (Score scorenya : score){
                                if (scorenya.getStaff() == r.getStaff()) {
                                    r.setStatusPenilaian("DONE");
                                }
                            }

                            rekap.add(r);



                        }
                        model.addAttribute("viewall", rekap);

                    });
        } else {
            staffDao.findByStatus(AktifConstants.Aktif, page).forEach(staff -> {
                for (StaffRole staffRole : staff.getRoles()) {

                    List<Evidence> evidence = evidenceDao.findByStaffAndPeriode(staff,periode);
                    List<Score> score = scoreDao.findByStaffAndPeriode(staff,periode);

                    Integer jumlah = staffRole.getKpi().size();
                    RekapPengisianKpi r = new RekapPengisianKpi();
                    r.setStaff(staff);
                    r.setId(staff.getId());
                    r.setNama(staff.getEmployeeName());
                    r.setArea(staff.getArea());
                    r.setDepartment(staff.getDepartment());
                    r.setIdRole(staffRole.getId());
                    r.setNamaRole(staffRole.getRoleName());
                    r.setJumlahKpi(jumlah.longValue());
                    System.out.println("Staff : " + r.getNama());
                    for (Evidence evinya : evidence) {
                        if (evinya.getStaff() == r.getStaff()) {
                            r.setStatusPengisian("DONE");
//                                                System.out.println("Staff : " + evinya.getStaff().getId() + " | Kpi : " + evinya.getKpi().getId());
                        }
                    }
                    for (Score scorenya : score){
                        if (scorenya.getStaff() == r.getStaff()) {
                            r.setStatusPenilaian("DONE");
                        }
                    }

                    rekap.add(r);



                }
                model.addAttribute("viewall", rekap);
            });

        }




    }



    @GetMapping("/pengecek/role")
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


        if (StringUtils.hasText(search)) {
            model.addAttribute("search", search);
            model.addAttribute("role", staffRoleDao.findByStatusAndAndRoleNameContainingIgnoreCaseOrderByRoleName(AktifConstants.Aktif,search,page));
        } else {
            model.addAttribute("role", staffRoleDao.findAll(page));

        }

        return "pengecek/role";


    }


}
