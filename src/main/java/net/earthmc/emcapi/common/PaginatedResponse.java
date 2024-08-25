package net.earthmc.emcapi.common;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginatedResponse<T> {

    private final List<T> results;
    private final int numResults;
    private final int numPages;
    private final int currentPage;
    private final int pageSize;

    public PaginatedResponse(List<T> results, int currentPage, int pageSize) {
        this.numResults = results.size();
        this.pageSize = pageSize;
        this.numPages = (int) Math.ceil((double) numResults / pageSize);
        this.currentPage = currentPage;
        this.results = this.numResults == 0 ? List.of() : getPaginatedItems(results);
    }

    private List<T> getPaginatedItems(List<T> results) {
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, results.size());

        if (start >= numResults || start < 0) {
            throw new IllegalArgumentException(String.format("Page number must be within inclusive range (1, %s)", numPages));
        }

        return results.subList(start, end);
    }
}
