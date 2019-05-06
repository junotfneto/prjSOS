/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sos.telas;

import java.sql.*;
import br.com.sos.dal.ModuloConexao;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import java.lang.String;
import java.util.Calendar;

// a linha abaixo importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;
import util.*;

/**
 *
 * @author informatica
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
     protected MaskFormatter createFormatter(String s) {
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter(s);
            } catch (java.text.ParseException exc) {
                System.err.println("formatter is bad: " + exc.getMessage());
                System.exit(-1);
            }
            return formatter;
        }
     
     private boolean validaRG(String rg){
        //if(rg.length() < 9 || rg.length() > 11) return false;
        for(int i = 0; i < rg.length(); i++){
            if(Character.isDigit(rg.charAt(i))) return false;
        }
        return true;
    }
    
    private boolean isNumber(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    private boolean validaEmail(String s){
        if(!s.contains("@")) return false;
        if(!s.contains(".")) return false;
        return true;
    }

    ///método para adicionar clientes
    private void adicionar() {
        String sql = "insert into tbclientes(nomecli,endcli,fonecli,emailcli,cpfcli,rgcli,datacadastrocli) values(?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEndereco.getText());
            pst.setString(3, txtCliTel.getText());
            pst.setString(4, txtCliMail.getText());
            pst.setString(5, txtCliCPF.getText());
            pst.setString(6, txtCliRG.getText());
            java.util.Date data = DatCad.getDate();
            pst.setString(7, );

            // validação dos campos obrigatórios
            if(!validaRG(txtCliRG.getText())){
                JOptionPane.showMessageDialog(null, "Preencha o RG com apenas números");
                return;
            }
            if(!validaEmail(txtCliMail.getText())){
                JOptionPane.showMessageDialog(null, "Digite um email válido");
                return;
            }
            if ((txtCliNome.getText().isEmpty()) || (txtCliTel.getText().isEmpty()) || (txtCliCPF.getText().isEmpty()) || (txtCliRG.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                /// a linha abaixo atualiza a tabela usuários com os dados do formulário
                int adicionado = pst.executeUpdate();
                /// a linha abaixo serve de apoio ao entendimento da lógica
                System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com Sucesso!");
                    // as linhas abaixo, limpam os campos
                    txtCliNome.setText(null);
                    txtCliTel.setText(null);
                    txtCliEndereco.setText(null);
                    txtCliCPF.setText(null);
                    txtCliRG.setText(null);
                    txtCliMail.setText(null);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //método para pesquisar clientes pelo nome com filtro
    private void pesquisar_cliente() {
        String sql = "select * from tbclientes where nomecli like ?";
        try{
            pst = conexao.prepareStatement(sql);
            //passando o contéudo da caixa de pesquisa para o ?
            //Atenção ao % que é a continuação dessa string SQL
            pst.setString(1,txtCliPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para pesquisar a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
            
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
            
    }
    
    /// método para setor os campos do formulário com o contéudo da tabela
    
    public void setar_campos(){
        int setar = tblClientes.getSelectedRow();
        
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEndereco.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliTel.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliCPF.setText(tblClientes.getModel().getValueAt(setar, 5).toString());
        txtCliRG.setText(tblClientes.getModel().getValueAt(setar, 6).toString());
        txtCliMail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        DatCad1.setText(tblClientes.getModel().getValueAt(setar, 7).toString());
        
        txtIdCli.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        
        // a linha abaixo desabilita o botão adicionar.
        //btnCliAdicionar.setEnabled(false);
    }
    
   
    
    
    // método para alterar dados do cliente
    
    
    private void alterar(){
        String sql="update tbclientes set nomecli=?,endcli=?,fonecli=?,emailcli=?,cpfcli=?,rgcli=? where idcli=?";
        try{
            pst=conexao.prepareStatement(sql);
            
            pst.setString(1,txtCliNome.getText());
            pst.setString(2,txtCliEndereco.getText());
            pst.setString(3,txtCliTel.getText());
            pst.setString(4,txtCliMail.getText());
            pst.setString(6,txtCliRG.getText());
            pst.setString(5,txtCliCPF.getText());
            pst.setString(7,txtIdCli.getText());
            
            // a estrutura abaixo é usada para confirmar a alteração dos dados do usuário
            if ( (txtCliNome.getText().isEmpty()) || (!ValidaCPF.isCPF(txtCliCPF.getText()))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                /// a linha abaixo atualiza a tabela usuários com os dados do formulário
                int adicionado = pst.executeUpdate();
                /// a linha abaixo serve de apoio ao entendimento da lógica
                System.out.println(adicionado);
                
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso!");
                    // as linhas abaixo, limpam os campos
                    
                    txtCliNome.setText(null);
                    txtCliTel.setText(null);
                    txtCliEndereco.setText(null);
                    txtCliCPF.setText(null);
                    txtCliRG.setText(null);
                    txtCliMail.setText(null);
                    DatCad.setDate(null);
                    
                    btnCliAdicionar.setEnabled(true);
                    
                }
            }
            
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }

    
    //método responsável pela remoção de clientes
    
    private void remover() {
        //a estrutura abaixo confirma a remoção do usuário
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este usuário ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbclientes where idcli=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdCli.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente Removido com sucesso");
                    txtCliNome.setText(null);
                    txtCliTel.setText(null);
                    txtCliEndereco.setText(null);
                    txtCliCPF.setText(null);
                    txtCliRG.setText(null);
                    txtCliMail.setText(null);
                    DatCad.setDate(null);

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        BtnCliEditar = new javax.swing.JButton();
        txtCliNome = new javax.swing.JTextField();
        txtCliCPF = new javax.swing.JFormattedTextField(createFormatter("###########"));
        txtCliMail = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtCliEndereco = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCliRG = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnCliAdicionar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnCliEditar = new javax.swing.JButton();
        lblCadastro = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        datacadastro = new javax.swing.JLabel();
        txtCliTel = new javax.swing.JFormattedTextField(createFormatter("(##) #####-####"));
        DatCad1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtIdCli = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        DatCad = new com.toedter.calendar.JDateChooser();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel9.setText("Telefone");

        BtnCliEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sos/icones/delete.png"))); // NOI18N
        BtnCliEditar.setToolTipText("Excluir");
        BtnCliEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        BtnCliEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnCliEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCliEditarActionPerformed(evt);
            }
        });

        jLabel1.setText("Nome");

        jLabel2.setText("CPF");

        jLabel3.setText("RG");

        jLabel4.setText("Endereço");

        btnCliAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sos/icones/create.png"))); // NOI18N
        btnCliAdicionar.setToolTipText("Adicionar");
        btnCliAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        btnCliAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAdicionarActionPerformed(evt);
            }
        });

        jLabel7.setText("E-mail");

        btnCliEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sos/icones/update.png"))); // NOI18N
        btnCliEditar.setToolTipText("Editar");
        btnCliEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        btnCliEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliEditarActionPerformed(evt);
            }
        });

        lblCadastro.setText("Data de Cadastro");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sos/icones/pesquisar.png"))); // NOI18N

        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Email", "Telefone"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jLabel6.setText("Pesquisar");

        jLabel8.setText("ID Cliente");

        txtIdCli.setEnabled(false);

        jButton1.setText("Limpar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblCadastro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DatCad, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(datacadastro))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(btnCliAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(123, 123, 123)
                                .addComponent(btnCliEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnCliEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCliNome)
                                    .addComponent(txtCliEndereco)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCliRG, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(txtCliTel, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(DatCad1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(48, 48, 48)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtCliCPF)
                                    .addComponent(txtCliMail))))))
                .addGap(17, 17, 17))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BtnCliEditar, btnCliAdicionar, btnCliEditar});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jButton1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCliMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtCliTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCliRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtCliCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(datacadastro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCadastro))
                    .addComponent(DatCad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(DatCad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCliEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCliEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCliAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnCliEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCliEditarActionPerformed
        // Chamando o método para remover cliente
        remover();
    }//GEN-LAST:event_BtnCliEditarActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void btnCliAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAdicionarActionPerformed
        // método para adicionar clientes
        if(txtIdCli.getText().isEmpty()){
            adicionar();
        }else{
            new Cadastrar(null,true).setVisible(true);
        }
        
        
    }//GEN-LAST:event_btnCliAdicionarActionPerformed

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // o evento é do tipo enquanto for digitando em tempo real faça o que foi declado abaixo
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // Este evento será usado para setar os campos da tabela, clicando com o mouse.
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnCliEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliEditarActionPerformed
        // Chamando o método para alterar clientes.
        alterar();
    }//GEN-LAST:event_btnCliEditarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        txtCliNome.setText("");
        txtCliTel.setText("");
        txtCliEndereco.setText("");
        txtCliCPF.setText("");
        txtCliRG.setText("");
        txtCliMail.setText("");
        DatCad1.setText("");
        txtIdCli.setText("");
        txtCliPesquisar.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCliEditar;
    private com.toedter.calendar.JDateChooser DatCad;
    private javax.swing.JTextField DatCad1;
    private javax.swing.JButton btnCliAdicionar;
    private javax.swing.JButton btnCliEditar;
    private javax.swing.JLabel datacadastro;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCadastro;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliCPF;
    private javax.swing.JTextField txtCliEndereco;
    private javax.swing.JTextField txtCliMail;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtCliRG;
    private javax.swing.JTextField txtCliTel;
    private javax.swing.JTextField txtIdCli;
    // End of variables declaration//GEN-END:variables

}
