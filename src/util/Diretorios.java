/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author kaio prado
 */
public class Diretorios {

    public static final String sepWindows = "\\";
    public static final String sepLinux = "/";
    
    public static String separador = sepWindows;
    
    File pasta;
    
    public Diretorios(File dir){
        pasta = dir;
    }
    public Diretorios(String nome) throws FileNotFoundException{
        pasta = new File(nome);
        if (!pasta.exists())
            pasta.mkdir();
    }
    public Diretorios(Diretorios maior, String nome) throws FileNotFoundException{
        this ((maior.getDiretorio().getAbsoluteFile())+separador+nome);
    }
    public File getDiretorio(){
        return pasta;
    }
    public String caminhoCompleto(){
        String nome = (pasta.getAbsoluteFile())+"";
        return nome;
    }
    public File getFile(){
    	return pasta;
    }
    public boolean delete(){
        return deleteDir(pasta);
    }
    
    public static boolean deleteDir(File dir) {
        
        if (dir.isDirectory()) {
            boolean success;
            String[] children = dir.list();  
            for (int i=0; i<children.length; i++) {  
                success = deleteDir(new File(dir, children[i] ));  
                if (!success) {  
                    return false;  
                }  
        }  
    }  
  
    // The directory is now empty so delete it  
        return dir.delete();  
    }
    public ArrayList<Diretorios> listDiretorios(){
        ArrayList<Diretorios> al = new ArrayList<Diretorios>();
        File[] aux = pasta.listFiles();
        int i;
        for(i = 0; i < aux.length; i++){
            if(aux[i].isDirectory()){
                al.add(new Diretorios(aux[i]));
            }
        }
        return al;
    }
    public ArrayList<Arquivos> listArquivos(){
        ArrayList<Arquivos> al = new ArrayList<Arquivos>();
        File[] aux = pasta.listFiles();
        int i;
        for(i = 0; i < aux.length; i++){
            if(!aux[i].isDirectory()){
                try {
                    al.add(new Arquivos(aux[i]));
                } catch (IOException ex) {
                    Logger.getLogger(Diretorios.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return al;
    }
    public void abrir() throws IOException{
        java.awt.Desktop.getDesktop().open(pasta);
    }

}