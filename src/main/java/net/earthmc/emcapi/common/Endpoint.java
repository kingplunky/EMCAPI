package net.earthmc.emcapi.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.javalin.Javalin;
import io.javalin.http.Context;
import net.earthmc.emcapi.common.interfaces.IEndpoint;
import net.earthmc.emcapi.manager.EndpointManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Endpoint<T> implements IEndpoint<T> {
    protected ObjectMapper objectMapper;
    private final Javalin javalin;
    protected Class<T> clazz;
    private LoadingCache<String, List<T>> objectsCache;

    public Endpoint(Javalin javalin) {
        this.javalin = javalin;
    }

    public void setup() {
        objectMapper = createObjectMapper();

        objectsCache = Caffeine.newBuilder()
                .maximumSize(1)
                .refreshAfterWrite(Duration.ofSeconds(10))
                .build(key -> this.createObjects());

        String path = EndpointManager.BASE_URL + this.getPath();

        javalin.post(path , ctx -> {
            List<Query<T>> queries = javalinContextToQuery(ctx);
            if (queries == null) return;

            List<T> results = this.query(queries);

            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);

            try {
                ctx.json(new PaginatedResponse<>(results, page, 100));

            } catch (IllegalArgumentException e) {
                ctx.status(400).result(e.getMessage());
            }

        });
    }



    private List<Query<T>> javalinContextToQuery(Context ctx) {
        String body = ctx.body().strip();

        if (body.isBlank()) {
            return new ArrayList<>();
        }

        try {
            List<Query<T>> queries = objectMapper.readValue(body, new TypeReference<>() {});

            List<String> errors = queries.stream()
                    .flatMap(q -> q.validate().stream())
                    .toList();


            if (!errors.isEmpty()) {ctx.status(400).json(errors); return null;}

            return queries;

        } catch (Exception e) {
            ctx.status(400).json(List.of("Invalid json body: " + e.getMessage()));
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
        module.addDeserializer(List.class, new QueryDeserializer<>(clazz));
        objectMapper.registerModule(module);

        return objectMapper;
    }

    @Override
    public List<T> query(List<Query<T>> queries) {
        return objectsCache.get("").parallelStream()
                .filter(object -> queries.stream().allMatch(query -> query.matches(object)))
                .toList();

    }


}
