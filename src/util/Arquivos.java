/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class Arquivos {
	
    public static final String sepWindows = "\\";
    public static final String sepLinux = "/";
    
    public static String separador = sepWindows;
    private File arq;
    private static int indice = 0;
    private boolean escrita, leitura;
    
    
    //Escrita:
    private FileWriter fw;
    private BufferedWriter bw;
    //Leitura:
    private FileReader fr;
    private BufferedReader br;
    
    public Arquivos(File file) throws IOException{
        arq = file;
        if(!arq.exists())
            arq.createNewFile();
        escrita = false;
        leitura = false;
    }
    public Arquivos() throws IOException{
        this("arquivo"+indice+".txt");
        indice++;
    }
    public Arquivos(String dest) throws IOException{
        arq = new File(dest);
        if(!arq.exists())
            arq.createNewFile();
        escrita = false;
        leitura = false;
    }
    public Arquivos(Diretorios pasta, String nome) throws IOException{
        this((pasta.getDiretorio().getAbsoluteFile())+separador+nome);
    }
    public Arquivos(Diretorios pasta) throws IOException{
        this((pasta.getDiretorio().getAbsoluteFile())+separador+"arquivo"+indice+".txt");
    }
    public boolean arquivoExiste(){
        return arq.exists();
    }
    public void iniciaEscrita() throws IOException{
        fw = new FileWriter(arq,true); 
        bw = new BufferedWriter(fw);
        escrita = true;
    }
    public void escreve(String s) throws IOException{
        if ((escrita)&&(arq.canWrite())){
            bw.write(s);
        }
    }
    public void escreveln(String s) throws IOException{
        escreve(s);
        novaLinha();
    }
    public void escreve() throws IOException{
        novaLinha();
    }
    public void novaLinha() throws IOException{
        if ((escrita)&&(arq.canWrite())){
            bw.newLine();
        }
    }
    public void acabaEscrita() throws IOException{
        bw.close();
        fw.close();
        escrita = false;
    }
    public void iniciaLeitura() throws FileNotFoundException{
        fr = new FileReader(arq);
        br = new BufferedReader(fr);
        leitura = true;
    }
    public String leArquivo() throws IOException{
        String linha;
        if ((leitura)&&(br.ready())){
            linha = br.readLine();
            return linha;
        }
        return null;
    }
    public String leArquivo(int linhas) throws IOException{
        int i;
        String aux = null;
        for(i = 0; i < linhas; i++)
            aux = leArquivo();
        return aux;
    }
    public void acabaLeitura() throws IOException{
        br.close();
        fr.close();
        leitura = false;
    } 
    public File getFile(){
    	return arq;
    }
    public String caminhoCompleto(){
        String nome = (arq.getAbsoluteFile())+"";
        return nome;
    }
    public boolean delete(){
         return arq.delete();
    }
    public String getName(){
        return arq.getName();
    }
    public void abrir() throws IOException{
        java.awt.Desktop.getDesktop().open(arq);
    }
}
