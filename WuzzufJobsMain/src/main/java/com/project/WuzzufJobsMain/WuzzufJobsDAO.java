/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.WuzzufJobsMain;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import smile.data.measure.NominalScale;
import smile.data.vector.IntVector;
import org.apache.commons.csv.CSVFormat;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.style.Styler;
import smile.data.DataFrame;
import smile.io.Read;

import org.knowm.xchart.PieChartBuilder;
import smile.data.vector.StringVector;

/**
 *
 * @author lamya
 */
public class WuzzufJobsDAO {
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
    private List<WuzzufDataPOJO> JobsData = new ArrayList<WuzzufDataPOJO>();
    private DataFrame df = null;
   // private DataFrame wuzzufDataFrame;
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
    
    public WuzzufJobsDAO(String CSVDataFilePath){
        //Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "src/main/resources/WelcomePage.bat"});
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader ();
        //DataFrame df1 = null;
        try {
            df = Read.csv (CSVDataFilePath, format).select("Title","Company","Location","Type","Level","YearsExp","Country","Skills");          
            for(int x=0;x<df.nrows();x++)
            {
                JobsData.add(new WuzzufDataPOJO(df.get(x,0).toString(), df.get(x,1).toString(), df.get(x,2).toString(), df.get(x,3).toString(), df.get(x,4).toString(), df.get(x,5).toString(), df.get(x,6).toString(), df.get(x,7).toString())) ;
               // System.out.println(JobsData.get(x).toString());
            }
                TitlesList = df.stream().map(f -> f.get("Title")).distinct().collect(Collectors.toList());//;.collect(Collectors.toList());
                //TitlesList.remove(0);
                //map(f -> f.toString().replace("{","").replace("}","")).
                CompaniesList = df.stream().map(f -> f.get("Company")).distinct().toArray();//;.collect(Collectors.toList());
                
                
                LocationsList = df.stream().map(f -> f.get("Location")).distinct().toArray();//;.collect(Collectors.toList());
                YearsExpsList = df.stream().map(f -> f.get("YearsExp")).distinct().toArray();//;.collect(Collectors.toList());
                
                for (Object x1 : CompaniesList) {
                    long JobCount =JobsData.stream().filter(d ->d.getCompany().equals(x1)).map(WuzzufDataPOJO::getTitle).count();
                    CompaniesJobs.add(new DictClass(x1.toString(),JobCount));
                }
                CompaniesJobs = CompaniesJobs.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                
                for (Object x1 : TitlesList) {
                    long JobCount =JobsData.stream().filter(d ->d.getTitle().equals(x1)).map(WuzzufDataPOJO::getTitle).count();
                    TitleCount.add(new DictClass(x1.toString(),JobCount));
                }
                TitleCount = TitleCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                for (Object x1 : LocationsList) {
                    long JobCount =JobsData.stream().filter(d ->d.getLocation().equals(x1)).map(WuzzufDataPOJO::getLocation).count();
                    AreasCount.add(new DictClass(x1.toString(),JobCount));
                }
                AreasCount = AreasCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                
                
                df.stream().map(f -> f.get("Skills")).forEach(x -> JobSkills.addAll(Arrays.asList(x.toString().split(","))));
                UniqueSkills = JobSkills.stream().distinct().collect(Collectors.toList());
                for (String x1 : UniqueSkills) {
                    long JobCount =JobSkills.stream().filter(d ->d.equals(x1)).count();
                    SkillsCount.add(new DictClass(x1,JobCount));
                }
                SkillsCount = SkillsCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
                df = df.merge (IntVector.of ("YearsExpEncoded", encodeCategory (df, "YearsExp")));
                df = df.merge (StringVector.of ("YearsExpRange", encodeCategoryRange (df, "YearsExp")));
                df = df.merge (IntVector.of ("YearsExpMin", encodeCategoryMin (df, "YearsExpRange")));
                df = df.merge (IntVector.of ("YearsExpMax", YearsExpDetails.stream().mapToInt(q -> q.getMax_Years())));
        
        } 
        catch (IOException | URISyntaxException e) {
            e.printStackTrace ();
        }
      //  df = df1;
    }

    public DataFrame getDataFrameFromFile() 
    {
        return df;
    }
    
    public String getSummary()
    {
        return df.summary().toString();
    }
    
    public String getSomeData()
    {   
        return df.slice(0, 10).toString();
    }
    
    public String getDataStructure()
    {
        return df.structure().toString();
    }
    
    public String getDataClean()
    {
        return df.omitNullRows().toString();
    }
    
    public String getJobsForEachCompany()
    {
        String result="<h1>Displaying Jobs For Each Company:</h1>";
        for (DictClass x1 : CompaniesJobs) {
            result = result+ ("<p>Company: "+x1.getKey()+" has "+x1.getValue()+" Jobs</p>");
        }
        //System.out.println(result);
        return result;
    }
    
   public String getDemandingSkills()
    {
        String result="<h1>Displaying Most Demanding Skills:</h1>";
        for (DictClass x1 : SkillsCount) {
            result = result+ ("<p>Skills: "+x1.getKey()+" has "+x1.getValue()+" Jobs</p>");
        }
        return result;
    }
   
