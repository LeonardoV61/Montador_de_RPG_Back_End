package com.draftstd.hydraforge.adapters.out.repository.sessao;

import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemLogRepository extends JpaRepository<MensagemLog, Long> {

}