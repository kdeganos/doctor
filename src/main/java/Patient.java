import java.util.List;
import org.sql2o.*;
import java.util.Arrays;

public class Patient {
  private String patient_name;
  private String birthday;
  private int id;
  private int doctor_id = 0;

  public Patient(String name, String birthday){
    patient_name = name;
    this.birthday = birthday;
  }

  public String getName() {
    return patient_name;
  }

  public String getBirthday() {
    return birthday;
  }

  public int getDoctorId() {
    return doctor_id;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherPatient) {
    if (!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getName().equals(newPatient.getName()) &&
             this.getBirthday().equals(newPatient.getBirthday()) &&
             this.getDoctorId() == newPatient.getDoctorId();
    }
  }

  public static List<Patient> all() {
    String sql = "SELECT id, patient_name, doctor_id, birthday FROM patients";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients(patient_name, birthday, doctor_id) VALUES (:patient_name, :birthday, :doctor_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("doctor_id", doctor_id)
        .addParameter("patient_name", patient_name)
        .addParameter("birthday", birthday)
        .executeUpdate()
        .getKey();
    }
  }

  public void saveDoctorId(int id) {
    doctor_id = id;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patients SET doctor_id = :doctor_id WHERE id=:id";
      con.createQuery(sql, true)
        .addParameter("doctor_id", doctor_id)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }



  public static Patient find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where id=:id";
      return con.createQuery(sql)
       .addParameter("id", id)
       .executeAndFetchFirst(Patient.class);
    }
  }

}
