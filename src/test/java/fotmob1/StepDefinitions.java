package fotmob1;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinitions {

    // Chromium driver vs Chrome mismatch
//    System.setProperty("webdriver.chrome.driver", "C:\\Users\\kindi\\selenium_webdrivers\\chromedriver_win32_110\\chromedriver.exe");
    WebDriver webDriver = new FirefoxDriver();

    boolean goal_diff_matches = false;
    boolean points_match = false;
    boolean teams_match = false;
    boolean played_match = false;


    @Given("User goes to https:\\/\\/fotmob.com")
    public void user_goes_to_https_fotmob_com() throws Exception {
        try {

            webDriver.get("https://fotmob.com");
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            By google = By.xpath("//*[@id='credential_picker_container']");
            WebElement ggl = webDriver.findElement(google);
            // Need to access the iframe. What a PITA!
            WebElement iframe = ggl.findElement(By.cssSelector("iframe[title='Sign in with Google Dialog']"));
            webDriver.switchTo().frame(iframe);
            // This needs serious work. Wayyy to hard coded.
            webDriver.findElement(By.xpath("/html/body/div/div/div/div/div/following-sibling::div")).click();
            webDriver.switchTo().defaultContent();

        } catch (Exception ex) {
            String message = ex.getMessage();
            throw new Exception(message);
        }
    }

    @When("User selects the Premier League")
    public void user_selects_the_premier_league() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        try {

            String pl_link_path = "/leagues/47/overview/premier-league";
            String pl_xpath = "//a[@href='" + pl_link_path + "']";
//            By linkText = By.linkText(pl_link_path);
            By xpath = By.xpath(pl_xpath);
            WebElement element = webDriver.findElement(xpath);
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            if (element.isDisplayed()) {
                System.out.println(element.isSelected());
                // Received Unable to click on element at (460, 316) hijinx
                Actions actions = new Actions(webDriver);
                actions.moveToElement(element).click().build().perform();
                element.click();
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            throw new Exception(message);
        }
    }

    @When("User examines a team")
    public void user_examines_a_team(DataTable dataTable) throws Exception {

//        String test_team = "Arsenal";
        List<String> team_list = dataTable.asList(String.class);

        int wins = 0;
        int draws = 0;
        int losses = 0;
        int points = 0;
        int plus = 0;
        int minus = 0;
        int goal_diff = 0;
        int played = 0;

        try {
            WebElement standings_table = webDriver.findElement(By.cssSelector(".Table"));

            // Wins, Draws, Losses, Points, Games Played, Goals For - Goals Against, Goal Differential
            List<WebElement> headers = standings_table.findElements(By.xpath(".//th"));

            for (String test_team : team_list) {
                By xpath = By.xpath("//*[text()[contains(., '" + test_team + "')]]");
                WebElement the_team = standings_table.findElement(xpath);
                WebElement team_name_el = the_team.findElement(By.xpath("//*/parent::tr/td/.."));
                List<WebElement> values = team_name_el.findElements(By.xpath(".//td"));

                String team_name_temp = new String(the_team.getAttribute("innerHTML").toCharArray());
                String team_name = StringEscapeUtils.unescapeHtml4(team_name_temp);

                int n_headers = headers.size();
//                if (n_headers != values.size()) {
//                    // Throw I guess
//                }
                for (int ii = 0; ii < n_headers - 1; ii++) {
                    String head = new String(headers.get(ii).getText().toCharArray());
                    switch(head) {
                        case "W":
                            wins = Integer.parseInt(new String(values.get(ii).getText().toCharArray()));
                            break;
                        case "D":
                            draws = Integer.parseInt(new String(values.get(ii).getText().toCharArray()));
                            break;
                        case "L":
                            losses = Integer.parseInt(new String(values.get(ii).getText().toCharArray()));
                            break;
                        case "PTS":
                            points = Integer.parseInt(new String(values.get(ii).getText().toCharArray()));
                            break;
                        case "+/-":
                            String temp = new String(values.get(ii).getText().toCharArray());
                            String[] expected = temp.split("-");
                            plus = Integer.parseInt(expected[0]);
                            minus = Integer.parseInt(expected[1]);
                            break;
                        case "GD":
                            goal_diff = Integer.parseInt(new String(values.get(ii).getText().toCharArray()));
                            break;
                        case "PL":
                            played = Integer.parseInt(new String(values.get(ii).getText().toCharArray()));
                            break;
                    }
                }

                if (plus - minus == goal_diff) {
                    goal_diff_matches = true;
                }
                if (3 * wins + draws == points) {
                    points_match = true;
                }
                if(wins + draws + losses == played){
                    played_match = true;
                }
                if(team_name.equals(test_team)){
                    teams_match = true;
                }
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            throw new Exception(message);
        }
    }

    @Then("the teams points are correct")
    public void the_teams_points_are_correct() {
        // Write code here that turns the phrase above into concrete actions

        assertTrue(points_match);
        assertTrue(played_match);
        assertTrue(teams_match);
        assertTrue(goal_diff_matches);

        webDriver.close();
    }

}
