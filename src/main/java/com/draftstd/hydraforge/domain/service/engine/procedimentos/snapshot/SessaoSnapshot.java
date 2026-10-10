package com.draftstd.hydraforge.domain.service.engine.procedimentos.snapshot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoSnapshot {

    private List<ProcedimentoSnapshot> pilha = new ArrayList<>();
}
