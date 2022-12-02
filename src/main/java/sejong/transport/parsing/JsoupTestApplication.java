package sejong.transport.parsing;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class JsoupTestApplication {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:/Users/jaehoon/Desktop/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);

        try{
            driver.get("https://map.kakao.com/?map_type=TYPE_MAP&target=walk&rt=516568%2C1124891%2C516673%2C1124865&rt1=%EC%84%B8%EC%A2%85%EB%8C%80%ED%95%99%EA%B5%90+%EC%A0%95%EB%AC%B8&rt2=%EC%96%B4%EB%A6%B0%EC%9D%B4%EB%8C%80%EA%B3%B5%EC%9B%90%EC%97%AD+7%ED%98%B8%EC%84%A0+1%EB%B2%88%EC%B6%9C%EA%B5%AC");
            WebElement temp = driver.findElement(By.xpath("//*[@id=\'view.mapContainer\']/div[2]"));
            String map_html = temp.getAttribute("innerHTML");
            System.out.println("temp = " + map_html);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}