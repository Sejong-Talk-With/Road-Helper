package sejong.transport.parsing;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class parsingApplication {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:/Users/jaehoon/Desktop/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);

        try{
            driver.get("https://map.naver.com/v5/directions/14145922.901061576,4515926.292957397,%EC%84%B8%EC%A2%85%EB%8C%80%ED%95%99%EA%B5%90%EC%A0%95%EB%AC%B8,18836862,PLACE_POI/14145973.785200816,4515908.629700193,%EC%96%B4%EB%A6%B0%EC%9D%B4%EB%8C%80%EA%B3%B5%EC%9B%90%EC%97%AD(%EC%84%B8%EC%A2%85%EB%8C%80)1%EB%B2%88%EC%B6%9C%EA%B5%AC,21406223,PLACE_POI/-/walk?c=14145949.1473125,4515917.5195940,20,0,0,0,dh");
            //WebElement temp = driver.findElement(By.xpath("//*[@id=\'view.mapContainer\']/div[2]"));
            WebElement temp = driver.findElement(By.xpath("/html/head"));
            String map_html = driver.getPageSource();
            //String map_html = temp.getAttribute("innerHTML");
            System.out.println("temp = " + map_html);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}