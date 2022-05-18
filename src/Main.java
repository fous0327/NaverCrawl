import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver","./resource/chromedriver");

        Scanner scanner  = new Scanner(new File("./resource/params.txt"));
        String[] searches;
        String line = "";
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
        }
        scanner.close();
        searches = line.split(",");
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        //driver.get("https://search.naver.com/search.naver?where=image&sm=tab_jum&query=" + "수동자연마을 힐링별밤수목원캠핑장" + "#imgId=image_sas%3Ablog155304256%7C8%7C222662899782_2010314647");
        driver.manage().window().setSize(new Dimension(1920,1080));
//        System.out.println(driver.findElements(By.tagName("img")).size());
//        Document document = Jsoup.connect("https://search.naver.com/search.naver?where=image&sm=tab_jum&query=" +"수동자연마을 힐링별밤수목원캠핑장").get();
//        Elements alls = document.getElementsByTag("img");
//        System.out.println(alls.size());
        for(int a = 0; a<searches.length;a++) {
            new File("./output/"+searches[a]).mkdirs();
            FileWriter fw = new FileWriter("./output/result.csv",true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(searches[a]+",");
            bw.newLine();
            bw.close();
            fw.close();
            int count = 0;
            int MAX_LOOP = 200;
            System.out.println(searches[a]);
            driver.get("https://search.naver.com/search.naver?where=image&sm=tab_jum&query=" + searches[a] + "#imgId=image_sas%3Ablog155304256%7C8%7C222662899782_2010314647");
            List<WebElement> elements = new ArrayList<WebElement>();
            boolean flag = true;

            while(flag&&count<MAX_LOOP){
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + 1 * count + ")");
                System.out.println("in");
                elements = driver.findElements(By.tagName("img"));
                //System.out.println("current elements: "+elements.size());
                if(elements.size()>20){
                    flag=false;
                }
                count ++;
            }
            System.out.println("elements: "+elements.size());
            List<String> imgUrl = new ArrayList<String>();
            int count2 = 0;
            for(int i = 0; i<elements.size();i++){
                System.out.println(i);
                String img =  elements.get(i).getAttribute("src");
                System.out.println(img);
                FileWriter fwt = new FileWriter("./output/result.csv",true);
                BufferedWriter bwt = new BufferedWriter(fwt);
                if(img.contains("https://")) {
                    img = img.split("&type")[0];
                    imgUrl.add(img);
                    URL url = new URL(img);
                    BufferedImage image = ImageIO.read(url);
                    if (img.contains("jpg")||img.contains("JPG")) {
                        ImageIO.write(image, "jpg", new File("./output/"+searches[a]+"/" + count2 + ".jpg"));
                        count2++;
                        bwt.write(","+img);
                        bwt.newLine();
                    }
                }
                bwt.close();
                fwt.close();
            }
        }
        System.out.println(searches.length);
//        for(int a = 0; a<searches.length;a++) {
//            System.out.println(searches[a]);
//            Document document = Jsoup.connect("https://search.naver.com/search.naver?where=image&sm=tab_jum&query=" + searches[a] + "#imgId=image_sas%3Ablog155304256%7C8%7C222662899782_2010314647").get();
//            Elements alls = document.getElementsByTag("img");
//            List<String> imgUrl = new ArrayList<String>();
//            System.out.println("query:"+alls.size());
//            for (int i = 0; i < alls.size(); ) {
//                System.out.println(i);
//                String img = alls.get(i).attr("src");
//                img = img.split("&type")[0];
//                imgUrl.add(img);
//                URL url = new URL(img);
//                BufferedImage image = ImageIO.read(url);
//                if (img.contains("jpg")) {
//
//                    ImageIO.write(image, "jpg", new File("./output/"+searches[a]+"/" + i + ".jpg"));
//                    i++;
//                }
//            }
//        }



        //WebElement singleItem = driver.findElement(By.cssSelector("id[class=\"wrap\"]"));
    }
}