    public String getJobsForEachTitle()
    {
        String result="<h1>Displaying Jobs For Each Title:</h1>";
        for (DictClass x1 : TitleCount) {
                result = result+ ("<p>Title: "+x1.getKey()+" has "+x1.getValue()+" Jobs</p>");
            }
        return result;
    }
    
    public String getJobsForEachArea()
    {
        String result="<h1>Displaying Jobs For Each Area:</h1>";
        for (DictClass x1 : AreasCount) {
                result = result+ ("<p>Area: "+x1.getKey()+" has "+x1.getValue()+" Jobs</p>");
            }
        return result;
    }
    
    public CategoryChart getBarChartForTitlesVsJobs(Integer limit)
    {
      //WritableImage image = scene.snapshot(null);
      //File file = new File("D:\\JavaFX\\tempPieChart.png");
      //System.out.println("Image Saved");
      
        CategoryChart chart2 = new CategoryChartBuilder ().width (1024).height (768).title ("The most popular job titles").xAxisTitle ("Job titles").yAxisTitle("Popularity").build ();
        chart2.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart2.getStyler ().setHasAnnotations (true);
        chart2.getStyler ().setStacked (true);
        chart2.addSeries ("most popular job titles", TitleCount.stream().map(DictClass::getKey).limit(limit).collect(Collectors.toList()), TitleCount.stream().map(DictClass::getValue).limit(limit).collect(Collectors.toList()) );
        return chart2;
    }
    
    public CategoryChart getBarChartForAreasVsJobs(Integer limit)
    {
        CategoryChart chart3 = new CategoryChartBuilder ().width (1024).height (768).title ("The most popular Areas").xAxisTitle ("Job areas").yAxisTitle("Popularity").build ();
        chart3.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart3.getStyler ().setHasAnnotations (true);
        chart3.getStyler ().setStacked (true);
        chart3.addSeries ("most popular Areas:", AreasCount.stream().map(DictClass::getKey).limit(limit).collect(Collectors.toList()), AreasCount.stream().map(DictClass::getValue).limit(limit).collect(Collectors.toList()) );
        return chart3;
    }
    
    public PieChart getPieChartForJobsVsCompanies(Integer limit)
    {
            PieChart chart = new PieChartBuilder ().width (800).height (600).title ("Jobs for each company").build ();
            Color[] sliceColors = new Color[]{new Color (180, 68, 50), new Color (130, 105, 120), new Color (80, 143, 160)};
            chart.getStyler ().setSeriesColors (sliceColors);
            for (DictClass x1 : CompaniesJobs) {
                chart.addSeries (x1.getKey(),x1.getValue());
                if((limit--) == 0)
                {
                    break;
                }
            }
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
    
    public IntStream encodeCategoryMin(DataFrame df, String columnName) 
    {
        String[] values = df.stringVector(columnName).stream().collect(Collectors.toList()).toArray (new String[]{});
        for (String value : values) {
           YearsExpDetails.add(parseYearsString(value));
       }
        //YearsExpDetails.stream().mapToInt(q -> q.getMin_Years()).forEach(System.out::println);
        return YearsExpDetails.stream().mapToInt(q -> q.getMin_Years());
    }
    /*
    public IntStream encodeCategoryMax(DataFrame df, String columnName) 
    {
        String[] values = df.stringVector(columnName).stream().collect(Collectors.toList()).toArray (new String[]{});
        for (String value : values) {
           YearsExpDetails.add(parseYearsString(value));
       }
        YearsExpDetails.stream().mapToInt(q -> q.getMax_Years()).forEach(System.out::println);
        return YearsExpDetails.stream().mapToInt(q -> q.getMax_Years());
    }*/
    public String[] encodeCategoryRange(DataFrame df, String columnName) 
    {
        return df.stringVector(columnName).stream().map(x -> x.replace("Yrs of Exp", "")).collect(Collectors.toList()).toArray (new String[]{});
    }
    
    public int[] encodeCategory(DataFrame df, String columnName) 
    {
        String[] values = df.stringVector (columnName).distinct ().toArray (new String[]{});
        return df.stringVector (columnName).factorize (new NominalScale (values)).toIntArray ();
            
    }
    
    public String factorizeYrsOfExp()
    {
        //System.out.println("<h1>Factorize Years Of Experience:</h1>");
        
        //df = df.merge (IntVector.of ("YearsExpEncoded", encodeCategory (df, "YearsExp")));
        //df = df.merge (StringVector.of ("YearsExpRange", encodeCategoryRange (df, "YearsExp")));
        //df = df.merge (IntVector.of ("YearsExpMin", encodeCategoryMin (df, "YearsExpRange")));
        //df = df.merge (IntVector.of ("YearsExpMax", YearsExpDetails.stream().mapToInt(q -> q.getMax_Years())));
        //df = df.merge (IntVector.of ("YearsExpMax", encodeCategoryMax (df, "YearsExpRange")));
        //df = df.merge (IntVector.of ("YearsExpMin", YearsExpDetails.stream().mapToInt(q -> q.getMin_Years())));
        
        return df.slice(0, 10).toString();
    }
}
