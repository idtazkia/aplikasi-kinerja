package id.ac.tazkia.kinerja.aplikasikinerja.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class DaftarBawahanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaftarBawahanController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private StaffKpiDao staffKpiDao;

    @Autowired
    private IndicatorsDao indicatorsDao;

    @Autowired
    private ScoreDao scoreDao;

    @Value("${upload.folder}")
    private String uploadFolder;

    @RequestMapping("/daftarbawahan/list")
    public void  daftarStaff(Model model,String user, Authentication currentUser)throws Exception{
        System.out.println("username" + currentUser.getClass().getName());

        if(currentUser == null){
            System.out.println("Current user is null");
            return;
        }

        String username = ((UserDetails)currentUser.getPrincipal()).getUsername();
        User u = userDao.findByUsername(username);

        if(u == null){
            System.out.println("Username {} not found in database " + username);
            return;
        }

        Staff p = staffDao.findByUser(u);


        if(p == null){
            System.out.println("Pendaftar not found for username {} " + username);
            return;
        }

        model.addAttribute("list", staffDao.findAllBySuperiorIdOrderByEmployeeName(p.getId()));


    }

    @RequestMapping("/daftarbawahan/form")
    public void  daftarKpi(String id, Model m, Pageable page){

        m.addAttribute("individual", staffKpiDao.findAllByStaff_IdAndKpi_Category_Id(id,"001"));
        m.addAttribute("tazkiaValue", staffKpiDao.findAllByStaff_IdAndKpi_Category_Id(id,"002"));

    }

    @GetMapping("/daftarbawahan/detail")
    public void detail(@RequestParam(required = false)String id, Model m, Pageable page) {
        if (StringUtils.hasText(id)) {
            m.addAttribute("nama", id);
            m.addAttribute("detailStaff", staffDao.findById(id, page));
        } else {
            m.addAttribute("detailStaff", staffDao.findAll(page));
        }

    }

    @GetMapping("/daftarbawahan/komen")
    public void komens(@RequestParam(required = true)String id, Model m){

        m.addAttribute("individual",scoreDao.findByStaffKpiStaffIdAndStaffKpiKpiCategoryId(id,"001"));
        m.addAttribute("tazkiaValue",scoreDao.findByStaffKpiStaffIdAndStaffKpiKpiCategoryId(id,"002"));


    }

    @GetMapping("/uploaded/{evidence}/bukti/")
    public ResponseEntity<byte[]> tampilkanBuktiPembayaran(@PathVariable Evidence evidence) throws Exception {
        String lokasiFile = uploadFolder + File.separator + evidence.getScore().getStaffKpi().getStaff().getId()
                + File.separator + evidence.getFileName();
        LOGGER.debug("Lokasi file bukti : {}",lokasiFile);

        try {
            HttpHeaders headers = new HttpHeaders();
            if(evidence.getFileName().toLowerCase().endsWith("jpeg") || evidence.getFileName().toLowerCase().endsWith("jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if(evidence.getFileName().toLowerCase().endsWith("png")){
                headers.setContentType(MediaType.IMAGE_PNG);
            }else if(evidence.getFileName().toLowerCase().endsWith("pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            }else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            byte[] data = Files.readAllBytes(Paths.get(lokasiFile));
            return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        } catch (Exception err){
            LOGGER.warn(err.getMessage(), err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
