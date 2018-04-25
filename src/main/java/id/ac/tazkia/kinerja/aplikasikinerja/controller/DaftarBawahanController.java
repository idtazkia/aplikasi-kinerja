package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.constants.CategoryConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
public class DaftarBawahanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaftarBawahanController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private IndicatorsDao indicatorsDao;

    @Autowired
    private ScoreDao scoreDao;

    @Autowired
    private EvidenceDao evidenceDao;

    @Autowired
    private KpiDao kpiDao;

    @Autowired
    private PeriodeDao periodeDao;


    @Value("${upload.folder}")
    private String uploadFolder;

    private Category individualCategory;
    private Category tazkiaValueCategory;

    public DaftarBawahanController() {
        individualCategory = new Category();
        individualCategory.setId(CategoryConstants.INDIVIDUAL_INDICATOR_ID);

        tazkiaValueCategory = new Category();
        tazkiaValueCategory.setId(CategoryConstants.TAZKIA_INDICATOR_ID);
    }


    @GetMapping("/daftarbawahan/list")
    public String daftarStaff(Model model, String role, Authentication currentUser) throws Exception {
        StaffRole staffRole = staffRoleDao.findById(role).get();

        Iterable<Staff> daftarBawahan = staffDao.findByRoles(staffRole);
        model.addAttribute("subordinate", daftarBawahan);
        model.addAttribute("roles",staffRole);
        System.out.println(staffRole.getRoleName());


        return "/daftarbawahan/list";

    }

    @GetMapping("/daftarbawahan/form")
    public String daftarKpi(@RequestParam  String id,String staff, Model m, Pageable page, Authentication currentUser) {
        StaffRole staffRole = staffRoleDao.findById(id).get();
        Staff s = staffDao.findById(staff).get();

        m.addAttribute("kpi",staffRole.getKpi());
        m.addAttribute("staff",s);
        m.addAttribute("role",staffRole);

        return "/daftarbawahan/form";

    }

    @ModelAttribute("daftarIndicators")
    public Iterable<Indicators> daftarIndicators() {
        return indicatorsDao.findAll();
    }


    @PostMapping(value = "/daftarbawahan/form")
    public String prosesForm(@RequestParam String id, String staff, HttpServletRequest request) {
        Staff s = staffDao.findById(staff).get();
        StaffRole roles = staffRoleDao.findById(id).get();
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);


        Set<Kpi> getKpi = roles.getKpi();
        for (Kpi kpi : getKpi) {
            LOGGER.info("kpinya :" + kpi.getKeyResult());
            String pilihan = request.getParameter(kpi.getId() + "-score");
            LOGGER.info("Pilihan : " + pilihan);
            Optional<Indicators> indicators = indicatorsDao.findById(pilihan);
            if (indicators != null) {
                Score score = new Score();
                score.setKpi(kpi);
                score.setScore(indicators.get().getScore());
                score.setRemark(indicators.get().getContent());
                score.setPeriode(periode);
                score.setStaff(s);
                scoreDao.save(score);

            }

        }
        return "redirect:/daftarbawahan/list?role=" +roles.getId();

    }

    @GetMapping("/daftarbawahan/detail")
    public String detail(@RequestParam(required = false) String id, Model m, Pageable page, Authentication currentUser) {
        Optional<Staff> s = staffDao.findById(id);
        if (s == null) {
            LOGGER.warn("Staff not found");
            return "redirect:/404";
        }


        System.out.println("username" + currentUser.getClass().getName());

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
        m.addAttribute("detail", p);

        return "/daftarbawahan/detail";

    }

    @GetMapping("/daftarbawahan/komen")
    public String String(@RequestParam(required = true) String id, Model m, Authentication currentUser) throws Exception {
       /* Optional<Staff> s = staffDao.findById(id);

        if (s == null) {
            LOGGER.warn("Staff not found");
            return "redirect:/404";
        }

        System.out.println("username" + currentUser.getClass().getName());

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


        m.addAttribute("individual", scoreDao.findByStaffKpiStaffIdAndStaffKpiKpiCategoryOrderByStaffKpiAsc(id, individualCategory));
        m.addAttribute("tazkiaValue", scoreDao.findByStaffKpiStaffIdAndStaffKpiKpiCategoryOrderByStaffKpiAsc(id, tazkiaValueCategory));
*/

        return "/daftarbawahan/komen";
    }


    @GetMapping("/daftarbawahan/evidence/list")
    public void evidence(@RequestParam String role,String evidence, Model model) {
        StaffRole sr = staffRoleDao.findById(role).get();

        model.addAttribute("individual",sr.getKpi());
        model.addAttribute("role",sr);

      /*  Evidence ev;
        ev = evidenceDao.findById(evidence).get();

        model.addAttribute("evidence", ev.getKpi().getKeyResult()).addAttribute("evidence", ev);
*/
    }

    @GetMapping("/daftarbawahan/evidence/detail")
    public void daftarNilaiBiaya(@RequestParam(required = false)String kpi, Model m, Pageable page){
        /*if(StringUtils.hasText(role)) {
            m.addAttribute("staff", role);
            m.addAttribute("detailevidence", staffRoleDao.findById(role));
        } else {
            m.addAttribute("detailevidence", staffRoleDao.findAll(page));
        }*/

     /*   List<StaffRole> sr = staffRoleDao.findDistinctByKpi_Id(kpi);

        m.addAttribute("detailevidence",sr);
        m.addAttribute("role",sr);*/

        Kpi kpi1 = kpiDao.findById(kpi).get();

        m.addAttribute("detailevidence", kpi1);


    }

    @GetMapping("/uploaded/{evidence}/bukti/")
    public ResponseEntity<byte[]> tampilkanEvidence(@PathVariable Evidence evidence, Model model) throws Exception {
        String lokasiFile = uploadFolder + File.separator + evidence.getStaff().getId()
                + File.separator + evidence.getFilename();
        LOGGER.debug("Lokasi file bukti : {}", lokasiFile);

        try {
            HttpHeaders headers = new HttpHeaders();
            if (evidence.getFilename().toLowerCase().endsWith("jpeg") || evidence.getFilename().toLowerCase().endsWith("jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (evidence.getFilename().toLowerCase().endsWith("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (evidence.getFilename().toLowerCase().endsWith("pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            byte[] data = Files.readAllBytes(Paths.get(lokasiFile));
            return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


        }

    }

    @GetMapping("/daftarbawahan/evidence/form")
    public String inputEvidence(@RequestParam String role,String id, Model m) {
        Kpi kpi = kpiDao.findById(id).get();
        StaffRole sr = staffRoleDao.findById(role).get();
        if (kpi.getId() == null && sr.getId() == null) {
            return "redirect:/404";
        }



        m.addAttribute("kpi", kpi);
        m.addAttribute("role", sr);

        Evidence evidence = new Evidence();
        return "/daftarbawahan/evidence/form";

    }

    @PostMapping("/daftarbawahan/evidence/form")
    public String uploadBukti(@RequestParam String role, String id, MultipartFile file,
                              Authentication currentUser) throws Exception {
        Kpi kpi = kpiDao.findById(id).get();
        StaffRole sr = staffRoleDao.findById(role).get();

//        mengambil data user yang login
        LOGGER.debug("Authentication class : {}", currentUser.getClass().getName());

        if (currentUser == null) {
            LOGGER.warn("Current user is null");
            return "redirect:/404";
        }

        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);
        LOGGER.debug("User ID : {}", u.getId());
        if (u == null) {
            LOGGER.warn("Username {} not found in database ", username);
            return "redirect:/404";
        }

        Staff staff = staffDao.findByUser(u);
        LOGGER.debug("Nama Pendaftar : " + staff.getEmployeeName());
        if (staff == null) {
            LOGGER.warn("Pendaftar not found for username {} ", username);
            return "redirect:/404";
        }


//


        String idEmployee = staff.getId();

        String namaFile = file.getName();
        String jenisFile = file.getContentType();
        String namaAsli = file.getOriginalFilename();
        Long ukuran = file.getSize();

        LOGGER.debug("Nama File : {}", namaFile);
        LOGGER.debug("Jenis File : {}", jenisFile);
        LOGGER.debug("Nama Asli File : {}", namaAsli);
        LOGGER.debug("Ukuran File : {}", ukuran);

//        memisahkan extensi
        String extension = "";

        int i = namaAsli.lastIndexOf('.');
        int p = Math.max(namaAsli.lastIndexOf('/'), namaAsli.lastIndexOf('\\'));

        if (i > p) {
            extension = namaAsli.substring(i + 1);
        }

        String idFile = UUID.randomUUID().toString();
        String lokasiUpload = uploadFolder + File.separator + idEmployee;
        LOGGER.debug("Lokasi upload : {}", lokasiUpload);
        new File(lokasiUpload).mkdirs();
        File tujuan = new File(lokasiUpload + File.separator + idFile + "." + extension);
        file.transferTo(tujuan);
        LOGGER.debug("File sudah dicopy ke : {}", tujuan.getAbsolutePath());

        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

        Evidence evidence = new Evidence();
        evidence.setStaff(staff);
        evidence.setPeriode(periode);
        evidence.setKpi(kpi);
        evidence.setFilename(idFile + "." + extension);
        evidence.setDescription("Upload Bukti Karyawan");
        evidenceDao.save(evidence);


        return "redirect:/daftarbawahan/evidence/list?role=" +sr.getId();

    }

    @GetMapping("daftarbawahan/role")
    public String role(Model model, Authentication currentUser) throws Exception {
        System.out.println("username" + currentUser.getClass().getName());

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


        Set<StaffRole> daftarRoleBawahan = staffRoleDao.findDistinctBySuperiorRoleIn(p.getRoles());

        model.addAttribute("role", daftarRoleBawahan);
        model.addAttribute("detail", p);

        return "/daftarbawahan/role";


    }
}

