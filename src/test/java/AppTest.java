import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.sql2o.*; // for DB support
import org.junit.*; // for @Before and @After

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctor_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTasksQuery = "DELETE FROM doctors *;";
      String deleteCategoriesQuery = "DELETE FROM patients *;";
      con.createQuery(deleteTasksQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
    }
  }

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Doctor");
  }

  @Test
  public void doctorIdIsSavedToPatient() {
    Doctor newDoctor = new Doctor("thisdoctor", "thisspecialty");
    newDoctor.save();
    Patient newPatient = new Patient("thispatient", "00/00/0000");
    newPatient.save();
    String categoryPath = String.format("http://localhost:4567/patientInfo/%d", newPatient.getId());
    goTo(categoryPath);
    browser.$("#"+newDoctor.getId()).click();
    submit(".btn");
    assertThat(pageSource()).contains("thisdoctor");
  }

}
