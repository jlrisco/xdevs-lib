package xdevs.lib.tfgs.c1718.alex;

import java.util.Collection;

public class PortMongo<E> {
	private MongoOperations mongo = MongoOperations.getInstance();
	
    protected ComponentMongo parent = null;
    protected String name;

    public PortMongo(String name) {
        this.name = name;
    }

    public PortMongo() {
        this(PortMongo.class.getSimpleName());
    }
    
    public String getName() {
        return name;
    }

    public void clear() {
        mongo.clear(name);
    }

    public boolean isEmpty() {
        return mongo.isEmpty(name);
    }

    @SuppressWarnings("unchecked")
	public E getSingleValue() {
        return (E) mongo.getSingleValue(name);
    }

    @SuppressWarnings("unchecked")
	public Collection<E> getValues() {
        return (Collection<E>) mongo.getValues(name);
    }

    public void addValue(E value) {
        mongo.add(name, value);
    }

    public void addValues(Collection<E> values) {
        for(E value : values)
        	addValue(value);
    }

    public ComponentMongo getParent() {
        return parent;
    }
}
