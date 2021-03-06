package ivory.regression.sigir2013.cdec;

import ivory.core.eval.Qrels;
import ivory.regression.GroundTruth;
import ivory.regression.GroundTruth.Metric;
import ivory.smrf.retrieval.Accumulator;
import ivory.sqe.retrieval.QueryEngine;
import ivory.sqe.retrieval.RunQueryEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import junit.framework.JUnit4TestAdapter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;
import com.google.common.collect.Maps;
import edu.umd.cloud9.collection.DocnoMapping;
import edu.umd.cloud9.io.map.HMapSFW;

public class EnAr_TREC02 {
  private QueryEngine qe;
  private static String PATH = "en-ar.trec02";
  private static String LANGUAGE = "ar";
  private static int numTopics = 50;

  private static float expectedTokenMAP = 0.2714f;
  private static Map<Integer,float[]> expectedMAPs = new HashMap<Integer,float[]>();

  private static Map<Integer, String[]> grammar_AP = new HashMap<Integer, String[]>();
  private static Map<Integer, String[]> Nbest_AP = new HashMap<Integer, String[]>();
  private static Map<Integer, String[]> Onebest_AP = new HashMap<Integer, String[]>();
  private static Map<Integer, String[]> Interp_AP = new HashMap<Integer, String[]>();

