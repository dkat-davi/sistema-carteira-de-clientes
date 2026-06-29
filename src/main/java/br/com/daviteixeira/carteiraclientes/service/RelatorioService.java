package br.com.daviteixeira.carteiraclientes.service;

import br.com.daviteixeira.carteiraclientes.factory.ConnectionFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RelatorioService {

    public JasperPrint gerarRelatorio(String caminhoRelatorio) {
        return gerarRelatorio(caminhoRelatorio, new HashMap<>());
    }

    public JasperPrint gerarRelatorio(String caminhoRelatorio, Map<String, Object> parametros) {
        try {
            InputStream arquivoRelatorio = getClass().getResourceAsStream(caminhoRelatorio);

            if (arquivoRelatorio == null) {
                throw new RuntimeException("Arquivo de relatório não encontrado: " + caminhoRelatorio);
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(arquivoRelatorio);

            return JasperFillManager.fillReport(
                    jasperReport,
                    parametros,
                    ConnectionFactory.getInstance().getConnection()
            );

        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar relatório.", e);
        }
    }
}