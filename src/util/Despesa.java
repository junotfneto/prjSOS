/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author kaioprado
 * 
 */
public class Despesa {
    public int id;
    public double valor;
    public String nome, tipo;
    
    public Despesa(int id, double valor, String nome, String tipo){
        this.id = id;
        this.valor = valor;
        this.nome = nome;
        this.tipo = tipo;
    }
}
