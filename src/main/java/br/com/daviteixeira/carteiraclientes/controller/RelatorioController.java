package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.service.RelatorioService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;

public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController() {
        this.relatorioService = new RelatorioService();
    }

    public JasperPrint gerarClientesPorLoja() {
        return relatorioService.gerarRelatorio(
                "/relatorios/clientes_por_loja.jrxml"
        );
    }

    public JasperPrint gerarClientesPorVendedor() {
        return relatorioService.gerarRelatorio(
                "/relatorios/clientes_por_vendedor.jrxml"
        );
    }

    public JasperPrint gerarUsuariosPorLoja() {
        return relatorioService.gerarRelatorio(
                "/relatorios/usuarios_por_loja.jrxml"
        );
    }

    public JasperPrint gerarAtendimentosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("DATA_INICIO", Timestamp.valueOf(dataInicio));
        parametros.put("DATA_FIM", Timestamp.valueOf(dataFim));

        return relatorioService.gerarRelatorio(
                "/relatorios/atendimentos_por_periodo.jrxml",
                parametros
        );
    }
}