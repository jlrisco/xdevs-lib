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

    private MongoOperations() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("xdevs");
        database.drop();
        collection = database.getCollection("ports");
    }

    public static MongoOperations getInstance() {
        if (instance == null) {
            instance = new MongoOperations();
        }
        return instance;
    }

    public void add(String component, String port, Object value) {
        Document document = new Document("component", component)
                .append("port", port)
                .append("value", value);
        collection.insertOne(document);
    }

    public Collection<?> getValues(String component, String port) {
        LinkedList<Document> values = collection
                .find(new Document("component", component).append("port", port))
                .projection(Projections.fields(Projections.include("value")))
                .into(new LinkedList<>());
        return values.stream()
                .map(d -> d.get("value"))
                .collect(Collectors.toList());
    }

    public void clear(String component, String port) {
        collection.deleteMany(new Document("component", component).append("port", port));
    }

    public boolean isEmpty(String component, String port) {
        return !collection
                .find(new Document("component", component).append("port", port))
                .iterator()
                .hasNext();
    }

    public Object getSingleValue(String component, String port) {
        MongoCursor<Document> it = collection
                .find(new Document("component", component).append("port", port))
                .iterator();
        if (!it.hasNext())
            throw new NoSuchElementException();
        else
            return it.next().get("value");
    }
}
