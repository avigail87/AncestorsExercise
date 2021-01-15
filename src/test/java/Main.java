import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application {
	// config
	static int numbrtOfRuns = 3;
	// general
	static long currentTimeMillis;
	static List<Long> idResults = new ArrayList<Long>();
	static MongoDB mongoDB;
	static CategoryAxis xAxis = new CategoryAxis();
	static NumberAxis yAxis = new NumberAxis();
	static BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
	
	public static void main(String[] args) {

		// create db for the results
		createMongoDB("ancestorsData", "ancestors");

		// build the given tree
		Node root = createRootNode();

		Random rn = new Random();
		// run on the function more than 100 times * numbrtOfRuns
		while(numbrtOfRuns > 0) {
			currentTimeMillis = System.currentTimeMillis();
			idResults.add(currentTimeMillis);
			for (int i = 0; i < 120; i++) {
				int rnNumber = rn.nextInt(31);
				List<Integer> ancestors = FindAncestors.getAncestors(root, rnNumber);
				saveAncestorsInDb(ancestors);
			}
			numbrtOfRuns--;
		}

		// get the results from the db
		getAncestorsFromDb();
		
		// create a visualization
		launch();
	}

	private static Node createRootNode() {
		Node node3 = new Node(3, new Node(7, new Node(15, null, null), new Node(17, null, null)),
				new Node(9, new Node(19, null, null), new Node(21, null, null)));
		Node node5 = new Node(5, new Node(11, new Node(23, null, null), new Node(25, null, null)),
				new Node(13, new Node(27, null, null), new Node(29, null, null)));
		Node node4 = new Node(4, new Node(8, new Node(16, null, null), new Node(18, null, null)),
				new Node(10, new Node(20, null, null), new Node(22, null, null)));
		Node node6 = new Node(6, new Node(12, new Node(24, null, null), new Node(26, null, null)),
				new Node(14, new Node(28, null, null), new Node(30, null, null)));
		Node root = new Node(0, new Node(1, node3, node5), new Node(2, node4, node6));
		return root;
	}

	private static void createMongoDB(String dbName, String collectionName) {
		mongoDB = new MongoDB(dbName, collectionName);	
	}

	private static void saveAncestorsInDb(List<Integer> ancestors) {
		mongoDB.insert(currentTimeMillis, ancestors);
	}

	private static void getAncestorsFromDb() {
		int repNumber = 1;
		for (Long idResult : idResults) {
			// get the results from the db and convert them to hmap
			HashMap<String, Integer> hmapNodes = mongoDB.findIdResult(idResult);

			// locate the repetition number in XYChart.Series
			XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
			series.setName("Run" + repNumber);
			for (int num=0; num<=30; num++) {
				String key = String.valueOf(num);
				Number val = 0;
				if (hmapNodes.containsKey(key)) {
					val = hmapNodes.get(key);
				}
				series.getData().add(new XYChart.Data<String, Number>(key, val));
			}		
			barChart.getData().addAll(series);
			repNumber++;
		}
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Random Ancestors Chart");
		xAxis.setLabel("Node");
		yAxis.setLabel("Repetition");
		Scene scene = new Scene(barChart, 1250, 600);
		stage.setScene(scene);
		stage.show();
	}

}
