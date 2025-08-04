package com.luq.store.controllers.CSV;

import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.services.CustomerService;
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
public class CustomerCSVExportController {
    @Autowired
    CustomerService cService;

    @GetMapping("/customer/csv")
    public void exportToCsv(
        HttpServletResponse response,
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="name", required=false) String name
    ) throws IOException {
        name = (Objects.equals(name, "")) ? null : name;

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"customers.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,name,created,created_by,modified,modified_by");

            List<CustomerResponseDTO> cList = cService.getAllSorted(sortBy, direction, name);

            for (CustomerResponseDTO customer : cList) {
                String row = String.format(Locale.ROOT,
                "%d,%s,%s,%s,%s,%s",
                    customer.id(),
                    escapeCsv(customer.name()),
                    escapeCsv(customer.created().toString()),
                    escapeCsv(customer.createdBy()),
                    escapeCsv(customer.modified().toString()),
                    escapeCsv(customer.modifiedBy())
                );

                writer.println(row);
            }
        }
    }
}
