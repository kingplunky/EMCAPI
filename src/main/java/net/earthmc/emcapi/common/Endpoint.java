package net.earthmc.emcapi.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.javalin.Javalin;
import io.javalin.http.Context;
import net.earthmc.emcapi.common.interfaces.IEndpoint;
import net.earthmc.emcapi.common.interfaces.IQuery;
import net.earthmc.emcapi.manager.EndpointManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class Endpoint<T> implements IEndpoint<T> {
    protected ObjectMapper objectMapper;
    private Javalin javalin;
    protected Class<T> clazz;

    public Endpoint(Javalin javalin) {
        this.javalin = javalin;
    }


    public void setup() {
        objectMapper = createObjectMapper();

        String path = EndpointManager.BASE_URL + this.getPath();
        javalin.post(path , ctx -> {
            Query<T> query = javalinContextToQuery(ctx);
            if (query == null) return;

            ctx.json(this.query(query));

        });
    }



    private Query<T> javalinContextToQuery(Context ctx) {
        try {
            Query<T> query = objectMapper.readValue(ctx.body(), new TypeReference<Query<T>>() {});

            List<String> errors = query.validate();
            if (!errors.isEmpty()) {ctx.status(400).json(errors); return null;}

            return query;

        } catch (Exception e) {
            ctx.status(400).result("Invalid json body: " + e.getMessage());
            return null;
        }
    }



    @SuppressWarnings("unchecked")
    protected Class<T> reflectClassType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Could not determine class type for generic T");
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Query.class, new QueryDeserializer<>(clazz));
        objectMapper.registerModule(module);

        return objectMapper;
    }

    @Override
    public List<T> query(IQuery<T> query) {
        List<T> objects = this.createObjects();

        return objects.stream().filter(query::matches).toList();
    }

}
