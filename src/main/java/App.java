import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App{
  public static void main(String[] args) {
    String layout = "templates/layout.vtl";

    get("/", (request, response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/addDoctor", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/newDoctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/addDoctor", (request, response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      String doctorName = request.queryParams("doctorName");
      String doctorSpeciality = request.queryParams("doctorSpeciality");
      Doctor newDoctor = new Doctor(doctorName, doctorSpeciality);
      newDoctor.save();
      Boolean addedNewDoctor = true;
      model.put("addedNewDoctor", addedNewDoctor);
      model.put("template", "templates/newDoctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/addPatient", (request, response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/newPatient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/addPatient", (request,response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String patientName = request.queryParams("patientName");
      String patientBirthday = request.queryParams("patientBirthday");
      Patient newPatient = new Patient(patientName, patientBirthday);
      newPatient.save();
      Boolean addedNewPatient = true;
      model.put("addedNewPatient", addedNewPatient);
      model.put("template", "templates/newPatient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/listPatients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/patientList.vtl");
      List<Patient> patientList = Patient.all();
      model.put("patientList", patientList);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patientInfo/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Patient newPatient = Patient.find(Integer.parseInt(request.params(":id")));
      int thisDoctorId = newPatient.getDoctorId();
      Doctor newDoctor = Doctor.find(thisDoctorId);
      List<Doctor> doctorList = Doctor.all();
      model.put("thisDoctor", newDoctor);
      model.put("doctorList", doctorList);
      model.put("thisPatient", newPatient);
      model.put("template", "templates/patientInfo.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/updatePatient/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();;
      int doctorId = Integer.parseInt(request.queryParams("doctorToPatient"));
      Patient newPatient = Patient.find(Integer.parseInt(request.params(":id")));
      Doctor newDoctor = Doctor.find(doctorId);
      newPatient.saveDoctorId(doctorId);

      model.put("thisDoctor", newDoctor);
      model.put("doctorId", doctorId);
      model.put("thisPatient", newPatient);
      model.put("template", "templates/patientInfo.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/listDoctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/doctorList.vtl");
      List<Doctor> doctorList = Doctor.all();
      model.put("doctorList", doctorList);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctorInfo/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor newDoctor = Doctor.find(Integer.parseInt(request.params(":id")));
      List<Patient> allMyPatients = newDoctor.allMyPatients();
      model.put("allMyPatients", allMyPatients);
      model.put("thisDoctor", newDoctor);
      model.put("template", "templates/doctorInfo.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
