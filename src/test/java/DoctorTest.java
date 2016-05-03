import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DoctorTest {
  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctor_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM doctors *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void doctor_instantiatesCorrectly() {
    Doctor newDoctor = new Doctor("Kevin", "Arthritis");
    assertEquals(true, newDoctor instanceof Doctor);
  }

  @Test
  public void getName_returnsCorrectName() {
    Doctor newDoctor = new Doctor("Ryan", "Pharmaceuticals");
    assertEquals("Ryan", newDoctor.getName());
  }

  @Test
  public void getSpeciality_returnsCorrectSpeciality() {
    Doctor newDoctor = new Doctor("Ryan", "Pharmaceuticals");
    assertEquals("Pharmaceuticals", newDoctor.getSpeciality());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Doctor.all().size(), 0);
  }

  @Test
  public void all_takesDoctors_2() {
    Doctor firstDoctor = new Doctor("Ryan", "Pharmaceuticals");
    Doctor secondDoctor = new Doctor("Kevin", "Arthritis");
    firstDoctor.save();
    secondDoctor.save();
    assertEquals(Doctor.all().size(), 2);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame_true() {
    Doctor firstDoctor = new Doctor("Ryan", "Pharmaceuticals");
    Doctor secondDoctor = new Doctor("Ryan", "Pharmaceuticals");
    assertEquals(true, firstDoctor.equals(secondDoctor));
  }

  @Test
  public void save_assignsIdtoObject_true() {
    Doctor newDoctor = new Doctor("Ryan", "Whatever");
    newDoctor.save();
    Doctor savedDoctor = Doctor.all().get(0);
    assertEquals(newDoctor.getId(), savedDoctor.getId());
  }

  @Test
  public void find_returnsCorrectDoctor_true(){
    Doctor newDoctor = new Doctor("Kevin", "Whatever");
    newDoctor.save();
    Doctor foundDoctor = Doctor.find(newDoctor.getId());
    assertTrue(newDoctor.equals(foundDoctor));
  }
}
