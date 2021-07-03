/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WuzzufJobsData;


import java.io.IOException;
import joinery.DataFrame;
import org.knowm.xchart.SwingWrapper;
/**
 *
 * @author lamya
 */
public class WuzzufJobsMain {
    
    //private static final String COMMA_DELIMITER = ",";
    
    public static void main(String[] args) throws IOException {
        
        WuzzufJobsDAO pDAO = new WuzzufJobsDAO("src/main/resources/Wuzzuf_Jobs.csv");
        
        DataFrame<Object> UpdatedDataFrame = pDAO.getDataFrameFromFile();
        UpdatedDataFrame.drop("YearsExpNumeric").writeCsv("src/main/resources/Wuzzuf_Jobs_Updated.csv");
        System.out.println("Displaying Some of Data:");    
        System.out.println(pDAO.GetSomeData());
        System.out.println("Displaying Structure of Data:");    
        System.out.println(pDAO.GetDataStructure());
        System.out.println("Displaying Summary of Data for The years of Experiance:");            
        System.out.println(pDAO.GetSummary());
        System.out.println("Displaying Jobs For Each Company:");            
        System.out.println(pDAO.GetJobsForEachCompany());
        new SwingWrapper (pDAO.GetPieChartForJobsVsCompanies(10)).displayChart ();
        System.out.println("Displaying Jobs For Each Title:");            
        System.out.println(pDAO.GetJobsForEachTitle());
        new SwingWrapper (pDAO.GetBarChartForTitlesVsJobs(10)).displayChart ();
        System.out.println("Displaying Jobs For Each Area:");            
        System.out.println(pDAO.GetJobsForEachArea());
        new SwingWrapper (pDAO.GetBarChartForAreasVsJobs(10)).displayChart ();
        System.out.println("Displaying Most Demanding Skills:");            
        System.out.println(pDAO.GetDemandingSkills());
    }
        
}
