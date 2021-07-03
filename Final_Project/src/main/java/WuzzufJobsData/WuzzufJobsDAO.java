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
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import static org.apache.hadoop.io.MapFile.Reader.comparator;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import scala.Int;
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
    private List<WuzzufData> JobsData = new ArrayList<WuzzufData>();
    
    public List<WuzzufData> getDataFromFile(String filepath) {
//        List<String> a = new ArrayList<String>();
            
        try{
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String line = "";
                int i = 0;
                while((line=br.readLine())!=null){
//                    a.add(line);
                    if(i >= 1){
                        String[] row =  getCSVFields(line);//.split(",(?=(?:[^\"]\"[^\"]\")[^\"]$)", -1);
                        
                        if(row.length >= 8)
                        {
                            if(!row[0].isEmpty() && !row[1].isEmpty() && !row[2].isEmpty() && !row[3].isEmpty() && !row[4].isEmpty() && !row[5].isEmpty() && !row[6].isEmpty() && !row[7].isEmpty())
                            {
                                JobsData.add(new WuzzufData((row[0]), row[1], row[2], row[3], row[4], row[5], row[6], row[7]));

                                //System.out.println(",[i]:"+i+",[0]:"+(row[0]));
                            }
                            else
                            {
                                System.out.println("Row #"+i+" has empty cell");
                            }
                        }
                        else
                        {
                            System.out.println("Row #"+i+" hasn't 8 fileds ");
                            for (int x=0;x<row.length;x++) {
                                System.out.print("Field["+x+"] "+ row[x] + " ,");
                            }
                            }
                        }

                    i++;

                }
                br.close();

        }
        catch(Exception e){
            System.out.println("Error happen!");
            System.out.println(e);
            e.printStackTrace();
        }
        return JobsData;
        
    }

    private String[] getCSVFields(String CSVLine) {
        String[] CSVFields;
        if(!CSVLine.contains("\""))
        {
            //return CSVLine.split(",");
        }
        else
        {
            boolean betweenQuotation=false;
            for(int x=0;x<CSVLine.length();x++)
            {
                if(CSVLine.charAt(x) == '\"')
                {
                    betweenQuotation = !betweenQuotation;
                }
                if((CSVLine.charAt(x) == ',')&&(betweenQuotation))
                {
                    CSVLine = CSVLine.substring(0, x-1) + '*' + CSVLine.substring(x+1);
                }
            }
            //return CSVLine.split(",");
        }
        CSVFields = CSVLine.split(",");
        for (int x=0;x< CSVFields.length ; x++)
        {
            CSVFields[x] = CSVFields[x].replace("\"", "").replace("*",",");
        }
        return CSVFields;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DataFrame<Object> getDataFrameFromFile(String filepath) {
        DataFrame<Object>  df = null;
        //List<Integer> range = IntStream.rangeClosed(0, 49).boxed().collect(Collectors.toList());

        try {
            df= DataFrame.readCsv(filepath)
                    .retain("Title","Company","Location","Type","Level","YearsExp","Country","Skills");
            for(int x=0;x<df.length();x++)
            {
                JobsData.add(new WuzzufData(df.get(x,0).toString(), df.get(x,1).toString(), df.get(x,2).toString(), df.get(x,3).toString(), df.get(x,4).toString(), df.get(x,5).toString(), df.get(x,6).toString(), df.get(x,7).toString())) ;
            }
            // .describe();
            //System.out.println (df.toString ());
            //DataFrame<Object> describe =df.head(50).add("values", range).retain("values").describe();
            //System.out.println(describe.toString());
            //df.groupBy(row ->row.get(2)).iterrows ().forEachRemaining (System.out::println);
            
            System.out.println ("=========================================================================================");
            System.out.println ("Displaying Some of Wuzzuf Jobs data: \n");
            System.out.println (df.head(10));    //some of data to print
            
            System.out.println ("=========================================================================================");
            System.out.println ("Displaying Summary of Wuzzuf Jobs data: \n");
            List<Object> TitlesList = df.col("Title").stream().distinct().collect(Collectors.toList());//;.collect(Collectors.toList());
            TitlesList.remove(0);
            System.out.println ("Wuzzuf Jobs Data has "+ (TitlesList.size()) + " Job Titles : ");
            for (Object x1 : TitlesList) {
             System.out.print(x1.toString()+", ");
            }
            Object[] CompaniesList = df.col("Company").stream().distinct().toArray();//;.collect(Collectors.toList());
            System.out.println ("\n\nWuzzuf Jobs Data has "+ (CompaniesList.length-1) + " Job Companies : ");
            
            for (Object x1 : CompaniesList) {
             System.out.print(x1.toString()+", ");
            }
            System.out.println ("\n\nJobs Locations are : ");
            Object[] LocationsList = df.col("Location").stream().distinct().toArray();//;.collect(Collectors.toList());
            for (Object x1 : LocationsList) {
             System.out.print(x1.toString()+", ");
            }
            System.out.println ("\n\nJob Years of Experiences are : ");
            Object[] YearsExpsList = df.col("YearsExp").stream().distinct().toArray();//;.collect(Collectors.toList());
            for (Object x1 : YearsExpsList) {
             System.out.print(x1.toString()+", ");
            }
            
            System.out.println ("=========================================================================================");
            System.out.println ("\nWuzzuf Jobs data structure is : ");
            System.out.print("Number of Features are: "+df.size()+",  Number of Observations are: "+(df.col("Title").stream().count()-1)+"\n");
            
            System.out.println ("=========================================================================================");
            System.out.println ("\nCleaning the Data:");
            System.out.println("Data records after clean: "+ (JobsData.stream().distinct().count()-1));
            //List<Object> MyList = 
            //df.flatten().stream().distinct().limit(10).forEach(System.out::println);//.collect(Collectors.toList());
            //System.out.print("Number of Features are: "+MyList.get(0).toString().split(",").length+",  Number of Observations are: "+(MyList.size()-1)+"\n");
            
            System.out.println ("=========================================================================================");
            System.out.println ("\nCounting the jobs for each company and display that in order : ");
            //Map<String, Integer> CompaniesJobs = new HashMap<>();
            class DictClass {
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
            List<DictClass> CompaniesJobs = new ArrayList<DictClass>();
            for (Object x1 : CompaniesList) {
                long JobCount =JobsData.stream().filter(d ->d.getCompany().equals(x1)).map(WuzzufData::getTitle).count();
                CompaniesJobs.add(new DictClass(x1.toString(),JobCount));
                //System.out.println("Company: "+x1.toString()+" has "+JobsData.stream().filter(d ->d.getCompany().equals(x1)).map(WuzzufData::getTitle).count()+" Jobs");
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
            new SwingWrapper (chart).displayChart ();
           
            // List<Object> MyList = df.flatten();
           //for (WuzzufData x1 : JobsData) {
            // System.out.println(x1.toString());
             // }
            //System.out.print(df.retain("Title","Company").groupBy(row ->row.get(0)).unique("Title").count());
            //df.forEach(JobsData.add(new WuzzufData(col(0), col(1), col(2), col(3), col(4), col(5), col(6), col(7)))) ;
            
            System.out.println ("=========================================================================================");
            System.out.println ("\nWhat are it the most popular job titles : ");
            
            List<DictClass> TitleCount = new ArrayList<DictClass>();
            for (Object x1 : TitlesList) {
                long JobCount =JobsData.stream().filter(d ->d.getTitle().equals(x1)).map(WuzzufData::getTitle).count();
                TitleCount.add(new DictClass(x1.toString(),JobCount));
            }
            TitleCount = TitleCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
            for (DictClass x1 : TitleCount) {
                System.out.println("Title: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
            }
            
            //System.out.println(TitleCount.stream().flatMapToLong(num -> LongStream.of(Long.parseLong(num.toString()))).max());
            
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
            new SwingWrapper (chart2).displayChart ();
            
            System.out.println ("=========================================================================================");
            System.out.println ("\nThe most popular areas : ");
            List<DictClass> AreasCount = new ArrayList<DictClass>();
            for (Object x1 : LocationsList) {
                long JobCount =JobsData.stream().filter(d ->d.getLocation().equals(x1)).map(WuzzufData::getLocation).count();
                AreasCount.add(new DictClass(x1.toString(),JobCount));
            }
            AreasCount = AreasCount.stream().sorted(Comparator.comparingLong(DictClass::getValue).reversed()).collect(Collectors.toList());
            for (DictClass x1 : AreasCount) {
                System.out.println("Area: "+x1.getKey()+" has "+x1.getValue()+" Jobs");
            }
            
            //System.out.println(TitleCount.stream().flatMapToLong(num -> LongStream.of(Long.parseLong(num.toString()))).max());
            
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
            new SwingWrapper (chart3).displayChart ();
            
            System.out.println ("=========================================================================================");
            System.out.println("\nThe most important skills required: ");
            List<DictClass> SkillsCount = new ArrayList<DictClass>();
            List<String> UniqueSkills = new ArrayList<String>();
            List<String> JobSkills = new ArrayList<String>();
            
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
            
            //df.col("YearsExp").stream().map(d -> d.toString().replaceAll("Yrs of Exp", "")).sorted().forEach(System.out::println);
            df.add("YearsExpNumeric", df.col("YearsExp").stream().map(d -> d.toString().replaceAll("Yrs of Exp", "")).collect(Collectors.toList()));
            
            
            
            List<ExperianceYears> YearsExpDetails= new ArrayList<ExperianceYears>();
            df.col("YearsExpNumeric").stream().forEach(x -> YearsExpDetails.add(parseYearsString(x)));
            df.add("MinYearsExp", YearsExpDetails.stream().map(ExperianceYears::getMin_Years).collect(Collectors.toList()));
            df.add("MaxYearsExp", YearsExpDetails.stream().map(ExperianceYears::getMax_Years).collect(Collectors.toList()));
            
            System.out.println(df.head(50));
            
            System.out.println ("=========================================================================================");
            System.out.println("Displaying Summary of Data for The years of Experiance:");
            System.out.println(df.retain("MinYearsExp","MaxYearsExp").describe());
            
            System.out.println ("=========================================================================================");
            System.out.println("Web Service:");
            
            
            System.out.println ("=========================================================================================");
            
        } catch (IOException e) {
            e.printStackTrace ();
        }/*
         val rddFromFile = spark.sparkContext.textFile(filepath)
        return df;*//*
        Table wuzzufData;
        WuzzufJobsDAO wjd = new WuzzufJobsDAO ();
          try {
            wjd.wuzzufData = wjd.loadDataFromCVS (wjd.filepath);
            //getting the Structure of the data
            String structure = wjd.getDataInfoStructure (wjd.wuzzufData);
            System.out.println (structure);
            //getting Data summery
            String summary = wjd.getDataSummary (wjd.wuzzufData);
            System.out.println (summary);
          }
          catch (IOException e) {
            e.printStackTrace ();
        }*/
        return df;

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
