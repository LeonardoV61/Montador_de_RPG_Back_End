package com.draftstd.hydraforge.adapters.out.repository.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SistemaRepository extends JpaRepository<Sistema, Long> {

}