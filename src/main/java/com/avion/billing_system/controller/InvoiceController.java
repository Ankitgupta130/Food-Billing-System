package com.avion.billing_system.controller;

import com.avion.billing_system.entity.Order;
import com.avion.billing_system.repository.OrderDetailRepository;
import com.avion.billing_system.service.OrderService;
import com.avion.billing_system.service.PdfInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final OrderService orderService;
    private final  PdfInvoiceService pdfInvoiceService;


    @GetMapping("/{orderId}")
    public ResponseEntity<byte[]> download(@PathVariable Long orderId) {
        byte[] pdf = pdfInvoiceService.generateInvoice(orderId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice-" + orderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }



}
