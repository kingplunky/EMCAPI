package net.earthmc.emcapi.common;

import lombok.Getter;

import java.util.List;

public class Pagination<T> {
    private final List<T> items;

    @Getter private final int page;
    @Getter private final int pageSize;


    public Pagination(List<T> items, int page, int pageSize) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<T> getPaginatedItems() {
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, items.size());

        if (start >= items.size()) {
            throw new IllegalArgumentException("Page number out of range.");
        }

        return items.subList(start, end);
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }
}
