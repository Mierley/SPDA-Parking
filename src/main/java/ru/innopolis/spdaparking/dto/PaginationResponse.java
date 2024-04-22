package ru.innopolis.spdaparking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class PaginationResponse<T>{
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private int totalCount;
    private int pageSize;
    

    private PaginationResponse(List<T> data, int count, int page, int pageSize) {
        this.data = data;
        this.currentPage = page;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) count / pageSize);
        this.totalCount = count;
    }

    public static <T> PaginationResponse<T> of(List<T> data, int count, int page, int pageSize) {
        return new PaginationResponse<>(data, count, page, pageSize);
    }

    @JsonProperty
    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    @JsonProperty
    public boolean hasNextPage() {
        return currentPage < totalPages;
    }
}

