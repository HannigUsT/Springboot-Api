package com.example.aula.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.aula.models.Disciplinas;
import com.example.aula.service.DisciplinasService;



@Controller
@ResponseBody
public class DisciplinasControllers {
	@Autowired
	  private DisciplinasService service;
	 
	 @GetMapping("/disciplinas")
	 public List<Disciplinas> list() {
		 try {
			 return service.listAll();}
		 catch(Exception e) {
			 List<Disciplinas> vazio = null;
			 return vazio;
		 }
	 }
	 
	 @GetMapping("/disciplina/{id}")
	 public  Optional<Disciplinas> disciplina(@PathVariable Integer id) {

		 try {
			 return service.get(id);}
		 catch(Exception e) {
			 Optional<Disciplinas> vazio = null;
			 return vazio;
		 }
	 }
	 
	 @PostMapping("/novadisciplina")
	 public String novaDisciplina(@RequestBody Disciplinas disciplina) {
		 try {
			 service.save(disciplina);
			 return "{mensagem:'salvo com sucesso'}";}
		 catch(Exception e) {
			 return "{mensagem:'Erro ao cadastrar'}";
		 }
	 }
	 
	 @PutMapping("/editardisciplina")
	 public String editarEmpresa(@RequestBody Disciplinas disciplina) {
		 try {
		 service.atualizar(disciplina);
		 return "{mensagem:'editar com sucesso'}";
		 }
		 catch(Exception e) {
			 return "{mensagem:'Erro ao editar'}";
		 }
	 }
	     
	  
	  @DeleteMapping("/deletardisciplina/{id}")
	  public String deletarDisciplina(@PathVariable Integer id) {
			 try {
				 service.delete(id);
				 return "{mensagem:'apagado com sucesso'}";}
			 catch(Exception e) {
				 return "{mensagem:'Erro ao deletar'}";
			 }
		 }
	 
	 
}
