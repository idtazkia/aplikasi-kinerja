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
import java.time.LocalDateTime;
import java.util.Optional;
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

        model.addAttribute("individual", scoreDao.findByStaffAndKpiCategoryAndPeriode(p, individualCategory,periode));
        model.addAttribute("tazkiaValue", scoreDao.findByStaffAndKpiCategoryAndPeriode(p, tazkiaValueCategory,periode));

    }

    @GetMapping("/lihatpenilaian/comment")
    public void detailComment(@RequestParam(required = false) Score id, Model m, Authentication currentUser) {
        if (id == null){
            LOGGER.debug("tidak ada data");
        }

        if (id != null) {
            m.addAttribute("score", id);
            BigDecimal perkalian = id.getKpi().getWeight().multiply(new BigDecimal(id.getScore()));
            m.addAttribute("total",perkalian);
        }

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

        Staff staff = staffDao.findByUser(u);

        m.addAttribute("staff",staff);


    }

    @RequestMapping(value = "/lihatpenilaian/total", method = RequestMethod.GET)
    public void tampilkanFormTotal(Model model){

    }


    @PostMapping("/lihatpenilaian/comment")
    public String proses(@ModelAttribute @Valid ScoreComment scoreComment, @RequestParam String id,
                         MultipartFile[] fileBukti,Authentication currentUser,Model model) throws Exception {
        Score score = scoreDao.findById(id).get();
        model.addAttribute("score",score);

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
            String lokasiUpload = uploadFolder + File.separator + score.getStaff().getId();
            LOGGER.debug("Lokasi upload : {}", lokasiUpload);
            new File(lokasiUpload).mkdirs();
            File tujuan = new File(lokasiUpload + File.separator + idFile + "." + extension);
            upload.transferTo(tujuan);
            LOGGER.debug("File sudah dicopy ke : {}", tujuan.getAbsolutePath());

            Periode periode = periodeDao.findByActive(AktifConstants.Aktif);

            Evidence evidence = new Evidence();
            evidence.setDescription("Score Comment");
            evidence.setKpi(score.getKpi());
            evidence.setFilename(idFile + "." + extension);
            evidence.setStaff(score.getStaff());
            evidence.setPeriode(periode);
            evidenceDao.save(evidence);

        }
        scoreCommentDao.save(scoreComment);

        return "redirect:/lihatpenilaian/list";

    }

}
