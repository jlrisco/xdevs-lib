package xdevs.lib.tfgs.c1718.alex;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

public class MongoOperations {
	private static MongoOperations instance = null;
	
	private static MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> collection;
		    
    private MongoOperations(){
    	mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    	database = mongoClient.getDatabase("xdevs");
    	database.drop();
		collection = database.getCollection("ports");
    }
    
    public static MongoOperations getInstance() {
    	if (instance == null){
    		instance = new MongoOperations();
    	}
    	return instance;
    }
    
	public void add(String name, Object value) {
		Document document = new Document("port", name).append("value", value);
		collection.insertOne(document);
	}

	public Collection<?> getValues(String name) {
		LinkedList<Document> values = collection
				.find(new Document("port", name))
				.projection(Projections.fields(Projections.include("value")))
				.into(new LinkedList<Document>());
		return (Collection<?>) values.stream()
				.map(d -> d.get("value"))
				.collect(Collectors.toList());

		/*FindIterable<Document> values = collection.find(new Document("port", name));
		for(Document value : values){
			valueList.add((E) value.getInteger("value"));
		}
		return valueList;*/
	}

	public void clear(String name) {
		collection.deleteMany(new Document("port",name));
	}

	public boolean isEmpty(String name) {
		return !collection
				.find(new Document("port", name))
				.iterator()
				.hasNext();
	}

	public Object getSingleValue(String name) {
		MongoCursor<Document> it = collection.find(new Document("port", name)).iterator();
		if(!it.hasNext())
			throw new NoSuchElementException();
		else
			return it.next().get("value");
	}

	public void readValues(AtomicMongo model) {
		// TODO Auto-generated method stub
		for(PortMongo<?> port : model.getInPorts()){
			port.getValues();
			
		}
		
	}

}