  public static void initialize() {
    expectedMAPs.put(0, new float[]{0.3020f, 0.2497f, 0.2475f, 0.2865f});  // "one2none" -> grammar,1best,10best,interp
    expectedMAPs.put(1, new float[]{0.2824f, 0.2489f, 0.2488f, 0.2733f});  // "one2one" -> grammar,1best,10best,interp
    expectedMAPs.put(2, new float[]{0.2931f, 0.2422f, 0.2551f, 0.2779f});  // "one2many" -> grammar,1best,10best,interp
    grammar_AP.put(0, new String[] {
        "35", "0.188","36", "0.0027","33", "0.0244","34", "0.0044","39", "0.3654","37", "0.4053","38", "0.0088","43", "0.0","42", "0.4118","41", "0.441","40", "0.5121","67", "0.6481","66", "0.1932","69", "0.5623","68", "0.261","26", "0.0089","27", "0.3424","28", "0.0091","29", "0.5544","30", "0.6711","32", "0.8569","31", "0.0744","70", "0.5443","71", "0.2286","72", "0.0293","73", "0.2661","74", "0.0638","75", "0.1295","59", "0.7487","58", "0.1522","57", "0.1376","56", "0.4786","55", "0.6413","64", "6.0E-4","65", "0.5615","62", "0.0506","63", "0.107","60", "0.7393","61", "0.0904","49", "0.7584","48", "0.7102","45", "0.3322","44", "0.1777","47", "0.3419","46", "0.3272","51", "0.0713","52", "0.0789","53", "0.0037","54", "0.0493","50", "0.7365"
    });
    grammar_AP.put(1, new String[] {
        "35", "0.1381","36", "0.0027","33", "0.0207","34", "0.0036","39", "0.3417","37", "0.3917","38", "0.0085","43", "0.0","42", "0.4121","41", "0.2775","40", "0.4882","67", "0.6519","66", "0.2299","69", "0.5615","68", "0.2853","26", "0.0082","27", "0.3423","28", "0.0279","29", "0.5465","30", "0.6935","32", "0.0398","31", "0.0706","70", "0.5439","71", "0.2477","72", "0.0282","73", "0.2722","74", "0.0292","75", "0.1607","59", "0.75","58", "0.1223","57", "0.139","56", "0.3901","55", "0.6461","64", "6.0E-4","65", "0.5798","62", "0.0504","63", "0.1069","60", "0.7108","61", "0.091","49", "0.8083","48", "0.7094","45", "0.3536","44", "0.1778","47", "0.3475","46", "0.3026","51", "0.0711","52", "0.1181","53", "0.0064","54", "0.05","50", "0.7627"
    });
    grammar_AP.put(2, new String[] {
        "35", "0.1882","36", "0.0027","33", "0.0284","34", "0.0045","39", "0.3652","37", "0.4054","38", "0.0088","43", "0.0","42", "0.409","41", "0.2199","40", "0.5323","67", "0.6504","66", "0.2093","69", "0.5626","68", "0.2556","26", "0.0097","27", "0.3424","28", "0.0090","29", "0.5544","30", "0.6767","32", "0.4901","31", "0.0779","70", "0.5445","71", "0.2311","72", "0.0303","73", "0.2668","74", "0.0552","75", "0.1331","59", "0.7516","58", "0.1529","57", "0.1376","56", "0.4735","55", "0.6432","64", "6.0E-4","65", "0.6179","62", "0.0499","63", "0.1078","60", "0.7322","61", "0.0478","49", "0.7779","48", "0.7102","45", "0.3465","44", "0.178","47", "0.3434","46", "0.3361","51", "0.0714","52", "0.0789","53", "0.0038","54", "0.0492","50", "0.7801"
    });
    Nbest_AP.put(0, new String[] {
        "35", "0.1409","36", "0.0032","33", "0.023","34", "0.0036","39", "0.2905","37", "0.2489","38", "0.0052","43", "0.0","42", "0.4121","41", "0.0","40", "0.4974","67", "0.5407","66", "0.3193","69", "0.5263","68", "0.1974","26", "0.0142","27", "0.2883","28", "0.0028","29", "0.5233","30", "0.6088","32", "0.0832","31", "0.0743","70", "0.4782","71", "0.0889","72", "0.0038","73", "0.1723","74", "0.0211","75", "0.1259","59", "0.7296","58", "0.0928","57", "0.0488","56", "0.4276","55", "0.6302","64", "6.0E-4","65", "0.4913","62", "0.0698","63", "0.0757","60", "0.7257","61", "0.0706","49", "0.704","48", "0.6716","45", "0.125","44", "0.1796","47", "0.3359","46", "0.3344","51", "0.0825","52", "0.0832","53", "0.0023","54", "0.0352","50", "0.7641" 
    });
    Nbest_AP.put(1, new String[] {
        "35", "0.1409","36", "0.0032","33", "0.0219","34", "0.0036","39", "0.2463","37", "0.2489","38", "0.0052","43", "0.0","42", "0.4121","41", "0.0342","40", "0.4974","67", "0.5846","66", "0.2938","69", "0.5263","68", "0.1974","26", "0.0142","27", "0.2883","28", "0.0119","29", "0.4895","30", "0.6088","32", "0.0635","31", "0.0743","70", "0.4782","71", "0.0889","72", "0.0183","73", "0.1723","74", "0.0286","75", "0.1259","59", "0.7296","58", "0.089","57", "0.0488","56", "0.3467","55", "0.6302","64", "6.0E-4","65", "0.4679","62", "0.0698","63", "0.0757","60", "0.7257","61", "0.0706","49", "0.704","48", "0.6716","45", "0.358","44", "0.1796","47", "0.3359","46", "0.3344","51", "0.0825","52", "0.0832","53", "0.0023","54", "0.0352","50", "0.7206"
    });
    Nbest_AP.put(2, new String[] {
        "35", "0.1409","36", "0.0032","33", "0.0245","34", "0.0036","39", "0.2905","37", "0.2489","38", "0.0052","43", "0.0","42", "0.4121","41", "7.0E-4","40", "0.4974","67", "0.5895","66", "0.3265","69", "0.5263","68", "0.1974","26", "0.0142","27", "0.2883","28", "0.0028","29", "0.5233","30", "0.6088","32", "0.0832","31", "0.0743","70", "0.4782","71", "0.0889","72", "0.0038","73", "0.1723","74", "0.0211","75", "0.1259","59", "0.7296","58", "0.097","57", "0.0488","56", "0.3877","55", "0.6302","64", "6.0E-4","65", "0.6409","62", "0.0698","63", "0.0757","60", "0.7257","61", "0.0706","49", "0.704","48", "0.6716","45", "0.341","44", "0.1796","47", "0.3359","46", "0.3344","51", "0.0825","52", "0.0832","53", "0.0023","54", "0.0352","50", "0.7544"
    });
    Onebest_AP.put(0, new String[] {
        "35", "0.1409","36", "0.0032","33", "0.015","34", "0.0036","39", "0.2904","37", "0.2199","38", "0.0052","43", "0.0","42", "0.4017","41", "0.0","40", "0.4349","67", "0.5407","66", "0.342","69", "0.5263","68", "0.176","26", "0.0176","27", "0.2883","28", "0.0031","29", "0.5233","30", "0.6088","32", "0.0832","31", "0.0743","70", "0.474","71", "0.0889","72", "0.0010","73", "0.1376","74", "0.0221","75", "0.1259","59", "0.7296","58", "0.1442","57", "0.0129","56", "0.4276","55", "0.6427","64", "6.0E-4","65", "0.4599","62", "0.0541","63", "0.0757","60", "0.7003","61", "0.0706","49", "0.704","48", "0.6716","45", "0.3485","44", "0.1796","47", "0.3341","46", "0.3358","51", "0.0825","52", "0.0832","53", "0.0021","54", "0.0339","50", "0.8417"  
    });
    Onebest_AP.put(1, new String[] {
        "35", "0.1409","36", "0.0032","33", "0.023","34", "0.0036","39", "0.2904","37", "0.2199","38", "0.0052","43", "0.0","42", "0.4017","41", "0.0649","40", "0.4349","67", "0.5823","66", "0.342","69", "0.5263","68", "0.176","26", "0.0176","27", "0.2883","28", "0.0031","29", "0.5233","30", "0.6088","32", "0.0484","31", "0.0743","70", "0.474","71", "0.0889","72", "0.0010","73", "0.1376","74", "0.0221","75", "0.1259","59", "0.7296","58", "0.1442","57", "0.0129","56", "0.2688","55", "0.6427","64", "6.0E-4","65", "0.5631","62", "0.0541","63", "0.0757","60", "0.7003","61", "0.0706","49", "0.704","48", "0.6716","45", "0.5262","44", "0.1796","47", "0.3341","46", "0.3358","51", "0.0825","52", "0.0832","53", "0.0021","54", "0.0339","50", "0.6022"
    });
    Onebest_AP.put(2, new String[] {
        "35", "0.1409","36", "0.0032","33", "0.029","34", "0.0036","39", "0.2904","37", "0.2199","38", "0.0052","43", "0.0","42", "0.4017","41", "0.0678","40", "0.4349","67", "0.6033","66", "0.342","69", "0.5263","68", "0.176","26", "0.0176","27", "0.2883","28", "0.0031","29", "0.5233","30", "0.6088","32", "0.0484","31", "0.0743","70", "0.474","71", "0.0889","72", "0.0010","73", "0.1376","74", "0.0221","75", "0.1259","59", "0.7296","58", "0.1442","57", "0.0129","56", "0.0834","55", "0.6427","64", "6.0E-4","65", "0.3971","62", "0.0541","63", "0.0757","60", "0.7003","61", "0.0706","49", "0.704","48", "0.6716","45", "0.5155","44", "0.1796","47", "0.3341","46", "0.3358","51", "0.0825","52", "0.0832","53", "0.0021","54", "0.0339","50", "0.6004"
    });
    Interp_AP.put(0, new String[] {
        "35", "0.176","36", "0.0027","33", "0.0229","34", "0.0044","39", "0.3336","37", "0.3356","38", "0.0064","43", "0.0","42", "0.4084","41", "0.3995","40", "0.5125","67", "0.6243","66", "0.1982","69", "0.5466","68", "0.2591","26", "0.0082","27", "0.3113","28", "0.0061","29", "0.5327","30", "0.62","32", "0.7949","31", "0.0806","70", "0.525","71", "0.2186","72", "0.0275","73", "0.2603","74", "0.0314","75", "0.1423","59", "0.742","58", "0.1133","57", "0.091","56", "0.4015","55", "0.6264","64", "6.0E-4","65", "0.5221","62", "0.049","63", "0.0814","60", "0.7431","61", "0.0897","49", "0.7513","48", "0.7008","45", "0.2595","44", "0.1752","47", "0.3374","46", "0.3298","51", "0.0628","52", "0.074","53", "0.0037","54", "0.0421","50", "0.7379"
    });
    Interp_AP.put(1, new String[] {
        "35", "0.1667","36", "0.0027","33", "0.0197","34", "0.0043","39", "0.3131","37", "0.333","38", "0.0064","43", "0.0","42", "0.4093","41", "0.2942","40", "0.5051","67", "0.6229","66", "0.2203","69", "0.5456","68", "0.2586","26", "0.0073","27", "0.3113","28", "0.0186","29", "0.5245","30", "0.6891","32", "0.1316","31", "0.0789","70", "0.5253","71", "0.2211","72", "0.0276","73", "0.2622","74", "0.0262","75", "0.1517","59", "0.7423","58", "0.1081","57", "0.0941","56", "0.3629","55", "0.6316","64", "6.0E-4","65", "0.486","62", "0.0491","63", "0.0814","60", "0.734","61", "0.0899","49", "0.7814","48", "0.7006","45", "0.3554","44", "0.1753","47", "0.337","46", "0.314","51", "0.0625","52", "0.0928","53", "0.0046","54", "0.0427","50", "0.7428"
    });
    Interp_AP.put(2, new String[] {
        "35", "0.1807","36", "0.0027","33", "0.0241","34", "0.0044","39", "0.3323","37", "0.3308","38", "0.0064","43", "0.0","42", "0.4085","41", "0.2136","40", "0.5166","67", "0.6253","66", "0.2055","69", "0.5464","68", "0.2556","26", "0.0082","27", "0.3113","28", "0.0058","29", "0.5319","30", "0.6832","32", "0.3168","31", "0.081","70", "0.5252","71", "0.219","72", "0.0278","73", "0.2601","74", "0.0302","75", "0.1449","59", "0.7421","58", "0.1165","57", "0.0905","56", "0.3992","55", "0.636","64", "6.0E-4","65", "0.5581","62", "0.0492","63", "0.0815","60", "0.7408","61", "0.0905","49", "0.7575","48", "0.7018","45", "0.3528","44", "0.1752","47", "0.3373","46", "0.3308","51", "0.0628","52", "0.0749","53", "0.0038","54", "0.0415","50", "0.7537"
    });
  }

