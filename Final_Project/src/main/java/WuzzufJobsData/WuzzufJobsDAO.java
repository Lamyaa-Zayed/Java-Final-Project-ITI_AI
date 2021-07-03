/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WuzzufJobsData;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.DoubleStream;
//import java.util.stream.Collectors;
//import static java.util.stream.Collectors.toList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import joinery.DataFrame;
import java.io.IOException;
import java.util.Date;
import tech.tablesaw.api.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
/**
 *
 * @author lamya
 */
public class WuzzufJobsDAO {

    public WuzzufJobsDAO(String CSVDataFilePath) {
        try {
            boolean debug=false;
            df= DataFrame.readCsv(CSVDataFilePath)
                    .retain("Title","Company","Location","Type","Level","YearsExp","Country","Skills");
            for(int x=0;x<df.length();x++)
            {
                JobsData.add(new WuzzufData(df.get(x,0).toString(), df.get(x,1).toString(), df.get(x,2).toString(), df.get(x,3).toString(), df.get(x,4).toString(), df.get(x,5).toString(), df.get(x,6).toString(), df.get(x,7).toString())) ;
            }
            if(debug)
            {
                
                System.out.println ("=========================================================================================");
                System.out.println ("Displaying Some of Wuzzuf Jobs data: \n");
                System.out.println (df.head(10));    //some of data to print
                
                System.out.println ("=========================================================================================");
                System.out.println ("Displaying Summary of Wuzzuf Jobs data: \n");
                TitlesList = df.col("Title").stream().distinct().collect(Collectors.toList());//;.collect(Collectors.toList());
                TitlesList.remove(0);
                System.out.println ("Wuzzuf Jobs Data has "+ (TitlesList.size()) + " Job Titles : ");
                for (Object x1 : TitlesList) {
                    System.out.print(x1.toString()+", ");
                }
                
                CompaniesList = df.col("Company").stream().distinct().toArray();//;.collect(Collectors.toList());
                System.out.println ("\n\nWuzzuf Jobs Data has "+ (CompaniesList.length-1) + " Job Companies : ");
                
                for (Object x1 : CompaniesList) {
                    System.out.print(x1.toString()+", ");
                }
                System.out.println ("\n\nJobs Locations are : ");
                
                
                LocationsList = df.col("Location").stream().distinct().toArray();//;.collect(Collectors.toList());
                for (Object x1 : LocationsList) {
                    System.out.print(x1.toString()+", ");
                }
                System.out.println ("\n\nJob Years of Experiences are : ");
                YearsExpsList = df.col("YearsExp").stream().distinct().toArray();//;.collect(Collectors.toList());
                for (Object x1 : YearsExpsList) {
                    System.out.print(x1.toString()+", ");
                }
                
                System.out.println ("=========================================================================================");
                System.out.println ("\nWuzzuf Jobs data structure is : ");
                System.out.print("Number of Features are: "+df.size()+",  Number of Observations are: "+(df.col("Title").stream().count()-1)+"\n");
                
                System.out.println ("=========================================================================================");
                System.out.println ("\nCleaning the Data:");
                System.out.println("Data records after clean: "+ (JobsData.stream().distinct().count()-1));
                System.out.println ("=========================================================================================");
                System.out.println ("\nCounting the jobs for each company and display that in order : ");
                
                for (Object x1 : CompaniesList) {
                    long JobCount =JobsData.stream().filter(d ->d.getCompany().equals(x1)).map(WuzzufData::getTitle).count();
                    CompaniesJobs.add(new DictClass(x1.toString(),JobCount));
                }
                CompaniesJobs = CompaniesJobs.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                for (DictClass x1 : CompaniesJobs) {
                    System.out.println("Company: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
                }
                
                System.out.println("Displaying Pie Chart:");
                // Create Chart
                PieChart chart = new PieChartBuilder ().width (800).height (600).title ("Jobs for each company").build ();
                // Customize Chart
                Color[] sliceColors = new Color[]{new Color (180, 68, 50), new Color (130, 105, 120), new Color (80, 143, 160)};
                chart.getStyler ().setSeriesColors (sliceColors);
                // Series
                int ceil=10;
                for (DictClass x1 : CompaniesJobs) {
                    chart.addSeries (x1.getKey(),x1.getValue());
                    if((ceil--) == 0)
                    {
                        break;
                    }
                }
                ceil=10;
                // Show it
                //new SwingWrapper (chart).displayChart ();
                System.out.println ("=========================================================================================");
                System.out.println ("\nWhat are it the most popular job titles : ");
                
                for (Object x1 : TitlesList) {
                    long JobCount =JobsData.stream().filter(d ->d.getTitle().equals(x1)).map(WuzzufData::getTitle).count();
                    TitleCount.add(new DictClass(x1.toString(),JobCount));
                }
                TitleCount = TitleCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                for (DictClass x1 : TitleCount) {
                    System.out.println("Title: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
                }
                
                
                System.out.println("Displaying the Bar Chart of the most popular job titles :");
                // Create Chart
                CategoryChart chart2 = new CategoryChartBuilder ().width (1024).height (768).title ("The most popular job titles").xAxisTitle ("Job titles").yAxisTitle("Popularity").build ();
                // Customize Chart
                chart2.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
                chart2.getStyler ().setHasAnnotations (true);
                chart2.getStyler ().setStacked (true);
                // Series
                chart2.addSeries ("most popular job titles", TitleCount.stream().map(DictClass::getKey).limit(ceil).collect(Collectors.toList()), TitleCount.stream().map(DictClass::getValue).limit(ceil).collect(Collectors.toList()) );
                // Show it
                //new SwingWrapper (chart2).displayChart ();
                
                System.out.println ("=========================================================================================");
                System.out.println ("\nThe most popular areas : ");
                for (Object x1 : LocationsList) {
                    long JobCount =JobsData.stream().filter(d ->d.getLocation().equals(x1)).map(WuzzufData::getLocation).count();
                    AreasCount.add(new DictClass(x1.toString(),JobCount));
                }
                AreasCount = AreasCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                for (DictClass x1 : AreasCount) {
                    System.out.println("Area: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
                }
                
                
                System.out.println("Displaying the Bar Chart of the most popular Areas :");
                // Create Chart
                CategoryChart chart3 = new CategoryChartBuilder ().width (1024).height (768).title ("The most popular Areas").xAxisTitle ("Job titles").yAxisTitle("Popularity").build ();
                // Customize Chart
                chart3.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
                chart3.getStyler ().setHasAnnotations (true);
                chart3.getStyler ().setStacked (true);
                // Series
                chart3.addSeries ("most popular Areas:", AreasCount.stream().map(DictClass::getKey).limit(ceil).collect(Collectors.toList()), AreasCount.stream().map(DictClass::getValue).limit(ceil).collect(Collectors.toList()) );
                // Show it
                // new SwingWrapper (chart3).displayChart ();
                
                System.out.println ("=========================================================================================");
                System.out.println("\nThe most important skills required: ");
                
                df.col("Skills").forEach(x -> JobSkills.addAll(Arrays.asList(x.toString().split(","))));
                UniqueSkills = JobSkills.stream().distinct().collect(Collectors.toList());
                for (String x1 : UniqueSkills) {
                    long JobCount =JobSkills.stream().filter(d ->d.equals(x1)).count();
                    SkillsCount.add(new DictClass(x1,JobCount));
                }
                SkillsCount = SkillsCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                for (DictClass x1 : SkillsCount) {
                    System.out.println("Skills: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
                }
                
                System.out.println ("=========================================================================================");
                System.out.println("\nFactorize the YearsExp feature and convert it to numbers in new col. (Bounce ): ");
                
                df.add("YearsExpNumeric", df.col("YearsExp").stream().map(d -> d.toString().replaceAll("Yrs of Exp", "")).collect(Collectors.toList()));
                
                
                
                df.col("YearsExpNumeric").stream().forEach(x -> YearsExpDetails.add(parseYearsString(x)));
                df.add("MinYearsExp", YearsExpDetails.stream().map(ExperianceYears::getMin_Years).collect(Collectors.toList()));
                df.add("MaxYearsExp", YearsExpDetails.stream().map(ExperianceYears::getMax_Years).collect(Collectors.toList()));
                
                System.out.println(df.head(50));
                
                System.out.println ("=========================================================================================");
                System.out.println("Displaying Summary of Data for The years of Experiance:");
                System.out.println(df.retain("MinYearsExp","MaxYearsExp").describe());
                
                System.out.println ("=========================================================================================");
                System.out.println("End of Data Represenation:");
                
                
                System.out.println ("=========================================================================================");
            }
            else
            {
                TitlesList = df.col("Title").stream().distinct().collect(Collectors.toList());//;.collect(Collectors.toList());
                TitlesList.remove(0);
                
                CompaniesList = df.col("Company").stream().distinct().toArray();//;.collect(Collectors.toList());
                
                
                LocationsList = df.col("Location").stream().distinct().toArray();//;.collect(Collectors.toList());
                YearsExpsList = df.col("YearsExp").stream().distinct().toArray();//;.collect(Collectors.toList());
                
                for (Object x1 : CompaniesList) {
                    long JobCount =JobsData.stream().filter(d ->d.getCompany().equals(x1)).map(WuzzufData::getTitle).count();
                    CompaniesJobs.add(new DictClass(x1.toString(),JobCount));
                }
                CompaniesJobs = CompaniesJobs.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                
                for (Object x1 : TitlesList) {
                    long JobCount =JobsData.stream().filter(d ->d.getTitle().equals(x1)).map(WuzzufData::getTitle).count();
                    TitleCount.add(new DictClass(x1.toString(),JobCount));
                }
                TitleCount = TitleCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                for (Object x1 : LocationsList) {
                    long JobCount =JobsData.stream().filter(d ->d.getLocation().equals(x1)).map(WuzzufData::getLocation).count();
                    AreasCount.add(new DictClass(x1.toString(),JobCount));
                }
                AreasCount = AreasCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                
                
                df.col("Skills").forEach(x -> JobSkills.addAll(Arrays.asList(x.toString().split(","))));
                UniqueSkills = JobSkills.stream().distinct().collect(Collectors.toList());
                for (String x1 : UniqueSkills) {
                    long JobCount =JobSkills.stream().filter(d ->d.equals(x1)).count();
                    SkillsCount.add(new DictClass(x1,JobCount));
                }
                SkillsCount = SkillsCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                
                
                df.add("YearsExpNumeric", df.col("YearsExp").stream().map(d -> d.toString().replaceAll("Yrs of Exp", "")).collect(Collectors.toList()));
                
                
                
                df.col("YearsExpNumeric").stream().forEach(x -> YearsExpDetails.add(parseYearsString(x)));
                df.add("MinYearsExp", YearsExpDetails.stream().map(ExperianceYears::getMin_Years).collect(Collectors.toList()));
                df.add("MaxYearsExp", YearsExpDetails.stream().map(ExperianceYears::getMax_Years).collect(Collectors.toList()));
                
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    
    private class ExperianceYears{
        private Integer Min_Years;
        private Integer Max_Years;
        
        public ExperianceYears(Integer Min_Years, Integer Max_Years) {
            this.Min_Years = Min_Years;
            this.Max_Years = Max_Years;
        }
        
        public Integer getMin_Years() {
            return Min_Years;
        }
        
        public void setMin_Years(Integer Min_Years) {
            this.Min_Years = Min_Years;
        }
        
        public Integer getMax_Years() {
            return Max_Years;
        }
        
        public void setMax_Years(Integer Max_Years) {
            this.Max_Years = Max_Years;
        }
        
    }
    private class DictClass {
        private String Key;
        private long Value;
        
        public DictClass(String Key, long Value) {
            this.Key = Key;
            this.Value = Value;
        }
        
        public String getKey() {
            return Key;
        }
        
        public void setKey(String Key) {
            this.Key = Key;
        }
        
        public long getValue() {
            return Value;
        }
        
        public void setValue(long Value) {
            this.Value = Value;
        }
        
    }
    private List<WuzzufData> JobsData = new ArrayList<WuzzufData>();
    private DataFrame<Object>  df = null;
    private List<Object> TitlesList = null;
    private Object[] LocationsList = null;
    private Object[] YearsExpsList = null;
    private Object[] CompaniesList = null;
    private List<DictClass> CompaniesJobs = new ArrayList<DictClass>();
    private List<DictClass> TitleCount = new ArrayList<DictClass>();
    private List<ExperianceYears> YearsExpDetails= new ArrayList<ExperianceYears>();
    private List<DictClass> AreasCount = new ArrayList<DictClass>();
    private List<DictClass> SkillsCount = new ArrayList<DictClass>();
    private List<String> UniqueSkills = new ArrayList<String>();
    private List<String> JobSkills = new ArrayList<String>();
    
    public DataFrame<Object> getDataFrameFromFile() {
        
        return df;

    }
    public String GetSummary()
    {
        return df.describe().toString();
    }
    public String GetSomeData()
    {
        return df.head(10).toString();
    }
    public String GetDemandingSkills()
    {
        String result="";
        for (DictClass x1 : SkillsCount) {
            result = result+ ("\nSkills: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
        }
        return result;
    }
    public String GetDataStructure()
    {
        return ("Number of Features are: "+df.size()+",  Number of Observations are: "+(df.col("Title").stream().count()-1)+"\n");
    }
    public String GetJobsForEachCompany()
    {
        String result="";
        for (DictClass x1 : CompaniesJobs) {
                result = result+ ("\nCompany: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
            }
        return result;
    }
    public String GetJobsForEachTitle()
    {
        String result="";
        for (DictClass x1 : TitleCount) {
                result = result+ ("\nTitle: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
            }
        return result;
    }
    public String GetJobsForEachArea()
    {
        String result="";
        for (DictClass x1 : AreasCount) {
                result = result+ ("\nArea: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
            }
        return result;
    }
    public CategoryChart GetBarChartForTitlesVsJobs(Integer limit)
    {
        CategoryChart chart2 = new CategoryChartBuilder ().width (1024).height (768).title ("The most popular job titles").xAxisTitle ("Job titles").yAxisTitle("Popularity").build ();
        // Customize Chart
        chart2.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart2.getStyler ().setHasAnnotations (true);
        chart2.getStyler ().setStacked (true);
        // Series
        chart2.addSeries ("most popular job titles", TitleCount.stream().map(DictClass::getKey).limit(limit).collect(Collectors.toList()), TitleCount.stream().map(DictClass::getValue).limit(limit).collect(Collectors.toList()) );
        // Show it
        //new SwingWrapper (chart2).displayChart ();
        return chart2;
    }
    public CategoryChart GetBarChartForAreasVsJobs(Integer limit)
    {
        CategoryChart chart3 = new CategoryChartBuilder ().width (1024).height (768).title ("The most popular Areas").xAxisTitle ("Job titles").yAxisTitle("Popularity").build ();
        // Customize Chart
        chart3.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart3.getStyler ().setHasAnnotations (true);
        chart3.getStyler ().setStacked (true);
        // Series
        chart3.addSeries ("most popular Areas:", AreasCount.stream().map(DictClass::getKey).limit(limit).collect(Collectors.toList()), AreasCount.stream().map(DictClass::getValue).limit(limit).collect(Collectors.toList()) );
        return chart3;
    }
    public PieChart GetPieChartForJobsVsCompanies(Integer limit)
    {
            // Create Chart
            PieChart chart = new PieChartBuilder ().width (800).height (600).title ("Jobs for each company").build ();
            // Customize Chart
            Color[] sliceColors = new Color[]{new Color (180, 68, 50), new Color (130, 105, 120), new Color (80, 143, 160)};
            chart.getStyler ().setSeriesColors (sliceColors);
            // Series
            for (DictClass x1 : CompaniesJobs) {
                chart.addSeries (x1.getKey(),x1.getValue());
                if((limit--) == 0)
                {
                    break;
                }
            }
            // Show it
            //new SwingWrapper (chart).displayChart ();
            
            return chart;
    }
    private ExperianceYears parseYearsString(Object x) {
        Integer Min=0;
        Integer Max=0;
        try
        {
            if(x.toString().contains("-"))
            {
                String[] temp= x.toString().replace(" ", "").replace("+", "").split("-");
                Min = Integer.parseInt(temp[0]);
                Max = Integer.parseInt(temp[1]);
            }
            else
            {
                Min = Integer.parseInt(x.toString().replace(" ", "").replace("+", ""));
                Max = Min+2;
            }
        }
        catch(Exception e){
            System.out.println("Error happen!");
            System.out.println(e);
            e.printStackTrace();
        }
        
        return new ExperianceYears(Min,Max);
    }
}
