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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @Autowired
    private ScoreCommentDao scoreCommentDao;


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
    public String daftarStaff(Model model, String role){
        StaffRole staffRole = staffRoleDao.findById(role).get();

        model.addAttribute("subordinate", staffDao.findByRolesAndStatus(staffRole,AktifConstants.Aktif));
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
    public ModelMap detail(@RequestParam Staff id){
        return new ModelMap()
                .addAttribute("detail",id);
    }

    @GetMapping("/daftarbawahan/komen")
    public void String(@RequestParam(required = true) String id, Model m){
        Staff staff = staffDao.findById(id).get();
        if (staff == null){
            LOGGER.debug("staff tidak ada");
        }
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

        m.addAttribute("individual",scoreCommentDao.findByAuthorAndPeriodeAndScoreKpiCategory(staff,periode,individualCategory));
        m.addAttribute("tazkiaValue",scoreCommentDao.findByAuthorAndPeriodeAndScoreKpiCategory(staff,periode,tazkiaValueCategory));
    }


    @GetMapping("/daftarbawahan/evidence/list")
    public ModelMap evidenceList(@RequestParam StaffRole role,Authentication currentUser){
        LOGGER.debug("Authentication class : {}", currentUser.getClass().getName());

        if (currentUser == null) {
            LOGGER.warn("Current user is null");
        }

        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);
        LOGGER.debug("User ID : {}", u.getId());
        if (u == null) {
            LOGGER.warn("Username {} not found in database ", username);
        }

        Staff p = staffDao.findByUser(u);
        LOGGER.debug("Nama Pendaftar : " + p.getEmployeeName());
        if (p == null) {
            LOGGER.warn("Pendaftar not found for username {} ", username);
        }

        return new ModelMap()
                .addAttribute("individual",role.getKpi())
                .addAttribute("role",role)
                .addAttribute("staff",p);
    }

    @GetMapping("/daftarbawahan/evidence/detail")
    public void detailEvidence(@RequestParam String staff, String kpi, Model m, Pageable page){
        Staff s = staffDao.findById(staff).get();
        Kpi k = kpiDao.findById(kpi).get();
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

        List<Evidence> evidence = evidenceDao.findByKpiAndStaffAndPeriode(k,s,periode);
        m.addAttribute("detailEvidence",evidence);
        m.addAttribute("kpi",k);
        m.addAttribute("periode",periode);


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
    public String uploadBukti(@RequestParam String role, String id, @Valid Evidence evidence,
                              BindingResult error, MultipartFile[] file,
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


        for (MultipartFile upload : file){
            String namaFile = upload.getName();
            String jenisFile = upload.getContentType();
            String namaAsli = upload.getOriginalFilename();
            Long ukuran = upload.getSize();

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
            String lokasiUpload = uploadFolder + File.separator + staff.getId();
            LOGGER.debug("Lokasi upload : {}", lokasiUpload);
            new File(lokasiUpload).mkdirs();
            File tujuan = new File(lokasiUpload + File.separator + idFile + "." + extension);
            upload.transferTo(tujuan);
            LOGGER.debug("File sudah dicopy ke : {}", tujuan.getAbsolutePath());

            Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

            evidence.setStaff(staff);
            evidence.setPeriode(periode);
            evidence.setKpi(kpi);
            evidence.setFilename(idFile + "." + extension);
            evidenceDao.save(evidence);
        }



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