  private static String[] baseline_token_AP = new String[] {
    "35", "0.1661","36", "0.0022","33", "0.0198","34", "0.0059","39", "0.324","37", "0.3725","38", "0.0068","43", "0.0","42", "0.4007","41", "0.3236","40", "0.5037","67", "0.5584","66", "0.2819","69", "0.5405","68", "0.2965","26", "0.0037","27", "0.2963","28", "0.0021","29", "0.5275","30", "0.7243","32", "0.3655","31", "0.042","70", "0.5104","71", "0.2278","72", "0.0276","73", "0.248","74", "0.0159","75", "0.1714","59", "0.7359","58", "0.1171","57", "0.0971","56", "0.3585","55", "0.616","64", "6.0E-4","65", "0.5153","62", "0.0509","63", "0.0783","60", "0.7071","61", "0.0885","49", "0.7933","48", "0.7146","45", "0.0731","44", "0.171","47", "0.3368","46", "0.2876","51", "0.0186","52", "0.0617","53", "0.0038","54", "0.0409","50", "0.7403"
  };

  public EnAr_TREC02() {
    super();
    qe = new QueryEngine();
  }

  @Test
  public void runRegressions() throws Exception {
    initialize();
    runRegression(0);   // "one2none"
    runRegression(1);   // "one2one"
    runRegression(2);   // "one2many"
    verifyAllResults(qe.getModels(), qe.getAllResults(), qe.getDocnoMapping(),
        new Qrels("data/"+ PATH + "/qrels."+ PATH + ".txt"));
  }

