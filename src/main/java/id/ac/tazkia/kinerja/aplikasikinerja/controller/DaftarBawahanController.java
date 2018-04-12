package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.constants.CategoryConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String daftarStaff(Model model, String user, Authentication currentUser,
                              @PageableDefault(size = 10)
                                      Pageable page, String search) throws Exception {
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

        Optional<Staff> atasan = staffDao.findById(p.getId());
        Page<Staff> daftarBawahan = staffDao.findSubordinate(atasan, page);

        if (StringUtils.hasText(search)) {
            model.addAttribute("search", search);
            model.addAttribute("subordinate", staffDao.findBySuperiorsAndEmployeeNameContainingIgnoreCaseOrderByEmployeeName(atasan, search, page));
        } else {
            model.addAttribute("subordinate", daftarBawahan);
        }

        return "/daftarbawahan/list";

    }

    @GetMapping("/daftarbawahan/form")
    public String daftarKpi(String id, Model m, Pageable page, Authentication currentUser) {
        /*Optional<Staff> s = staffDao.findById(id);

        if (s == null) {
            LOGGER.warn("Staff not found");
            return "redirect:/404";
        }

        m.addAttribute("staff", s);

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


        m.addAttribute("individual", staffKpiDao.findAllByStaffAndKpiCategory(s, individualCategory));
        m.addAttribute("tazkiaValue", staffKpiDao.findAllByStaffAndKpiCategory(s, tazkiaValueCategory));
*/
        return "/daftarbawahan/form";

    }

    @ModelAttribute("daftarIndicators")
    public Iterable<Indicators> daftarIndicators() {
        return indicatorsDao.findAll();
    }


    @PostMapping(value = "/daftarbawahan/form")
    public String prosesForm(@RequestParam String staff, HttpServletRequest request) {
        /*Optional<Staff> s = staffDao.findById(staff);
        System.out.println("Staff : " + s.get().getEmployeeName());

        List<StaffKpi> daftarKpi = staffKpiDao.findAllByStaffAndKpiCategory(s, tazkiaValueCategory);
        List<StaffKpi> daftarKpiIndividual = staffKpiDao.findAllByStaffAndKpiCategory(s, individualCategory);

        for (StaffKpi skp : daftarKpi) {
            String pilihan = request.getParameter(skp.getKpi().getId() + "-score");
            System.out.println("Pilihan : " + pilihan);
            Optional<Indicators> indicators = indicatorsDao.findById(pilihan);
            if (indicators != null) {
                Score score = new Score();
                score.setStaffKpi(skp);
                score.setScore(indicators.get().getScore());
                score.setRemark(indicators.get().getContent());
                float val = Float.parseFloat(score.getStaffKpi().getKpi().getWeight());
                float scoree = Float.parseFloat(score.getScore());
                LOGGER.info("perkalian 1         " + val);
                LOGGER.info(" perkalian 2          " + scoree);
                LOGGER.info("hasilnya         " + val * scoree);
                float perkalian = val * scoree;
                String hasile = String.valueOf(perkalian);
                score.setTotal(hasile);
                scoreDao.save(score);
            }

        }

        for (StaffKpi sk : daftarKpiIndividual) {
            String pilihan = request.getParameter(sk.getKpi().getId() + "-score");

            System.out.println("Pilihan : " + pilihan);
            Optional<Indicators> indicators = indicatorsDao.findById(pilihan);
            if (indicators != null) {
                Score score = new Score();
                score.setStaffKpi(sk);
                score.setScore(indicators.get().getScore());
                score.setRemark(indicators.get().getContent());
                float val = Float.parseFloat(score.getStaffKpi().getKpi().getWeight());
                float scoree = Float.parseFloat(score.getScore());
                LOGGER.info("perkalian 1         " + val);
                LOGGER.info(" perkalian 2          " + scoree);
                LOGGER.info("hasilnya         " + val * scoree);
                float perkalian = val * scoree;
                String hasile = String.valueOf(perkalian);
                score.setTotal(hasile);
                scoreDao.save(score);
            }


        }*/
        return "redirect:list";
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

        Optional<Staff> atasan = staffDao.findById(p.getId());

        if (StringUtils.hasText(id)) {
            m.addAttribute("nama", id);
            m.addAttribute("detail", staffDao.findById(id));
            m.addAttribute("superior", atasan);
        } else {
            m.addAttribute("detailStaff", staffDao.findAll(page));
        }


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

    /*@GetMapping("/uploaded/{evidence}/bukti/")
    public ResponseEntity<byte[]> tampilkanBuktiPembayaran(@PathVariable Evidence evidence) throws Exception {
        String lokasiFile = uploadFolder + File.separator + evidence.getStaffKpi().getStaff().getId()
                + File.separator + evidence.getFileName();
        LOGGER.debug("Lokasi file bukti : {}", lokasiFile);

        try {
            HttpHeaders headers = new HttpHeaders();
            if (evidence.getFileName().toLowerCase().endsWith("jpeg") || evidence.getFileName().toLowerCase().endsWith("jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (evidence.getFileName().toLowerCase().endsWith("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (evidence.getFileName().toLowerCase().endsWith("pdf")) {
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
    }*/

    @GetMapping("/daftarbawahan/evidence/list")
    public void evidence(Model model, Authentication currentUser) {
        LOGGER.debug("username" + currentUser.getClass().getName());

        if (currentUser == null) {
            LOGGER.warn("Current user is null");
            return;
        }

        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);

        if (u == null) {
            LOGGER.warn("Username {} not found in database " + username);
            return;
        }

        Staff p = staffDao.findByUser(u);

        if (p == null) {
            LOGGER.info("Employee not found for username {} " + username);
            return;

        }


        Set<StaffRole> staffRole = p.getRoles();
        for (StaffRole role : staffRole){
            Set<Kpi> kpis = role.getKpi();
            for (Kpi kpi : kpis){
                System.out.println("kpinya :" + kpi.getKeyResult());
            }
            model.addAttribute("individual",kpis);

        }



    }

    @GetMapping("/daftarbawahan/evidence/form")
    public String inputEvidence(String id, Model m) {
        Kpi kpi = kpiDao.findById(id).get();
        if (id == null) {
            return "redirect:/404";
        }

        m.addAttribute("kpi", kpi);

        Evidence evidence = new Evidence();
        return "/daftarbawahan/evidence/form";

    }

    @PostMapping("/daftarbawahan/evidence/form")
    public String uploadBukti(@RequestParam String id, MultipartFile file, Authentication currentUser) throws Exception {
        Kpi kpi = kpiDao.findById(id).get();
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
        evidence.setFilename(idFile);
        evidence.setDescription("Upload Bukti Karyawan");
        evidenceDao.save(evidence);


        return "redirect:list";

    }


}

