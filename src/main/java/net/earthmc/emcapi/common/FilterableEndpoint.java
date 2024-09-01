package net.earthmc.emcapi.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.javalin.Javalin;
import io.javalin.http.Context;
import net.earthmc.emcapi.common.interfaces.IFilterableEndpoint;
import net.earthmc.emcapi.manager.EndpointManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public abstract class FilterableEndpoint<T> implements IFilterableEndpoint<T> {
    private final EndpointManager endpointManager;
    private final Javalin javalin;
    protected ObjectMapper objectMapper;
    protected Class<T> clazz;
    private LoadingCache<String, List<T>> objectsCache;

    public FilterableEndpoint(Javalin javalin) {
        this.javalin = javalin;
        this.endpointManager = EndpointManager.getInstance();
    }

    public void setup() {
        // Set up the filter serializer
        objectMapper = createObjectMapper();

        // Create a caffeine cache of filterable objects
        objectsCache = Caffeine.newBuilder()
                .maximumSize(1)
                .refreshAfterWrite(Duration.ofSeconds(10))
                .build(key -> this.createObjects());


        String path = EndpointManager.BASE_URL + this.getPath();

        javalin.post(path , ctx -> {
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);

            List<Filter<T>> filters = getFiltersFromBody(ctx);
            if (filters == null) return;

            List<T> results = this.filter(filters);


            try {
                ctx.json(new PaginatedResponse<>(results, page, endpointManager.PAGE_SIZE));

            } catch (IllegalArgumentException e) {
                ctx.status(400).result(e.getMessage());
            }

        });
    }


    private List<Filter<T>> getFiltersFromBody(Context ctx) {
        String body = ctx.body();

        if (body.isBlank()) {
            return new ArrayList<>();
        }

        try {
            List<Filter<T>> filters = objectMapper.readValue(body, new TypeReference<>() {});

            List<String> errors = filters.stream()
                    .flatMap(f -> f.validate().stream())
                    .toList();


            if (!errors.isEmpty()) {ctx.status(400).json(errors); return null;}

            return filters;

        } catch (Exception e) {
            ctx.status(400).json(List.of("Invalid json body: " + e.getMessage()));
            return null;
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new FilterDeserializer<>(clazz));
        objectMapper.registerModule(module);

        return objectMapper;
    }

    @Override
    public List<T> filter(List<Filter<T>> filters) {
        return objectsCache.get("").parallelStream()
                .filter(object -> filters.stream().allMatch(query -> query.matches(object)))
                .toList();
    }

    /**
     * Gets the class type from the type generic <T>.
     * Must be called before `setup()` in the inheriting Endpoint object.
     * Note: <a href="https://stackoverflow.com/questions/51388446/getactualtypearguments-with-parent-which-get-its-parametrized-type-from-the-chil">From this stackoverflow post.</a>
     * @return The `Class<T>` for the generic type T
     */
    @SuppressWarnings("unchecked")
    protected Class<T> reflectClassType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Could not determine class type for generic T");
        }
    }

}