  public void runRegression(int heuristic) throws Exception {
    /////// baseline-token
    Configuration conf = RunQueryEngine.parseArgs(new String[] {
        "--xml", "data/"+ PATH + "/run_en-" + LANGUAGE + ".token.xml",
        "--queries_path", "data/"+ PATH + "/cdec/title_en-" + LANGUAGE + "-trans10-filtered.xml", "--one2many", heuristic + "", "--is_stemming", "--is_doc_stemmed" });
    FileSystem fs = FileSystem.getLocal(conf);

    // no need to repeat token-based case for other heuristics
    if (heuristic == 0) {
      qe.init(conf, fs);
      qe.runQueries(conf);
    }
    /////// 1-best

    conf = RunQueryEngine.parseArgs(new String[] {
        "--xml", "data/"+ PATH + "/run_en-" + LANGUAGE + ".1best.xml",
        "--queries_path", "data/"+ PATH + "/cdec/title_en-" + LANGUAGE + "-trans1-filtered.xml", "--one2many", heuristic + "", "--is_stemming", "--is_doc_stemmed" });

    qe.init(conf, fs);
    qe.runQueries(conf);

    /////// grammar

    conf = RunQueryEngine.parseArgs(new String[] {
        "--xml", "data/"+ PATH + "/run_en-" + LANGUAGE + ".grammar.xml",
        "--queries_path", "data/"+ PATH + "/cdec/title_en-" + LANGUAGE + "-trans10-filtered.xml", "--one2many", heuristic + "", "--is_stemming", "--is_doc_stemmed" });

    qe.init(conf, fs);
    qe.runQueries(conf);

    /////// 10-best

    conf = RunQueryEngine.parseArgs(new String[] {
        "--xml", "data/"+ PATH + "/run_en-" + LANGUAGE + ".10best.xml",
        "--queries_path", "data/"+ PATH + "/cdec/title_en-" + LANGUAGE + "-trans10-filtered.xml", "--one2many", heuristic + "", "--is_stemming", "--is_doc_stemmed" });

    qe.init(conf, fs);
    qe.runQueries(conf);

    /////// interp

    conf = RunQueryEngine.parseArgs(new String[] {
        "--xml", "data/"+ PATH + "/run_en-" + LANGUAGE + ".interp.xml",
        "--queries_path", "data/"+ PATH + "/cdec/title_en-" + LANGUAGE + "-trans10-filtered.xml", "--one2many", heuristic + "", "--is_stemming", "--is_doc_stemmed" });

    qe.init(conf, fs);
    qe.runQueries(conf);
  }

