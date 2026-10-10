package com.draftstd.hydraforge.adapters.out.repository.mecanica;

import com.draftstd.hydraforge.domain.model.mecanica.Primitivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimitivoRepository extends JpaRepository<Primitivo, Long> {

}