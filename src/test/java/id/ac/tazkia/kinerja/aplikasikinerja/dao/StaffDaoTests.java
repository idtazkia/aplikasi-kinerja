package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StaffDaoTests {

    @Autowired
    private StaffDao staffDao;

    @Autowired private StaffRoleDao staffRoleDao;


    @Test @Ignore
    public void testAmbilRole(){
        Staff staff = staffDao.findById("001").get();
        for(StaffRole roles : staff.getRoles()) {
            System.out.println("Role : "+roles.getRoleName());
        }

        Set<StaffRole> roleBaru = new HashSet<>();
        StaffRole direktur = staffRoleDao.findById("003").get();
        roleBaru.add(direktur);

        StaffRole sekretaris = staffRoleDao.findById("006").get();
        roleBaru.add(sekretaris);

        staff.setRoles(roleBaru);

        staffDao.save(staff);
    }
}
