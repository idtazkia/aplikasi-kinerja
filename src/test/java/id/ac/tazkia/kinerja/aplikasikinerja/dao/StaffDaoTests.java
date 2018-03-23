package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StaffDaoTests {

    @Autowired
    private StaffDao staffDao;

    @Test
    public void testAmbilBawahan() {

        String id = "001";
        Staff atasan = staffDao.findOne(id);
        Assert.assertNotNull(atasan);

        List<Staff> daftarBawahan = staffDao.test(atasan);
        Assert.assertNotNull(daftarBawahan);

        System.out.println("Atasan : " + atasan.getEmployeeName());
        for (Staff bawahan : daftarBawahan) {
            System.out.println("Nama : " + bawahan.getEmployeeName());
        }
    }
}
