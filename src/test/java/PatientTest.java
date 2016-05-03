import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PatientTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctor_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM patients *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void patient_instantiatesCorrectly() {
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    assertEquals(true, newPatient instanceof Patient);
  }

  @Test
  public void getName_returnsCorrectName_kevin() {
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    assertEquals("Kevin", newPatient.getName());
  }

  @Test
  public void getBirthday_returnsCorrectBirthday_kevin() {
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    assertEquals("12/16/2016", newPatient.getBirthday());
  }

  @Test
  public void getDoctorId_returnsCorrectDoctorId_kevin() {
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    assertEquals(0, newPatient.getDoctorId());
  }

  @Test
  public void all_returnsAllPatients() {
    Patient firstPatient = new Patient("Ryan", "12/07/1988");
    Patient secondPatient = new Patient("Kevin", "12/16/2017");
    firstPatient.save();
    secondPatient.save();
    assertEquals(Patient.all().size(), 2);
  }

  @Test
  public void save_assignsIdtoObject_true() {
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    newPatient.save();
    Patient savedPatient = Patient.all().get(0);
    assertEquals(newPatient.getId(), savedPatient.getId());
  }

  @Test
  public void find_returnsCorrectPatient_true(){
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    newPatient.save();
    Patient foundPatient = Patient.find(newPatient.getId());
    assertTrue(newPatient.equals(foundPatient));
  }

  @Test
  public void saveDoctorId_assignsIdToObject_true() {
    Patient newPatient = new Patient("Kevin", "12/16/2016");
    newPatient.save();
    newPatient.saveDoctorId(1);
    assertEquals(1, newPatient.getDoctorId());
  }
}
