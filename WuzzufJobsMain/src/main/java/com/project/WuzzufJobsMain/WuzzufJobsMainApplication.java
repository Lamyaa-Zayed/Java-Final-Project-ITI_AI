package com.project.WuzzufJobsMain;

import java.io.IOException;
import org.knowm.xchart.BitmapEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author lamya
 */
@SpringBootApplication
@RestController
@RequestMapping(path = "api/lamyaa")

public class WuzzufJobsMainApplication {

	public static void main(String[] args) 
        {
            SpringApplication.run(WuzzufJobsMainApplication.class, args);
            
	}

    @GetMapping(path = "hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) 
    {
        return String.format ("Hello %s!", name);
    }
    
    @GetMapping(path = "test")
    public String getData() 
    {
        WuzzufData DAO = new WuzzufData("1","2","3","4","5","6","7","8");
        return DAO.toString();
    }/*
    @GetMapping(path = "hello3")
    public String getData2() {
        WuzzufJobsPOJO pDAO1 = new WuzzufJobsPOJO();
        //WuzzufJobsDAO DAO = new WuzzufJobsPOJO("src/main/resources/Wuzzuf_Jobs.csv");
        List<WuzzufData> x = pDAO1.getDataFromFile("src/main/resources/Wuzzuf_Jobs.csv");
        for (WuzzufData x1 : x) {
             System.out.println(x1.toString());
        }
    return x.toString();
    }*/
    
    private WuzzufJobsPOJO pDAO = new WuzzufJobsPOJO("src/main/resources/Wuzzuf_Jobs.csv");
    @GetMapping(path = "read")
    public StringBuilder readData() 
    {
        //<button onclick="location.href='http://localhost:8082/api/lamyaa/some'" type="button"> www.example.com</button>
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>Welcome To Wuzzaf Project</h1>");
        builder.append("<p><button onclick=\"location.href='http://localhost:8082/api/lamyaa/some'\" type=\"button\"> Read Sample from Data</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:8082/api/lamyaa/clean'\" type=\"button\"> Get Data Cleaned</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:8082/api/lamyaa/summary'\" type=\"button\"> Get Data Summary</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:8082/api/lamyaa/struct'\" type=\"button\"> Get Data Structure</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:8082/api/lamyaa/jobVsComp'\" type=\"button\"> Get the most demanding Companies for Jobs</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:8082/api/lamyaa/title'\" type=\"button\"> Get the most popular job Titles</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:8082/api/lamyaa/area'\" type=\"button\"> Get the most popular Areas</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:8082/api/lamyaa/skills'\" type=\"button\"> Get the most demanding Skills</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:8082/api/lamyaa/factorize'\" type=\"button\"> Factorize the YearsExp feature</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:8082/api/lamyaa/bartitle'\" type=\"button\"> Bar-chart for the most popular job Titles</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:8082/api/lamyaa/bararea'\" type=\"button\"> Bar-Chart for the most popular Areas</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:8082/api/lamyaa/piecompany'\" type=\"button\"> Pie-Chart for the most demanding Companies for Jobs</button></p>");
        builder.append("<br><br><h1>Done By Lamyaa Zayed</h1>");
        return builder;
    }
    
    @GetMapping(path = "struct")
    public String getDataStructure() 
    {
        return pDAO.getDataStructure();
    }
    
    @GetMapping(path = "some")
    public String getSomeData() 
    {
        return pDAO.getSomeData();
    }
    
    @GetMapping(path = "summary")
    public String getDataSummary() 
    {
        return pDAO.getSummary();
    }
    
    @GetMapping(path = "clean")
    public String getDataClean() 
    {
        return pDAO.getDataClean();
    }
    
    @GetMapping(path = "jobVsComp")
    public String getJobForEachComp() 
    {
        return pDAO.getJobsForEachCompany();
    }
    
    @GetMapping(path = "title")
    public String getJobForEachTitle() 
    {
        return pDAO.getJobsForEachTitle();
    }
    
    @GetMapping(path = "area")
    public String getJobForEachLocation() 
    {
        return pDAO.getJobsForEachArea();
    }
    
    @GetMapping(path = "skills")
    public String getDemandingSkills() 
    {
        return pDAO.getDemandingSkills();
    }
    
    @GetMapping(path = "bartitle")
    public StringBuilder getBarChartForTitle() throws IOException 
    {
        String imageNameAndPath = "/images/TitleBarChart.png";
        try{
            BitmapEncoder.saveBitmap(pDAO.getBarChartForTitlesVsJobs(8),"src/main/resources/static"+ imageNameAndPath, BitmapEncoder.BitmapFormat.PNG);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>Titles Bar Chart</h1>");
        builder.append("<img src='").append(imageNameAndPath).append("'/>");
        return builder;
        //return new java.io.File(".").getCanonicalPath();
        // return "<img src=\""+(new java.io.File(".").getCanonicalPath())+"/"+imageNameAndPath+"\" alt=\"TitleBarChart\">";
    }
    
    @GetMapping(path = "bararea")
    public StringBuilder getBarChartForAreas() throws IOException 
    {
        String imageNameAndPath = "/images/AreasBarChart.png";
        try{
            BitmapEncoder.saveBitmap(pDAO.getBarChartForAreasVsJobs(8),"src/main/resources/static"+ imageNameAndPath, BitmapEncoder.BitmapFormat.PNG);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>Areas Bar Chart</h1>");
        builder.append("<img src='").append(imageNameAndPath).append("'/>");
        return builder;
    }
    
    @GetMapping(path = "piecompany")
    public StringBuilder getPieChartForCompany() throws IOException 
    {
        String imageNameAndPath = "/images/CompanyPieChart.png";
        try{
            BitmapEncoder.saveBitmap(pDAO.getPieChartForJobsVsCompanies(8),"src/main/resources/static"+ imageNameAndPath, BitmapEncoder.BitmapFormat.PNG);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>Companies Pie Chart</h1>");
        builder.append("<img src='").append(imageNameAndPath).append("'/>");
        return builder;
        }
    
    @GetMapping(path = "factorize")
    public String factorizeYrsExp() throws IOException 
    {
        return pDAO.factorizeYrsOfExp();
    }
    
}
