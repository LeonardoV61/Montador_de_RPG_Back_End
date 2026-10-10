// package com.draftstd.hydraforge.repository.sessao;

// import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface EfeitoAtivoRepository extends JpaRepository<EfeitoAtivo, Long> {

//     List<EfeitoAtivo> findByEntidadeInstanciaIdAndAtivo(Long idInstancia, boolean isAtivo);

//     List<EfeitoAtivo> findByBatalhaIdAndMomento(Long idBatalha, String momento);
// }