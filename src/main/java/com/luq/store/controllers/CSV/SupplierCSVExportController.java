package com.luq.store.controllers.CSV;

import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.services.SupplierService;
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
public class SupplierCSVExportController {
    @Autowired
    SupplierService sService;

    @GetMapping("/supplier/csv")
    public void exportToCsv(
        HttpServletResponse response,
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="cnpj", required=false) String cnpj,
        @RequestParam(name="mail", required=false) String mail,
        @RequestParam(name="phone", required=false) String phone
    ) throws IOException {
        name = (Objects.equals(name, "")) ? null : name;
        cnpj = (Objects.equals(cnpj, "")) ? null : cnpj;
        mail = (Objects.equals(mail, "")) ? null : mail;
        phone = (Objects.equals(phone, "")) ? null : phone;

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"suppliers.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,name,cnpj,mail,phone,created,created_by,modified,modified_by");

            List<SupplierResponseDTO> sList = sService.getAllSorted(sortBy, direction, name, cnpj, mail, phone);

            sList
                .stream()
                .map(data -> String.format(
                    Locale.ROOT,
                    "%d,%s,%s,%s,%s,%s,%s,%s,%s",
                    data.id(),
                    escapeCsv(data.name()),
                    escapeCsv(data.cnpj()),
                    escapeCsv(data.mail()),
                    escapeCsv(data.phone()),
                    escapeCsv(data.created().toString()),
                    escapeCsv(data.createdBy()),
                    escapeCsv(data.modified().toString()),
                    escapeCsv(data.modifiedBy())
                )).forEach(writer::println);
        }
    }
}
