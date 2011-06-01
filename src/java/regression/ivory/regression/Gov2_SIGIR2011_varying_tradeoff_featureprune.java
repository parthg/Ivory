package ivory.regression;

import ivory.eval.Qrels;
import ivory.eval.Qrels_new;
import ivory.regression.GroundTruth.Metric;
import ivory.smrf.retrieval.Accumulator;
import ivory.smrf.retrieval.BatchQueryRunner;
import ivory.smrf.retrieval.BatchQueryRunner_cascade;

import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.junit.Test;

import edu.umd.cloud9.collection.DocnoMapping;

public class Gov2_SIGIR2011_varying_tradeoff_featureprune {

	private static final Logger sLogger = Logger.getLogger(Gov2_SIGIR2011_varying_tradeoff_featureprune.class);

	private static String[] p1 = new String[] {
	 "776", "0.268",  "777", "0.1257",  "778", "0.4791",  "779", "0.9087",  "780", "0.4601", 
	 "781", "0.5673",  "782", "0.5214",  "783", "0.4787",  "784", "0.7574",  "785", "0.8446", 
	 "786", "0.2088",  "787", "0.8758",  "788", "0.7909",  "789", "0.3502",  "790", "0.8563", 
	 "791", "0.611",  "792", "0.2369",  "793", "0.5467",  "794", "0.4725",  "795", "0.0", 
	 "796", "0.3535",  "797", "0.2122",  "798", "0.1414",  "799", "0.3632",  "800", "0.1413", 
	 "801", "0.5867",  "802", "0.9572",  "803", "0.0",  "804", "0.4972",  "805", "0.1564", 
	 "806", "0.5721",  "807", "0.9393",  "808", "0.9549",  "809", "0.3055",  "810", "0.4375", 
	 "811", "0.5875",  "812", "0.7242",  "813", "0.7551",  "814", "0.7059",  "815", "0.1877", 
	 "816", "0.5724",  "817", "0.9046",  "818", "0.4776",  "819", "0.9239",  "820", "0.7114", 
	 "821", "0.2575",  "822", "0.1366",  "823", "0.6292",  "824", "0.3156",  "825", "0.1378", 
	 "826", "0.4913",  "827", "0.5058",  "828", "0.5376",  "829", "0.4397",  "830", "0.3218", 
	 "831", "0.7272",  "832", "0.5761",  "833", "0.3533",  "834", "0.5356",  "835", "0.7072", 
	 "836", "0.1959",  "837", "0.0",  "838", "0.6889",  "839", "0.6344",  "840", "0.1744", 
	 "841", "0.5466",  "842", "0.1061",  "843", "0.6907",  "844", "0.1686",  "845", "0.2825", 
	 "846", "0.478",  "847", "0.0472",  "848", "0.148",  "849", "0.6821",  "850", "0.3229"};

	private static String[] p3 = new String[] {
	 "776", "0.2503",  "777", "0.1257",  "778", "0.4791",  "779", "0.9087",  "780", "0.443", 
	 "781", "0.5673",  "782", "0.5214",  "783", "0.4787",  "784", "0.7574",  "785", "0.8446", 
	 "786", "0.2088",  "787", "0.8758",  "788", "0.7909",  "789", "0.3502",  "790", "0.7095", 
	 "791", "0.611",  "792", "0.2088",  "793", "0.5467",  "794", "0.4725",  "795", "0.0", 
	 "796", "0.3535",  "797", "0.2122",  "798", "0.1414",  "799", "0.3632",  "800", "0.1413", 
	 "801", "0.5867",  "802", "0.9627",  "803", "0.0",  "804", "0.5225",  "805", "0.1564", 
	 "806", "0.5721",  "807", "0.9393",  "808", "0.9393",  "809", "0.3055",  "810", "0.4375", 
	 "811", "0.5875",  "812", "0.7242",  "813", "0.7525",  "814", "0.7059",  "815", "0.1877", 
	 "816", "0.5724",  "817", "0.9046",  "818", "0.4776",  "819", "0.9239",  "820", "0.7114", 
	 "821", "0.2575",  "822", "0.1366",  "823", "0.6743",  "824", "0.3156",  "825", "0.1378", 
	 "826", "0.4913",  "827", "0.5058",  "828", "0.5376",  "829", "0.4397",  "830", "0.3218", 
	 "831", "0.4402",  "832", "0.5761",  "833", "0.3533",  "834", "0.5356",  "835", "0.7072", 
	 "836", "0.2155",  "837", "0.0",  "838", "0.6889",  "839", "0.6344",  "840", "0.1744", 
	 "841", "0.5466",  "842", "0.1061",  "843", "0.6907",  "844", "0.1686",  "845", "0.2825", 
	 "846", "0.478",  "847", "0.0472",  "848", "0.148",  "849", "0.6821",  "850", "0.3289"};

