import java.util.List;
import org.sql2o.*;
import java.util.Arrays;

public class Doctor {
  private String doctor_name;
  private String speciality;
  private int id;

  public Doctor(String name, String speciality){
    this.speciality = speciality;
    doctor_name = name;
  }

  public String getName() {
    return doctor_name;
  }

  public String getSpeciality() {
    return speciality;
  }

  public int getId(){
    return id;
  }

  public static List<Doctor> all() {
    String sql = "SELECT id, doctor_name, speciality FROM doctors";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
             this.getSpeciality().equals(newDoctor.getSpeciality()) &&
             this.getId() == newDoctor.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors(doctor_name, speciality) VALUES (:name, :speciality)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.doctor_name)
        .addParameter("speciality", this.speciality)
        .executeUpdate()
        .getKey();
    }
  }

  public static Doctor find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors where id=:id";
      return con.createQuery(sql)
       .addParameter("id", id)
       .executeAndFetchFirst(Doctor.class);
    }
  }

  public List<Patient> allMyPatients() {
    String sql = "SELECT id, patient_name, birthday FROM patients WHERE doctor_id=" + this.getId();
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }
}
