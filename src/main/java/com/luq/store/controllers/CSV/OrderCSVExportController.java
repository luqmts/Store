package com.luq.store.controllers.CSV;

import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.services.OrderService;
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
public class OrderCSVExportController {
    @Autowired
    OrderService oService;

    @GetMapping("/order/csv")
    public void exportToCsv(
        HttpServletResponse response,
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="product.id", required=false) Integer productId,
        @RequestParam(name="seller.id", required=false) Integer sellerId,
        @RequestParam(name="customer.id", required=false) Integer customerId
    ) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"orders.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,product,seller,customer,orderDate,quantity,totalPrice,created,created_by,modified,modified_by");

            List<OrderResponseDTO> oList = oService.getAllSorted(sortBy, direction, productId, sellerId, customerId);

            for (OrderResponseDTO order : oList) {
                String row = String.format(Locale.ROOT,
                "%d,%s,%s,%s,%s,%d,%.2f,%s,%s,%s,%s",
                    order.id(),
                    escapeCsv(order.product().getName()),
                    escapeCsv(order.seller().getName()),
                    escapeCsv(order.customer().getName()),
                    escapeCsv(order.orderDate().toString()),
                    order.quantity(),
                    order.totalPrice(),
                    escapeCsv(order.created().toString()),
                    escapeCsv(order.createdBy()),
                    escapeCsv(order.modified().toString()),
                    escapeCsv(order.modifiedBy())
                );

                writer.println(row);
            }
        }
    }
}
