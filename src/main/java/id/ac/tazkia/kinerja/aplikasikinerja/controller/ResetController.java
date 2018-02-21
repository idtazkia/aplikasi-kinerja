package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.ResetDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserPasswordDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.ResetPassword;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.UserPassword;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @Autowired
    private UserPasswordDao userPasswordDao;
    @Autowired private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public void tampilkanForm(){}

    @RequestMapping(value = "/reset-sukses", method = RequestMethod.GET)
    public void sukses(){}

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public void error(){}



    @GetMapping(value = "/confirm")
    public String confirm(@RequestParam String code,Model m) {
        ResetPassword resetPassword = resetDao.findByCode(code);

        if (resetPassword == null){
            return "redirect:/404";
        }

        if (code != null && !code.isEmpty()){
            UserPassword userPassword= userPasswordDao.findOne(resetPassword.getUser().getId());
            if (userPassword != null){
                m.addAttribute("confirm", userPassword);
                System.out.println("as" + userPassword);
            }

        }

        if(resetPassword.getExpired().isBefore(LocalDate.now())){
            return "redirect:404";
        }

        m.addAttribute("code", code);
        return "/confirm";
    }



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

    @PostMapping("/confirm")
    public String updatePassword(@RequestParam String code,
                                 @RequestParam String password){


        ResetPassword resetPassword = resetDao.findByCode(code);


        UserPassword up = userPasswordDao.findByUser(resetPassword.getUser());
        if(up == null){
            LOGGER.info("User tidak ditemukan :" + up);
            up = new UserPassword();
            up.setUser(resetPassword.getUser());
        }
        up.setPassword(passwordEncoder.encode(password));


        userPasswordDao.save(up);
        return "redirect:login";


    }


}
