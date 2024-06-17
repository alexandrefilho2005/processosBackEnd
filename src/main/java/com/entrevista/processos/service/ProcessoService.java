package com.entrevista.processos.service;

import com.entrevista.processos.model.Processo;
import com.entrevista.processos.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    public Page<Processo> findAll(Pageable pageable) {
        return processoRepository.findAll(pageable);
    }

    public Optional<Processo> findById(Long id) {
        return processoRepository.findById(id);
    }

    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    public void deleteById(Long id) {
        processoRepository.deleteById(id);
    }

    public Processo marcarVisualizado(Long id) {
        Optional<Processo> processoOpt = processoRepository.findById(id);
        if (processoOpt.isPresent()) {
            Processo processo = processoOpt.get();
            processo.setDataVisualizacao(LocalDate.now());
            return processoRepository.save(processo);
        }
        return null;
    }
}