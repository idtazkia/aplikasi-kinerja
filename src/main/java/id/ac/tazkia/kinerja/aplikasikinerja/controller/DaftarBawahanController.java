package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.constants.CategoryConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.KpiTerisi;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.RekapPengisianKpi;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StatusPengisian;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import id.ac.tazkia.kinerja.aplikasikinerja.helper.RekapKpiHelper;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

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

       /* model.addAttribute("subordinate", staffDao.findByRolesAndStatus(staffRole,AktifConstants.Aktif));
        model.addAttribute("roles",staffRole);
        System.out.println(staffRole.getRoleName());*/


        Integer jumlahKpi = staffRole.getKpi().size();
        model.addAttribute("jmlKpi",jumlahKpi);

        Periode p = periodeDao.getCurrentPeriod(StatusKpi.AKTIF, LocalDate.now());
        List<KpiTerisi> hasil = evidenceDao.findKpiTerisi(p);
        Map<Staff, Long> jumlahKpiTerisi = RekapKpiHelper.hitungJumlahKpiTerisi(hasil);

        List<RekapPengisianKpi> rekap = new ArrayList<>();
        staffDao.findByRolesAndStatus(staffRole,AktifConstants.Aktif).forEach(staff -> {
            RekapPengisianKpi r = new RekapPengisianKpi();
            r.setId(staff.getId());
            r.setNama(staff.getEmployeeName());
            r.setArea(staff.getArea());
            r.setNamaRole(staffRole.getRoleName());
            r.setDepartment(staff.getDepartment());
            r.setJumlahKpi(jumlahKpi.longValue());
            r.setJumlahKpiTerisi(jumlahKpiTerisi.get(staff));
            rekap.add(r);
        });
        model.addAttribute("roles",staffRole);
        System.out.println(jumlahKpiTerisi.size());


        model.addAttribute("listSubordinate",rekap);
        return "daftarbawahan/list";

    }

    @GetMapping("/daftarbawahan/form")
    public String daftarKpi(@RequestParam  String id,String staff, Model m, Pageable page, Authentication currentUser) {
        StaffRole staffRole = staffRoleDao.findById(id).get();
        Staff s = staffDao.findById(staff).get();
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);
        Iterable<Score> existingScore = scoreDao.findByStaffAndPeriode(s, periodeDao.findByStatus(StatusKpi.AKTIF));

        Map<String, BigInteger> kpiScore = new HashMap<>();
        for(Score score : existingScore){
            kpiScore.put(score.getKpi().getId(), score.getScore());
        }

        m.addAttribute("kpiScore",kpiScore);

        m.addAttribute("kpi",staffRole.getKpi());
        m.addAttribute("staff",s);
        m.addAttribute("role",staffRole);


        return "daftarbawahan/form";

    }

    @ModelAttribute("daftarIndicators")
    public Iterable<Indicators> daftarIndicators() {
        return indicatorsDao.findAll();
    }


    @PostMapping(value = "/daftarbawahan/form")
    public String prosesForm(@RequestParam String id, String staff, HttpServletRequest request, RedirectAttributes attributes) {
        Staff s = staffDao.findById(staff).get();
        StaffRole roles = staffRoleDao.findById(id).get();
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);
        List<Score> sd = scoreDao.findByStaffAndPeriode(s,periode);

        Map<Kpi, String> errorMessage = new HashMap<>();
        List<String> indikatorYangDipilih = new ArrayList<>();
        Set<Kpi> daftarKpiKaryawan = roles.getKpi();


        Map<String, BigInteger> mapNilaiKpi = new HashMap<>();
        for(Kpi kpi : daftarKpiKaryawan) {
            String pilihan = request.getParameter(kpi.getId() + "-score");
            if (pilihan == null) {
                errorMessage.put(kpi, "harus diisi");
            }else {
                Indicators indicators = indicatorsDao.findById(pilihan).get();
                mapNilaiKpi.put(kpi.getId(), indicators.getScore());
                if (scoreDao.findByStaffAndPeriodeAndKpi(s,periode,indicators.getKpi()).isEmpty()){
                    Score score = new Score();
                    score.setKpi(kpi);
                    score.setScore(indicators.getScore());
                    score.setRemark(indicators.getContent());
                    score.setPeriode(periode);
                    score.setStaff(s);
                    scoreDao.save(score);
                }
            }


        }

        List<Score> daftarScoreYangDiinputSebelumnya = sd;
        for(Score scorenya : daftarScoreYangDiinputSebelumnya){
            String pilihan = request.getParameter(scorenya.getKpi().getId() + "-score");
            if (pilihan != null) {
                    Indicators indicators = indicatorsDao.findById(pilihan).get();
                    scorenya.setScore(mapNilaiKpi.get(scorenya.getKpi().getId()));
                    scorenya.setStaff(s);
                    scorenya.setPeriode(periode);
                    scorenya.setRemark(indicators.getContent());
                    scorenya.setKpi(scorenya.getKpi());
                    scorenya.setId(scorenya.getId());
                    scoreDao.save(scorenya);
            }
        }



        if(errorMessage.isEmpty()) {
            return "redirect:/daftarbawahan/nilai?staff=" +s.getId();
        } else {
            attributes.addFlashAttribute("selectedIndicators", indikatorYangDipilih);
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/daftarbawahan/form?id="+roles.getId()+"&staff="+s.getId();
        }

    }

    @GetMapping("/daftarbawahan/detail")
    public void detail(@RequestParam Staff id,Model model){
       model.addAttribute("detail",id);


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
    public void evidenceList(@RequestParam StaffRole role, Model m, Authentication currentUser){
        Periode periode = periodeDao.findByStatus(StatusKpi.AKTIF);

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

        m.addAttribute("individual",role.getKpi());
        m.addAttribute("role",role);
        m.addAttribute("staff",p);

        List<StatusPengisian> rekap = new ArrayList<>();
        for (Kpi kpi : role.getKpi()){
            List<Evidence> evidence = evidenceDao.findByKpiAndStaffAndPeriode(kpi,p,periode);
            StatusPengisian statusPengisian = new StatusPengisian();
            statusPengisian.setKpi(kpi);
            for (Evidence ev : evidence){
                if (ev.getKpi() == statusPengisian.getKpi()){
                    statusPengisian.setStatus("DONE");
                }

            }
            rekap.add(statusPengisian);
            m.addAttribute("status",rekap);
        }

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
        return "daftarbawahan/evidence/form";

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

    @GetMapping("/daftarbawahan/role")
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
        u.getRole().getId();


        if (p == null) {
            LOGGER.warn("Staff not found for username {} " + username);
            return "redirect:/404";
        }


        Set<StaffRole> daftarRoleBawahan = staffRoleDao.findDistinctBySuperiorRoleIn(p.getRoles());

        model.addAttribute("role", daftarRoleBawahan);
        model.addAttribute("detail", p);

        return "daftarbawahan/role";


    }

    @GetMapping("/daftarbawahan/nilai")
    public void nilai(Model model,@RequestParam Staff staff){
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);
        model.addAttribute("s",staff);
        model.addAttribute("individual",scoreDao.findByStaffAndKpiCategoryAndPeriode(staff,individualCategory,periode));
        model.addAttribute("tazkia",scoreDao.findByStaffAndKpiCategoryAndPeriode(staff,tazkiaValueCategory,periode));
    }
}

