package br.com.sos.dal;
// importando bibliotecas

import java.sql.*;

/**
 * Sistema OS
 * @author Kaio
 */
public class ModuloConexao {
    //criando um método responsável por estabelecer
    //uma conexão com o banco

    public static Connection conector() {
        //criando uma variável especial para 
        //estabelecer uma conexão com o banco
        java.sql.Connection conexao = null;
        //carregando o driver correspondente
        String driver = "com.mysql.jdbc.Driver";
        //armazenando informações referente ao 
        //banco de dados
        String url = "jdbc:mysql://localhost:3306/dbinfox";
        String user = "root";
        String password = "";
        String arquivo = "jdbc:sqlite:dbinfox.db";
        //estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            //conexao = DriverManager.getConnection(arquivo);
            return conexao;

        } catch (Exception e) {
            //a linha abaixo serve de apoio para esclarecer o erro
            //System.out.println(e);
            System.out.println("Erro\n");
            e.printStackTrace();
            return null;
        }

    }
}