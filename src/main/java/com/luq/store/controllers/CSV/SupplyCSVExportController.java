package com.luq.store.controllers.CSV;

import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.services.SupplyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import static org.unbescape.csv.CsvEscape.escapeCsv;

@RestController
public class SupplyCSVExportController {
    @Autowired
    SupplyService sService;

    @GetMapping("/supply/csv")
    public void exportToCsv(
        HttpServletResponse response,
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="productId", required=false) Integer productId
    ) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"supply.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,product,quantity,created,created_by,modified,modified_by");

            List<SupplyResponseDTO> sList = sService.getAllSorted(sortBy, direction, productId);

            sList
                .stream()
                .map(data -> String.format(
                    Locale.ROOT,
                    "%d,%s,%d,%s,%s,%s,%s",
                    data.id(),
                    escapeCsv(data.product().getName()),
                    data.quantity(),
                    escapeCsv(data.created().toString()),
                    escapeCsv(data.createdBy()),
                    escapeCsv(data.modified().toString()),
                    escapeCsv(data.modifiedBy())
                )).forEach(writer::println);
        }
    }
}
