package simpleshop.service.infrastructure;

import simpleshop.common.ObjectGraphDFS;
import simpleshop.data.infrastructure.HibernateDeProxyStrategy;
import simpleshop.service.infrastructure.impl.JsonValueResolver;

/**
 * Load or null out properties before sending to json serialization.
 */
public class JsonPreProcessor  {

    private ObjectGraphDFS<String> objectGraphDFS;

    public JsonPreProcessor(){
        objectGraphDFS = new ObjectGraphDFS<>(new JsonValueResolver(), new JacksonObjectGraphFilter(), new HibernateDeProxyStrategy());
    }

    public void process(Object target, String group){
        objectGraphDFS.inspect(target, group);
    }
}
