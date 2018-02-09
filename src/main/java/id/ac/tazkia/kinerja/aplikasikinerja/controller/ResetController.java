package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.ResetDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.ResetPassword;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;


@Controller
public class ResetController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetController.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private ResetDao resetDao;

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public void tampilkanForm(){}

    @RequestMapping(value = "/reset-sukses", method = RequestMethod.GET)
    public void sukses(){}

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public void confirm(){}

    @PostMapping(value = "/reset")
    public String reset(@Valid @RequestParam String user){
        User u = userDao.findByUsername(user);

        if(u == null){
            LOGGER.info("Reset password untuk username belum terdaftar : {}", user);
            return "redirect:reset-sukses";
        }

        ResetPassword rp = resetDao.findByUser(u);

        if(rp == null){
            rp = new ResetPassword();
            rp.setUser(u);
        }

        rp.setCode(RandomStringUtils.randomAlphabetic(6));
        rp.setExpired(LocalDate.now().plusDays(3));

        resetDao.save(rp);
        return "redirect:reset-sukses";
    }

}
