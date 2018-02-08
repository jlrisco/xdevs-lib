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
        mongo.clear(parent.name, this.name);
    }

    public boolean isEmpty() {
        return mongo.isEmpty(parent.name, this.name);
    }

    @SuppressWarnings("unchecked")
    public E getSingleValue() {
        return (E) mongo.getSingleValue(parent.name, this.name);
    }

    @SuppressWarnings("unchecked")
    public Collection<E> getValues() {
        return (Collection<E>) mongo.getValues(parent.name, this.name);
    }

    public void addValue(E value) {
        mongo.add(parent.name, this.name, value);
    }

    public void addValues(Collection<E> values) {
        for (E value : values)
            addValue(value);
    }

    public ComponentMongo getParent() {
        return parent;
    }
}
