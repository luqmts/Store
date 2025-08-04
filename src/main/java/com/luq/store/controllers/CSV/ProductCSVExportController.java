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
        @RequestParam(name="supplier.id", required=false) Integer supplierId,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="sku", required=false) String sku
    ) throws IOException {
        name = (Objects.equals(name, "")) ? null : name;
        sku = (Objects.equals(sku, "")) ? null : sku;

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"products.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,name,price,sku,supplier,description,created,created_by,modified,modified_by");

            List<ProductResponseDTO> pList = pService.getAllSorted(sortBy, direction, supplierId, name, sku);

            for (ProductResponseDTO product : pList) {
                String row = String.format(Locale.ROOT,
                "%d,%s,%.2f,%s,%s,%s,%s,%s,%s,%s",
                    product.id(),
                    escapeCsv(product.name()),
                    product.price(),
                    escapeCsv(product.sku()),
                    escapeCsv(product.supplier().getName()),
                    escapeCsv(product.description()),
                    escapeCsv(product.created().toString()),
                    escapeCsv(product.createdBy()),
                    escapeCsv(product.modified().toString()),
                    escapeCsv(product.modifiedBy())
                );

                writer.println(row);
            }
        }
    }
}
