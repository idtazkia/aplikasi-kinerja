package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.EvidenceDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.ScoreDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Evidence;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
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
    private EvidenceDao evidenceDao;
    @Value("${upload.folder}")
    private String uploadFolder;


    @GetMapping("/lihatpenilaian/list")
    public void list(Model model, Authentication currentUser){
        LOGGER.debug("username" + currentUser.getClass().getName());

        if(currentUser == null){
            LOGGER.warn("Current user is null");
            return;
        }

        String username = ((UserDetails)currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);

        if(u == null){
            LOGGER.warn("Username {} not found in database " + username);
            return;
        }

        Staff p = staffDao.findByUser(u);


        if(p == null){
            LOGGER.info("Employee not found for username {} " + username);
            return;
        }

        model.addAttribute("individual",scoreDao.findByStaffKpiStaffIdAndStaffKpiKpiCategoryId(u.getId(),"001"));
        model.addAttribute("tazkiaValue",scoreDao.findByStaffKpiStaffIdAndStaffKpiKpiCategoryId(u.getId(),"002"));

    }

    @GetMapping("/lihatpenilaian/comment")
    public void detailComment(@RequestParam(required = false)String id, Model m,Pageable page) {
        Score score = scoreDao.findOne(id);
            if (score != null){
                m.addAttribute("score",scoreDao.findById(id,page));
                m.addAttribute("score", score);
            }

        Evidence evidence = new Evidence();
        m.addAttribute("evidence", evidence);


    }

    @PostMapping("/lihatpenilaian/comment")
    public String proses(@ModelAttribute @Valid Score score, MultipartFile fileBukti)throws Exception {
        String idEmployee = score.getStaffKpi().getStaff().getId();

        String namaFile = fileBukti.getName();
        String jenisFile = fileBukti.getContentType();
        String namaAsli = fileBukti.getOriginalFilename();
        Long ukuran = fileBukti.getSize();

        LOGGER.debug("Nama File : {}", namaFile);
        LOGGER.debug("Jenis File : {}", jenisFile);
        LOGGER.debug("Nama Asli File : {}", namaAsli);
        LOGGER.debug("Ukuran File : {}", ukuran);

//        memisahkan extensi
        String extension = "";

        int i = namaAsli.lastIndexOf('.');
        int p = Math.max(namaAsli.lastIndexOf('/'), namaAsli.lastIndexOf('\\'));

        if (i > p) {
            extension = namaAsli.substring(i+1);
        }

        String idFile = UUID.randomUUID().toString();
        String lokasiUpload = uploadFolder+ File.separator + idEmployee;
        LOGGER.debug("Lokasi upload : {}",lokasiUpload);
        new File(lokasiUpload).mkdirs();
        File tujuan = new File(lokasiUpload + File.separator + idFile + "." + extension);
        fileBukti.transferTo(tujuan);
        LOGGER.debug("File sudah dicopy ke : {}",tujuan.getAbsolutePath());

        Evidence evidence = new Evidence();
        evidence.setScore(score);
        evidence.setFileName(idFile+"."+extension);
        evidenceDao.save(evidence);

        scoreDao.save(score);
        return "redirect:/lihatpenilaian/list";

    }
}
