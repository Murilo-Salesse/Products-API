package dev.java10x.RegisterProductsAPI.Reports.Service;

import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.Services.ProductService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final ProductService productService;

    public ReportService(ProductService productService) {
        this.productService = productService;
    }

    public byte[] generateReportTop5() throws Exception {
        try {
            // 1. Buscar os 5 produtos mais caros
            List<ProductsDTO> produtos = productService.listTop5Products();
            System.out.println("📦 Total de produtos carregados: " + produtos.size());

            // 2. Carregar template .jrxml via classpath
            String reportPath = "/reports/products_top5.jrxml";
            InputStream reportStream = getClass().getResourceAsStream(reportPath);

            if (reportStream == null) {
                // Tentar caminho alternativo
                reportStream = getClass().getClassLoader().getResourceAsStream("reports/products_top5.jrxml");
                if (reportStream == null) {
                    throw new RuntimeException("❌ Arquivo products_top5.jrxml não encontrado em: " + reportPath);
                }
            }

            System.out.println("✅ Arquivo JRXML encontrado e carregado");

            // 3. Compilar o relatório com validação desabilitada
            System.out.println("🔄 Tentando compilar o relatório...");

            // Tenta compilar sem validação XML
            JasperReport jasperReport;
            try {
                jasperReport = JasperCompileManager.compileReport(reportStream);
                System.out.println("✅ Relatório compilado com sucesso");
            } catch (JRException e) {
                System.err.println("❌ Falha na compilação.");
                System.err.println("Mensagem: " + e.getMessage());
                System.err.println("Causa: " + e.getCause());

                // Captura stack trace completo
                Throwable cause = e;
                int level = 0;
                while (cause != null && level < 10) {
                    System.err.println("  Nível " + level + ": " + cause.getClass().getName() + " - " + cause.getMessage());
                    cause = cause.getCause();
                    level++;
                }
                throw e;
            }

            // 4. Montar datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

            Map<String, Object> params = new HashMap<>();
            params.put("createdBy", "Sistema Spring Boot");

            // 5. Preencher o relatório
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, dataSource);
            System.out.println("✅ Relatório preenchido com sucesso");

            // 6. Exportar para PDF (byte[])
            byte[] pdf = JasperExportManager.exportReportToPdf(print);
            System.out.println("✅ PDF gerado com sucesso. Tamanho: " + pdf.length + " bytes");

            return pdf;

        } catch (JRException e) {
            System.err.println("❌ Erro no JasperReports: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("❌ Erro geral: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}