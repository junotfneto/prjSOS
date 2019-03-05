/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sos.telas;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author informatica
 */
public class PanelCam extends javax.swing.JPanel implements Runnable {

    
    private VideoCapture video;
    private Mat frame;
    private BufferedImage buff;
    /**
     * Creates new form PanelCam
     */
    public PanelCam() {
        initComponents();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        new Thread(this).start();
    }
    
    @Override
   public void paintComponent(Graphics g){
       super.paintComponent(g);
       
       if(buff == null){
           return;
       }
       
       g.drawImage(buff,10,20,buff.getWidth(),buff.getHeight(),null);
   }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void run() {
        this.video = new VideoCapture(0); // inicializa a variavel video
        this.frame =  new Mat(); //recebera frames do video
        
        if (video.isOpened()){
            while (true) {
                video.read(frame);
                if (!frame.empty()){
                    MatToBufferedImage(frame);
                    this.repaint();
                }
            }
        }
    }

    private void MatToBufferedImage(Mat mat) {
         
        int altura = mat.width();
        int largura =  mat.height();
        int canais = mat.channels(); //RGB
        
        byte[] source = new byte [altura * largura * canais];
        mat.get (0,0, source);
        buff =  new BufferedImage (altura,largura,BufferedImage.TYPE_3BYTE_BGR );
        final byte[] saida = ((DataBufferByte) buff.getRaster().getDataBuffer()).getData();
        System.arraycopy(source, 0, saida, 0, source.length);
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
