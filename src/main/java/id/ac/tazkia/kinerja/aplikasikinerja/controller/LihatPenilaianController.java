package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.constants.CategoryConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.*;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.LihatScoreDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Controller
public class LihatPenilaianController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LihatPenilaianController.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private PeriodeDao periodeDao;
    @Autowired
    private EvidenceDao evidenceDao;
    @Autowired
    private ScoreCommentDao scoreCommentDao;
    @Autowired
    private StaffRoleDao staffRoleDao;
    @Value("${upload.folder}")
    private String uploadFolder;

    private Category individualCategory;
    private Category tazkiaValueCategory;

    public LihatPenilaianController() {
        individualCategory = new Category();
        individualCategory.setId(CategoryConstants.INDIVIDUAL_INDICATOR_ID);

        tazkiaValueCategory = new Category();
        tazkiaValueCategory.setId(CategoryConstants.TAZKIA_INDICATOR_ID);
    }


    @GetMapping("/lihatpenilaian/list")
    public void list(Model model, Authentication currentUser) {
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

        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

        model.addAttribute("staff",p);
        model.addAttribute("periode",periode);
        model.addAttribute("individual", scoreDao.findByStaffAndKpiCategoryAndPeriode(p, individualCategory,periode));
        model.addAttribute("tazkiaValue", scoreDao.findByStaffAndKpiCategoryAndPeriode(p, tazkiaValueCategory,periode));

    }

    @GetMapping("/lihatpenilaian/comment")
    public void detailComment(@RequestParam(required = false) Score id, Model m,Authentication currentUser) {
        LOGGER.debug("Authentication class : {}", currentUser.getClass().getName());

        if (currentUser == null) {
            LOGGER.warn("Current user is null");
            return;
        }

        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);
        LOGGER.debug("User ID : {}", u.getId());
        if (u == null) {
            LOGGER.warn("Username {} not found in database ", username);
            return;
        }

        Staff p = staffDao.findByUser(u);
        LOGGER.debug("Nama Pendaftar : " + p.getEmployeeName());
        if (p == null) {
            LOGGER.warn("Pendaftar not found for username {} ", username);
        }


        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

        ScoreComment sc = new ScoreComment();
        sc.setScore(id);
        sc.setAuthor(p);
        sc.setPeriode(periode);
        m.addAttribute("scoreComment", sc);

        if (id == null){
            LOGGER.debug("tidak ada data");
        }

        if (id != null) {
            m.addAttribute("score", id);
            BigDecimal perkalian = id.getKpi().getWeight().multiply(new BigDecimal(id.getScore()));
            m.addAttribute("total",perkalian);
            m.addAttribute("periode",periode);
            ScoreComment scoreComment = scoreCommentDao.findByAuthorAndPeriodeAndScore(p,periode,id);
            if (scoreComment != null){
                m.addAttribute("scoreComment",scoreComment);
            }
        }


    }

    @RequestMapping(value = "/lihatpenilaian/total", method = RequestMethod.GET)

    public void tampilkanFormTotal(@RequestParam Staff staff, @RequestParam Periode periode, Model model){
        List<LihatScoreDto> score = scoreDao.pisahkanScore(staff, periode);
        List<LihatScoreDto> scoreAkhir = scoreDao.totalAkhir(staff, periode);


        model.addAttribute("score", score);
        model.addAttribute("scoreAkhir",scoreAkhir);


//        String username = ((UserDetails) currentUser.getPrincipal()).getUsername();
//        User u = userDao.findByUsername(username);
//        u.getRole().getId();
//        Staff p = staffDao.findByUser(u);
//        List<Staff> cariRole = staffDao.findByStatusAndRoles(AktifConstants.Aktif,p.getRoles());
//        System.out.println("ROLENYA "  + cariRole  );
//        model.addAttribute("detail", p.getRoles());





//        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);
//        List<Score> individualScore = scoreDao.findByStaffAndKpiCategoryAndPeriode(staff,individualCategory,periode);
//        List<Score> tazkiaScore = scoreDao.findByStaffAndKpiCategoryAndPeriode(staff,tazkiaValueCategory,periode);
//        List<Score> totalAkhir = scoreDao.findByStaffAndPeriode(staff,periode);
//
//        BigDecimal totalIndividual = BigDecimal.ZERO;
//        BigDecimal totalTazkia = BigDecimal.ZERO;
//        BigDecimal scoreAkhir = BigDecimal.ZERO;
//        for (Score s : individualScore){
//            totalIndividual = totalIndividual.add(s.getKpi().getWeight().multiply(new BigDecimal(s.getScore())));
//        }
//
//        for (Score s : tazkiaScore){
//            totalTazkia = totalTazkia.add(s.getKpi().getWeight().multiply(new BigDecimal(s.getScore())));
//        }
//
//        for (Score s : totalAkhir){
//            scoreAkhir = scoreAkhir.add(s.getKpi().getWeight().multiply(new BigDecimal(s.getScore())));
//        }



//        model.addAttribute("individual",Integer.valueOf(totalIndividual.intValue()));
//        model.addAttribute("tazkia",Integer.valueOf(totalTazkia.intValue()));
//        model.addAttribute("total",Integer.valueOf(scoreAkhir.intValue()));
    }


    @PostMapping("/lihatpenilaian/comment")
    public String proses(@ModelAttribute @Valid ScoreComment scoreComment, @RequestParam Score id,
                         @RequestParam(required = false) String scoreCom, MultipartFile[] fileBukti) throws Exception {
        Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

        if (scoreCom != null && !scoreCom.isEmpty()){
            ScoreComment sc = scoreCommentDao.findById(scoreCom).get();
            scoreCommentDao.delete(sc);
        }

        for (MultipartFile upload : fileBukti) {
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
            String lokasiUpload = uploadFolder + File.separator + id.getStaff().getId();
            LOGGER.debug("Lokasi upload : {}", lokasiUpload);
            new File(lokasiUpload).mkdirs();
            File tujuan = new File(lokasiUpload + File.separator + idFile + "." + extension);
            upload.transferTo(tujuan);
            LOGGER.debug("File sudah dicopy ke : {}", tujuan.getAbsolutePath());

            Evidence evidence = new Evidence();
            evidence.setDescription(scoreComment.getContent());
            evidence.setKpi(id.getKpi());
            evidence.setFilename(idFile + "." + extension);
            evidence.setStaff(id.getStaff());
            evidence.setPeriode(periode);
            evidenceDao.save(evidence);

        }
        scoreComment.setPeriode(periode);
        scoreCommentDao.save(scoreComment);


        return "redirect:/lihatpenilaian/list";

    }

}