	private static String [] p5 = new String [] {
	 "776", "0.2503",  "777", "0.1257",  "778", "0.4791",  "779", "0.9087",  "780", "0.443", 
	 "781", "0.5673",  "782", "0.5214",  "783", "0.4787",  "784", "0.7574",  "785", "0.8446", 
	 "786", "0.2088",  "787", "0.8758",  "788", "0.7522",  "789", "0.3502",  "790", "0.7095", 
	 "791", "0.611",  "792", "0.2088",  "793", "0.5467",  "794", "0.4725",  "795", "0.0", 
	 "796", "0.3535",  "797", "0.2122",  "798", "0.1414",  "799", "0.3632",  "800", "0.1413", 
	 "801", "0.5867",  "802", "0.9627",  "803", "0.0",  "804", "0.5225",  "805", "0.1564", 
	 "806", "0.5721",  "807", "0.9393",  "808", "0.9393",  "809", "0.3055",  "810", "0.4375", 
	 "811", "0.5875",  "812", "0.7242",  "813", "0.7525",  "814", "0.7059",  "815", "0.1877", 
	 "816", "0.5724",  "817", "0.9046",  "818", "0.4776",  "819", "0.9239",  "820", "0.7309", 
	 "821", "0.2575",  "822", "0.1366",  "823", "0.6743",  "824", "0.3156",  "825", "0.1378", 
	 "826", "0.4913",  "827", "0.5058",  "828", "0.5376",  "829", "0.4397",  "830", "0.3218", 
	 "831", "0.4402",  "832", "0.5761",  "833", "0.3533",  "834", "0.5356",  "835", "0.7072", 
	 "836", "0.2155",  "837", "0.0",  "838", "0.6889",  "839", "0.6344",  "840", "0.1744", 
	 "841", "0.5466",  "842", "0.1061",  "843", "0.6907",  "844", "0.1686",  "845", "0.2825", 
	 "846", "0.478",  "847", "0.0472",  "848", "0.2901",  "849", "0.6821",  "850", "0.3289"};


	private static String [] p7 = new String[] {
	 "776", "0.2503",  "777", "0.2199",  "778", "0.4465",  "779", "0.9087",  "780", "0.443", 
	 "781", "0.1535",  "782", "0.4378",  "783", "0.5223",  "784", "0.7574",  "785", "0.6816", 
	 "786", "0.4783",  "787", "0.8758",  "788", "0.7522",  "789", "0.3502",  "790", "0.7095", 
	 "791", "0.5934",  "792", "0.2088",  "793", "0.5467",  "794", "0.1336",  "795", "0.0", 
	 "796", "0.4703",  "797", "0.2828",  "798", "0.1414",  "799", "0.3934",  "800", "0.1413", 
	 "801", "0.5912",  "802", "0.9677",  "803", "0.0",  "804", "0.5225",  "805", "0.1564", 
	 "806", "0.5721",  "807", "0.8917",  "808", "0.9393",  "809", "0.2694",  "810", "0.4375", 
	 "811", "0.5726",  "812", "0.809",  "813", "0.8975",  "814", "0.701",  "815", "0.125", 
	 "816", "0.5724",  "817", "0.7799",  "818", "0.4596",  "819", "0.9821",  "820", "0.7109", 
	 "821", "0.3232",  "822", "0.2268",  "823", "0.6743",  "824", "0.3156",  "825", "0.1389", 
	 "826", "0.436",  "827", "0.6028",  "828", "0.4339",  "829", "0.2159",  "830", "0.2912", 
	 "831", "0.7033",  "832", "0.5761",  "833", "0.5925",  "834", "0.5989",  "835", "0.0334", 
	 "836", "0.2155",  "837", "0.0",  "838", "0.6889",  "839", "0.5399",  "840", "0.1744", 
	 "841", "0.5466",  "842", "0.1059",  "843", "0.6511",  "844", "0.061",  "845", "0.3358", 
	 "846", "0.478",  "847", "0.0472",  "848", "0.2901",  "849", "0.562",  "850", "0.3289"};


