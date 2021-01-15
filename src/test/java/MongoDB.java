import java.util.HashMap;
import java.util.List;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDB {

	private static MongoClient mongoClient;
	static DBCollection nodes;
	static HashMap<String, Integer> hmapNodes = new HashMap<String, Integer>();
	
	public MongoDB(String dbName, String collectionName) {
		mongoClient = new MongoClient("127.0.0.1", 27017);
		createDb(dbName, collectionName);
	}

	private void createDb(String dbName, String collectionName) {
		DB database = mongoClient.getDB(dbName);
		database.createCollection(collectionName, null);
		nodes = database.createCollection("ancestors", null);	
	}

	public void insert(Long currentTimeMillis, List<Integer> ancestor) {
		BasicDBObject document = new BasicDBObject();
		document.put("date", currentTimeMillis);
		document.put("ancestors", ancestor);
		nodes.insert(document);	
	}

	public HashMap<String, Integer> findIdResult(Long idResult) {
		hmapNodes.clear();
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("date", idResult);
		DBCursor cursor = nodes.find(searchQuery);

		while (cursor.hasNext()) {
			DBObject val = cursor.next();
			System.out.println(val.get("ancestors"));
			String arrayNodes = val.get("ancestors").toString();
			getNumbersNodeToHmap(arrayNodes);
		}
		return hmapNodes;	
	}

	// save the number node in hmap
	private static void getNumbersNodeToHmap(String arrayNodes) {
		System.out.println(arrayNodes);
		String[] arr= arrayNodes.replace("[", "").replace("]", "").replace(" ", "").split(",");
		for (String num : arr) {
			if (hmapNodes.containsKey(num)) {
				hmapNodes.replace(num, hmapNodes.get(num) + 1);
			} else {
				hmapNodes.put(num, 1);
			}
		}
	}
}
