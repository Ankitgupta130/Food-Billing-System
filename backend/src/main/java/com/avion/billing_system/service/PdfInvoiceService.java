package com.avion.billing_system.service;

import com.avion.billing_system.dto.OrderHistoryResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfInvoiceService {

    private final OrderService orderService;

    public byte[] generateInvoice(Long orderId) {
        try {
            OrderHistoryResponse order = orderService.getOrderById(orderId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font h2 = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font text = new Font(Font.HELVETICA, 11);
            NumberFormat inr = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");

            Paragraph title = new Paragraph("Avion Food Billing System", h1);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" ", text));

            // Meta table
            PdfPTable meta = new PdfPTable(new float[]{1.2f, 2.8f});
            meta.setWidthPercentage(100);
            meta.addCell(cell("Invoice No:", h2));
            meta.addCell(cell("INV-" + order.getOrderId(), text));
            meta.addCell(cell("Date:", h2));
            meta.addCell(cell(order.getOrderDate() != null ? dtf.format(order.getOrderDate()) : "-", text));
            meta.addCell(cell("Customer:", h2));
            meta.addCell(cell(order.getCustomerName(), text));
            meta.addCell(cell("Phone:", h2));
            meta.addCell(cell(order.getMobileNumber(), text));
            doc.add(meta);
            doc.add(new Paragraph(" "));

            // Items table
            PdfPTable table = new PdfPTable(new float[]{3.5f, 1.2f, 1.6f, 1.7f});
            table.setWidthPercentage(100);
            table.addCell(header("Item"));
            table.addCell(header("Qty"));
            table.addCell(header("Price"));
            table.addCell(header("Total"));

            double subtotal = 0.0;
            for (OrderHistoryResponse.ItemDetails item : order.getItems()) {
                table.addCell(body(item.getFoodItemName()));
                table.addCell(body(String.valueOf(item.getQuantity())));
                table.addCell(body(inr.format(item.getFoodItemPrice())));
                table.addCell(body(inr.format(item.getTotal())));
                subtotal += item.getTotal();
            }
            doc.add(table);
            doc.add(new Paragraph(" "));

            // Totals
            PdfPTable totals = new PdfPTable(new float[]{3.6f, 2.0f});
            totals.setWidthPercentage(50);
            totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.addCell(right("Subtotal:", h2));
            totals.addCell(right(inr.format(subtotal), h2));
            totals.addCell(right("Order Total:", h2));
            totals.addCell(right(inr.format(order.getTotalAmount()), h2));
            doc.add(totals);

            doc.add(new Paragraph("\nThank you for dining with us!", text));
            doc.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    private PdfPCell header(String s) {
        PdfPCell c = new PdfPCell(new Phrase(s, new Font(Font.HELVETICA, 11, Font.BOLD)));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setPadding(6);
        return c;
    }

    private PdfPCell body(String s) {
        PdfPCell c = new PdfPCell(new Phrase(s, new Font(Font.HELVETICA, 11)));
        c.setPadding(6);
        return c;
    }

    private PdfPCell right(String s, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(s, f));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(4);
        return c;
    }

    private PdfPCell cell(String s, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(s, f));
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(4);
        return c;
    }
}
