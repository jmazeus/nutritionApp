package com.nutrition.mx.response;

import com.nutrition.mx.dto.ClinicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicResponse {
    private List<ClinicDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
