package com.example.aula.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aula.models.Disciplinas;
import com.example.aula.models.DisciplinasRepository;



@Service
@Transactional
public class DisciplinasService {

    @Autowired
    private DisciplinasRepository repo;
     
    public List<Disciplinas> listAll() {
        return repo.findAll();
    }
     
    public void save(Disciplinas disciplina) {
        repo.save(disciplina);
    }
     
    public Optional<Disciplinas> get(Integer id) {
        return  repo.findById(id);
    }
     
    public void delete(Integer id) {
        repo.deleteById(id);
    }
    
    public void atualizar(Disciplinas disciplina) {
    	Optional<Disciplinas> disciplinaEditada = repo.findById(disciplina.getId());

    	System.out.println(disciplina.getId());
        if(disciplinaEditada!=null) {
	    	disciplinaEditada.get().setNome(disciplina.getNome());
	    	disciplinaEditada.get().setId(disciplina.getId());
	    	
	    	repo.save(disciplinaEditada.get());
        }
    }
}
