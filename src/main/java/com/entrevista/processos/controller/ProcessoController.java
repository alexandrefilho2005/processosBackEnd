package com.entrevista.processos.controller;

import com.entrevista.processos.model.Processo;
import com.entrevista.processos.service.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    @GetMapping
    public ResponseEntity<Page<Processo>> getAllProcessos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Processo> processos = processoService.findAll(PageRequest.of(page, size));
        return new ResponseEntity<>(processos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Processo> getProcessoById(@PathVariable Long id) {
        Optional<Processo> processo = processoService.findById(id);
        return processo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Processo> createProcesso(
            @RequestParam("npu") String npu,
            @RequestParam("dataCadastro") String dataCadastro,
            @RequestParam("municipio") String municipio,
            @RequestParam("uf") String uf,
            @RequestParam("documento") MultipartFile documento) throws IOException {

        Processo processo = new Processo();
        processo.setNpu(npu);
        processo.setDataCadastro(LocalDate.parse(dataCadastro));
        processo.setMunicipio(municipio);
        processo.setUf(uf);
        processo.setDocumento(documento.getBytes());

        Processo savedProcesso = processoService.save(processo);
        return new ResponseEntity<>(savedProcesso, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Processo> updateProcesso(
            @PathVariable Long id,
            @RequestParam("npu") String npu,
            @RequestParam("dataCadastro") String dataCadastro,
            @RequestParam("municipio") String municipio,
            @RequestParam("uf") String uf,
            @RequestParam(value = "documento", required = false) MultipartFile documento) throws IOException {

        Optional<Processo> processoOpt = processoService.findById(id);
        if (!processoOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Processo processo = processoOpt.get();
        processo.setNpu(npu);
        processo.setDataCadastro(LocalDate.parse(dataCadastro));
        processo.setMunicipio(municipio);
        processo.setUf(uf);
        if (documento != null && !documento.isEmpty()) {
            processo.setDocumento(documento.getBytes());
        }

        Processo updatedProcesso = processoService.save(processo);
        return new ResponseEntity<>(updatedProcesso, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcesso(@PathVariable Long id) {
        processoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/visualizar")
    public ResponseEntity<Processo> marcarVisualizado(@PathVariable Long id) {
        Processo processo = processoService.marcarVisualizado(id);
        if (processo != null) {
            return new ResponseEntity<>(processo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/documento")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long id) {
        Optional<Processo> processoOpt = processoService.findById(id);
        if (processoOpt.isPresent() && processoOpt.get().getDocumento() != null) {
            byte[] documento = processoOpt.get().getDocumento();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(documento);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

