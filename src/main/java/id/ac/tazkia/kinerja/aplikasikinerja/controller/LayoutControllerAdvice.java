package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.UserDao;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class LayoutControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(LayoutControllerAdvice.class);

    @Autowired private UserDao userDao;
    @Autowired private StaffDao staffDao;

    @ModelAttribute("currentUser")
    public Map<String, Object> currentUser(Authentication auth){
        Map<String, Object> currentUser = new HashMap<>();

        if(auth != null) {
            LOGGER.debug("Authentication Object : {}", auth);
            User user = userDao.findByUsername(auth.getName());
            Staff staff = staffDao.findByUser(user);
            currentUser.put("staff", staff);
        }

        return currentUser;
    }
}
