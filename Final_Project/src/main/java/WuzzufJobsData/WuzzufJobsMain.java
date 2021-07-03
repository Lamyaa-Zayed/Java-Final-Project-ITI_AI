/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WuzzufJobsData;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import joinery.DataFrame;
/**
 *
 * @author lamya
 */
public class WuzzufJobsMain {
    
    //private static final String COMMA_DELIMITER = ",";
    
    public static void main(String[] args) throws IOException {
        
        WuzzufJobsDAO pDAO = new WuzzufJobsDAO();
        
        DataFrame<Object> UpdatedDataFrame = pDAO.getDataFrameFromFile("src/main/resources/Wuzzuf_Jobs.csv");
        UpdatedDataFrame.drop("YearsExpNumeric").writeCsv("src/main/resources/Wuzzuf_Jobs_Updated.csv");
        
//        List<WuzzufData> x = pDAO.getDataFromFile("src/main/resources/Wuzzuf_Jobs.csv");
//        for (WuzzufData x1 : x) {
//             System.out.println(x1.toString());
//        }
    }
        
//        WuzzufJobsMain xChart = new WuzzufJobsMain ();
//        List<WuzzufJobsDAO> Data = (List<WuzzufJobsDAO>) xChart.getDataFromFile ();
//        System.out.println(Data);

//        JavaRDD<String> dataFromFile = getDataFromFile();
//        System.out.println(dataFromFile);
//        }
//    
//    public JavaRDD<String> getDataFromFile(){
        /*
        Logger.getLogger ("org").setLevel (Level.ERROR);
        // CREATE SPARK CONTEXT
        SparkConf conf = new SparkConf ().setAppName ("WuzzufJobs").setMaster ("local[3]");
        JavaSparkContext sparkContext = new JavaSparkContext (conf);
        // LOAD DATASETS
        JavaRDD<String> Data = sparkContext.textFile ("src/main/resources/Wuzzuf_Jobs.csv");
        // TRANSFORMATIONS
        JavaRDD<String> titles = Data
                .map (WuzzufJobsMain::extractTitle)
                .filter (StringUtils::isNotBlank);
       // JavaRDD<String>
        JavaRDD<String> words = titles.flatMap (title -> Arrays.asList (title
                .toLowerCase ()
                .trim ()
                .replaceAll ("\\p{Punct}", " ")
               .split (" ")).iterator ());
        System.out.println(words.toString());
        // COUNTING
        Map<String, Long> wordCounts = words.countByValue ();
        List<Map.Entry> sorted = wordCounts.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        // DISPLAY
        for (Map.Entry<String, Long> entry : sorted) {
            System.out.println (entry.getKey () + " : " + entry.getValue ());
        }
//        return Data;
//
    }
        public static String extractTitle(String videoLine) {
            try {
                return videoLine.split (COMMA_DELIMITER)[2];
                } catch (ArrayIndexOutOfBoundsException e) {
                    return "";
                }
            }*/
}
