package com.luq.store.controllers.CSV;

import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.unbescape.csv.CsvEscape.escapeCsv;

@RestController
public class ProductCSVExportController {
    @Autowired
    ProductService pService;

    @GetMapping("/product/csv")
    public void exportToCsv(
        HttpServletResponse response,
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="supplierId", required=false) Integer supplierId,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="sku", required=false) String sku
    ) throws IOException {
        name = (Objects.equals(name, "")) ? null : name;
        sku = (Objects.equals(sku, "")) ? null : sku;

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"products.csv\"");
        List<ProductResponseDTO> pList = pService.getAllSorted(sortBy, direction, supplierId, name, sku);

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,name,price,sku,supplier,description,created,created_by,modified,modified_by");

            pList
                .stream()
                .map(data -> String.format(
                    Locale.ROOT,
                    "%d,%s,%.2f,%s,%s,%s,%s,%s,%s,%s",
                    data.id(),
                    escapeCsv(data.name()),
                    data.price(),
                    escapeCsv(data.sku()),
                    escapeCsv(data.supplier().getName()),
                    escapeCsv(data.description()),
                    escapeCsv(data.created().toString()),
                    escapeCsv(data.createdBy()),
                    escapeCsv(data.modified().toString()),
                    escapeCsv(data.modifiedBy())
                )).forEach(writer::println);
        }
    }
}
