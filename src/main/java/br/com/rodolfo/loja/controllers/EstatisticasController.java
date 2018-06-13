package br.com.rodolfo.loja.controllers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estatisticas")
public class EstatisticasController {
    
    @PersistenceContext
    EntityManager em;

    @Autowired
    private Statistics statistics;

    @RequestMapping
    public String index(Model model) {
        return "estatisticas/index";
    }

    @RequestMapping("/limpar")
    public String invalidar() {
        
        statistics.clear();

        return "redirect:/estatisticas";
    }

}