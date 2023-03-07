import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;

@ExtendWith(Extension.class)
@CucumberOptions(
    features = {"src/test/resources/fotmob1/fotmob.feature"},
        glue = {"StepDefinitions"},
        tags = "@All",
        plugin = {
            "pretty",
            "json:target/myreports/report.json",
            "junit:target/myreports/report.xml",
            "html:target/myreports/report.html"
        },
        monochrome = true
)
public class fotmob1 {
}
