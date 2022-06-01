package com.ivanxc.netcracker.bookshop.response;

import java.util.List;
import lombok.Data;

@Data
public class ErrorResponse {
    private final String message;
    private final List<String> details;
}