        private static String [] p9 = new String[] {
	 "776", "0.268",  "777", "0.1257",  "778", "0.4791",  "779", "0.9087",  "780", "0.4601", 
	 "781", "0.5673",  "782", "0.5214",  "783", "0.4787",  "784", "0.7574",  "785", "0.8446", 
	 "786", "0.2088",  "787", "0.8758",  "788", "0.7909",  "789", "0.3502",  "790", "0.8563", 
	 "791", "0.611",  "792", "0.2369",  "793", "0.5467",  "794", "0.4725",  "795", "0.0", 
	 "796", "0.3535",  "797", "0.2122",  "798", "0.1414",  "799", "0.3632",  "800", "0.1413", 
	 "801", "0.5867",  "802", "0.9572",  "803", "0.0",  "804", "0.4972",  "805", "0.1564", 
	 "806", "0.5721",  "807", "0.9393",  "808", "0.9549",  "809", "0.3055",  "810", "0.4375", 
	 "811", "0.5875",  "812", "0.7242",  "813", "0.7551",  "814", "0.7059",  "815", "0.1877", 
	 "816", "0.5724",  "817", "0.9046",  "818", "0.4776",  "819", "0.9239",  "820", "0.7114", 
	 "821", "0.2575",  "822", "0.1366",  "823", "0.6292",  "824", "0.3156",  "825", "0.1378", 
	 "826", "0.4913",  "827", "0.5058",  "828", "0.5376",  "829", "0.4397",  "830", "0.3218", 
	 "831", "0.7272",  "832", "0.5761",  "833", "0.3533",  "834", "0.5356",  "835", "0.7072", 
	 "836", "0.1959",  "837", "0.0",  "838", "0.6889",  "839", "0.6344",  "840", "0.1744", 
	 "841", "0.5466",  "842", "0.1061",  "843", "0.6907",  "844", "0.1686",  "845", "0.2825", 
	 "846", "0.478",  "847", "0.0472",  "848", "0.148",  "849", "0.6821",  "850", "0.3229"};


	@Test
	public void runRegression() throws Exception {
		Map<String, GroundTruth> g = new HashMap<String, GroundTruth>();

                g.put("gov2-featureprune-0.9", new GroundTruth("gov2-featureprune-0.9", Metric.NDCG20, 75, p9, 0.4478f));

                g.put("gov2-featureprune-0.7", new GroundTruth("gov2-featureprune-0.7", Metric.NDCG20, 75, p7, 0.4539f));

                g.put("gov2-featureprune-0.5", new GroundTruth("gov2-featureprune-0.5", Metric.NDCG20, 75, p5, 0.4677f));

                g.put("gov2-featureprune-0.3", new GroundTruth("gov2-featureprune-0.3", Metric.NDCG20, 75, p3, 0.4661f));

                g.put("gov2-featureprune-0.1", new GroundTruth("gov2-featureprune-0.1", Metric.NDCG20, 75, p1, 0.4716f));


		Qrels_new qrels = new Qrels_new("data/gov2/qrels.gov2.all");

    String[] params = new String[] {
            "data/gov2/run.gov2.SIGIR2011.varying.tradeoff.featureprune.xml",
            "data/gov2/gov2.title.776-850" };

		FileSystem fs = FileSystem.getLocal(new Configuration());

		BatchQueryRunner_cascade qr = new BatchQueryRunner_cascade(params, fs);

		long start = System.currentTimeMillis();
		qr.runQueries();
		long end = System.currentTimeMillis();

		sLogger.info("Total query time: " + (end - start) + "ms");

		DocnoMapping mapping = qr.getDocnoMapping();

		for (String model : qr.getModels()) {
			sLogger.info("Verifying results of model \"" + model + "\"");

			Map<String, Accumulator[]> results = qr.getResults(model);
			g.get(model).verify(results, mapping, qrels);

			sLogger.info("Done!");
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(Gov2_SIGIR2011_varying_tradeoff_featureprune.class);
	}
}
