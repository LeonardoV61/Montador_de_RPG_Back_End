package com.draftstd.hydraforge.ports.in.query.importacao;

public record ArquivoExportadoQueryResponse(String nomeArquivo, String tipoConteudo, byte[] dados) {

}