  public static void verifyAllResults(Set<String> models,
      Map<String, Map<String, Accumulator[]>> results, DocnoMapping mapping, Qrels qrels) {

    Map<String, GroundTruth> g = Maps.newHashMap();

    g.put("en-" + LANGUAGE + ".token_10-0-100-100_0", new GroundTruth(Metric.AP, numTopics, baseline_token_AP, expectedTokenMAP));

    for (int heuristic=0; heuristic <= 2; heuristic++) {
      g.put("en-" + LANGUAGE + ".grammar_10-0-0-100_" + heuristic, new GroundTruth(Metric.AP, numTopics, grammar_AP.get(heuristic), expectedMAPs.get(heuristic)[0]));
      g.put("en-" + LANGUAGE + ".1best_1-0-0-100_" + heuristic, new GroundTruth(Metric.AP, numTopics, Onebest_AP.get(heuristic), expectedMAPs.get(heuristic)[1]));
      g.put("en-" + LANGUAGE + ".10best_10-100-0-100_" + heuristic, new GroundTruth(Metric.AP, numTopics, Nbest_AP.get(heuristic), expectedMAPs.get(heuristic)[2]));
      g.put("en-" + LANGUAGE + ".interp_10-30-30-100_" + heuristic, new GroundTruth(Metric.AP, numTopics, Interp_AP.get(heuristic), expectedMAPs.get(heuristic)[3]));
    }

    for (String model : models) {
      System.err.println("Verifying results of model \"" + model + "\"");

      GroundTruth groundTruth = g.get(model); 
      Map<String, Accumulator[]> result = results.get(model);
      groundTruth.verify(result, mapping, qrels);

      System.err.println("Done!");
    }
  }

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(EnAr_TREC02.class);
  }

  public static void main(String[] args) {
    initialize();

    HMapSFW tenbestAPMap = array2Map(Nbest_AP.get(2));
    HMapSFW onebestAPMap = array2Map(Onebest_AP.get(2));
    HMapSFW grammarAPMap = array2Map(grammar_AP.get(2));
    HMapSFW tokenAPMap = array2Map(baseline_token_AP);

    System.out.println("10best: improved=" + countNumberOfImprovedTopics(tokenAPMap, tenbestAPMap) + ", negligible=" + countNumberOfNegligibleTopics(tokenAPMap, tenbestAPMap));
    System.out.println("Grammar: improved=" + countNumberOfImprovedTopics(tokenAPMap, grammarAPMap) + ", negligible=" + countNumberOfNegligibleTopics(tokenAPMap, grammarAPMap));
    System.out.println("1best: improved=" + countNumberOfImprovedTopics(tokenAPMap, onebestAPMap) + ", negligible=" + countNumberOfNegligibleTopics(tokenAPMap, onebestAPMap));
  }

  private static int countNumberOfImprovedTopics(HMapSFW tokenAPMap, HMapSFW gridAPMap) {
    int cnt = 0;
    for (String key : tokenAPMap.keySet()) {
      float difference = gridAPMap.get(key) - tokenAPMap.get(key); 
      if ( difference > 0.001 ) {
        cnt++;
      }
    }
    return cnt;
  }

  private static int countNumberOfNegligibleTopics(HMapSFW tokenAPMap, HMapSFW gridAPMap) {
    int cnt = 0;
    for (String key : tokenAPMap.keySet()) {
      float difference = gridAPMap.get(key) - tokenAPMap.get(key); 
      if ( difference > -0.001 && difference < 0.001 ) {
        cnt++;
      }
    }
    return cnt;
  }

  private static HMapSFW array2Map(String[] array) {
    HMapSFW map = new HMapSFW();
    for ( int i = 0; i < array.length; i += 2 ) {
      map.put(array[i], Float.parseFloat(array[i+1]));
    }
    return map;
  }

}
