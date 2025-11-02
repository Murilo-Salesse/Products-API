package dev.java10x.RegisterProductsAPI.Reports.Controller;

import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.Services.ProductService;
import dev.java10x.RegisterProductsAPI.Reports.Service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/top5")
    public ResponseEntity<byte[]> gerarTop5Produtos() throws Exception {
        byte[] pdf = reportService.generateReportTop5(); // método que gera o PDF

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=top5-produtos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
