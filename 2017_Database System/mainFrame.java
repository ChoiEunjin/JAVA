
package javatermproject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class mainFrame extends javax.swing.JFrame {
public Connection con = MyCon.makeCon();
JTextField[] tf = new JTextField[8];
JLabel[] lf = new JLabel[8];


    public mainFrame() {
        initComponents();       // Constructor
        addNode();
        
        tf[0] = jTextField1;
        tf[1] = jTextField2;
        tf[2] = jTextField3;
        tf[3] = jTextField4;
        tf[4] = jTextField5;
        tf[5] = jTextField6;
        tf[6] = jTextField7;
        tf[7] = jTextField8;
        
        lf[0] = jLabel1;
        lf[1] = jLabel2;
        lf[2] = jLabel3;
        lf[3] = jLabel4;
        lf[4] = jLabel5;
        lf[5] = jLabel6;
        lf[6] = jLabel7;
        lf[7] = jLabel8;
        clearFields();
    }
    
    /* 
        1, Customer
        1.1. addCustomerRS : add a record using ResultSet object
        1.2. updateCustomerRS : update a record using ResultSet method
        1.3. deleteCustomerRS : delete a record using ResultSet object
        1.4. searchCustomerID : print the data that ID is same as input data
    */
    private void addCustomerRS(){
        String st = mySelectedNode();
        String query = "select * from "+st+"";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); // SCROLL both wave
            rs = pst.executeQuery();
            rs.moveToInsertRow();
            // create in new row
            rs.updateInt(1, Integer.parseInt(jTextField1.getText()));
            rs.updateString(2, jTextField2.getText());
            rs.updateString(3, jTextField3.getText());
            rs.updateString(4, jTextField4.getText());
            rs.updateString(5, jTextField5.getText());
            rs.updateString(6, jTextField6.getText());            
            rs.updateInt(7, Integer.parseInt(jTextField7.getText()));
            rs.insertRow();
            jTextArea3.append(" row is added..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void updateCustomerRS(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());
        String query = "select * from Customer";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            rs.absolute(rownum+1);
            rs.updateString(2, jTextField2.getText());
            rs.updateString(3, jTextField3.getText());
            rs.updateString(4, jTextField4.getText());
            rs.updateString(5, jTextField5.getText());
            rs.updateString(6, jTextField6.getText());
            rs.updateInt(7, Integer.parseInt(jTextField7.getText()));
            rs.updateRow();
            rs.moveToCurrentRow();
            jTextArea3.append(" row is updated..." + "\n");
            pst.close();       
        }catch(Exception e){
            jTextArea3.append("Error in updateCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void deleteCustomerRS(){
        int rownum = jTable1.getSelectedRow();
        String id = jTable1.getModel().getValueAt(rownum, 0).toString();
        String query = "select * from Customer";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            
            rs.absolute(rownum+1);
            rs.deleteRow();
            rs.moveToCurrentRow();
            jTextArea3.append(" row is deleted..." + "\n");
            pst.close();
            rs.close();  
        }catch(SQLException e){
            jTextArea3.append("Error in deleteCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void searchCustomerID(){
        PreparedStatement pst = null;
        ResultSet rs = null;
        int cnum = Integer.parseInt(jTextField1.getText());
        String query = "select * from Customer where C_NUM = "+cnum+"";        
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            jTable1.clearSelection();
            jTable1.setModel(rsToTableModel(rs));
            jTextArea3.append("search is finished..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }

    /*
        2. PC_Order
        2.1. addPCOrderSP : add / insert record using a stored procedure
        2.2. updatePCOrderSP : update a record using a stored procedure
        2.3. deletePCOrderSP : delete a record using a stored procedure
        2.4. searchPCOrderID : print the data that ID is same as input data
    */ 
    private void addPCOrderSP(){
        String sql = "{call mktAddPCOrderP(?, ?, ?, ?, ?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(sql);
            cst.setInt(1, Integer.parseInt(jTextField1.getText()));
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
            cst.setInt(4, Integer.parseInt(jTextField4.getText()));
            cst.setInt(5, Integer.parseInt(jTextField5.getText()));
            cst.execute();
            jTextArea3.append(" row is added..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addPCOrderSP()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void updatePCOrderSP(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());
        String query = "{call mktUpdatePCOrderP(?, ?, ?, ?, ?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(query);
            cst.setInt(1, id);
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
            cst.setInt(4, Integer.parseInt(jTextField4.getText()));
            cst.setInt(5, Integer.parseInt(jTextField5.getText()));
            cst.execute();
            jTextArea3.append(" row is updated..." + "\n");
            cst.close();
        }catch(Exception e){
            jTextArea3.append("Error in updatePCOrderUS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");    
        }
    }
    
    private void deletePCOrderSP(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());
        String query = "{call mktDeletePCOrderP(?)}";
        CallableStatement cst = null;
        try{
            cst = con.prepareCall(query);
            cst.setInt(1, id);
            cst.execute();
            jTextArea3.append(" row is deleed..." + "\n");
            cst.close();  
        }catch(Exception e){
            jTextArea3.append("Error in deletePCOrderP()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void searchPCOrderID(){
        PreparedStatement pst = null;
        ResultSet rs = null;
        int onum = Integer.parseInt(jTextField1.getText());
        String query = "select * from PC_Order where O_Number = "+onum+"";        
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            jTable1.clearSelection();
            jTable1.setModel(rsToTableModel(rs));
            jTextArea3.append("search is finished..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    
    /*
        3. Product
        3.1. addProductIS : Insert records using IS(Insert statement) in product table.
        3.2. updateProductUS : Update records using US(Update statement) in product table.
        3.3. deleteProductDS : Delete records using DS(Delete statement) in product table.
        3.4. searchProductID : Print the data that ID is same as input data
    */
    private void addProductIS(){
        String query = "insert into Product values(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(jTextField1.getText()));
            pst.setString(2, jTextField2.getText());                    
            pst.setInt(3, Integer.parseInt(jTextField3.getText()));
            pst.setString(4, jTextField4.getText());
            pst.setString(5, jTextField5.getText());
            pst.setString(6, jTextField6.getText());        
            pst.setString(7, jTextField7.getText());        
            pst.setString(8, jTextField8.getText());
            pst.executeUpdate();
            jTextArea3.append(" row is added..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addProductIS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void updateProductUS(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());        
        String query = "update Product set P_Name = ?, P_Price = ?, P_Type = ?, P_EnterDate = ?, P_Company = ?, P_Area = ?, P_Volume = ? where P_NUM = ?";
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(query);
            pst.setString(1, jTextField2.getText());                    
            pst.setInt(2, Integer.parseInt(jTextField3.getText()));
            pst.setString(3, jTextField4.getText());                    
            pst.setString(4, jTextField5.getText());
            pst.setString(5, jTextField6.getText());        
            pst.setString(6, jTextField7.getText());        
            pst.setString(7, jTextField8.getText());
            pst.setInt(8, id);
            pst.executeUpdate();
            jTextArea3.append(" row is updated..." + "\n");
            pst.close();
        }catch(Exception e){
            jTextArea3.append("Error in updateProductUS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void deleteProductDS(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());     
        String query = "delete from Product where P_NUM = ?";
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
            jTextArea3.append(" row is deleted..." + "\n");
            pst.close();  
        }catch(Exception e){
            jTextArea3.append("Error in deleteProductDS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void searchProductID(){
        PreparedStatement pst = null;
        ResultSet rs = null;
        int pnum = Integer.parseInt(jTextField1.getText());
        String query = "select * from Product where P_NUM = "+pnum+"";       
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            jTable1.clearSelection();
            jTable1.setModel(rsToTableModel(rs));
            jTextArea3.append("search is finished..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }        
    }
    
    /*
        4. Supplier
        4.1. addSupplierIS() : Insert records using IS into statement in Supplier Table
        4.2. updateSupplierUS() : Update records using US into statement in Supplier Table
        4.3. deleteSupplierDS() : Delete records using DS into statement in Supplier Table
        4.4. searchSupplierID : Print the data that ID is same as input data
    */

    private void addSupplierIS(){
        String query = "insert into Supplier values(?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(jTextField1.getText()));
            pst.setString(2, jTextField2.getText());
            pst.setInt(3, Integer.parseInt(jTextField3.getText()));
            pst.setInt(4, Integer.parseInt(jTextField4.getText()));            
            pst.setString(5, jTextField5.getText());
            pst.setString(6, jTextField6.getText());
            pst.executeUpdate();
            jTextArea3.append(" row is added..." + "\n");          
        }catch(Exception e){
            jTextArea3.append("Error in addSupplierIS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void updateSupplierUS(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());        
        String query = "update Supplier set S_Address = ?, TotalStock = ?, P_NUM = ?, S_Date = ?, S_Time = ? where S_NUM = ?";
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(query);            
            pst.setString(1, jTextField2.getText());
            pst.setInt(2, Integer.parseInt(jTextField3.getText()));
            pst.setInt(3, Integer.parseInt(jTextField4.getText()));            
            pst.setString(4, jTextField5.getText());
            pst.setString(5, jTextField6.getText());
            pst.setInt(6, id);
            pst.executeUpdate();
            jTextArea3.append(" row is updated..." + "\n");
            pst.close();
        }catch(Exception e){
            jTextArea3.append("Error in updateSupplierUS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    private void deleteSupplierDS(){
        int rownum = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(rownum, 0).toString());     
        String query = "delete from Supplier where S_NUM = ?";
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
            jTextArea3.append(" row is deleted..." + "\n");
            pst.close();  
        }catch(Exception e){
            jTextArea3.append("Error in deleteSupplierDS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
     
    private void searchSupplierID(){
        PreparedStatement pst = null;
        ResultSet rs = null;
        int snum = Integer.parseInt(jTextField1.getText());
        String query = "select * from Supplier where S_NUM = "+snum+"";         
        try{
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            jTable1.clearSelection();
            jTable1.setModel(rsToTableModel(rs));
            jTextArea3.append("search is finished..." + "\n");
        }catch(Exception e){
            jTextArea3.append("Error in addCustomerRS()..." + "\n");
            jTextArea3.append(e.getMessage() + "\n");
        }
    }
    
    
    private void clearFields(){
        for(int i=0; i<tf.length; i++){
            tf[i].setText("");
            lf[i].setText("");
            tf[i].setVisible(false);
            lf[i].setVisible(false);
            //jPanel3.setVisible(false);
        }
    }
    
    private void clearTextFields(){
        int noc = jTable1.getColumnCount(); // number of columns

        for(int i = 0; i<noc; i++){
            tf[i].setText(" ");            
        }
    }
    
    private void showFields(){
        clearFields();
        int sr = jTable1.getSelectedRow();
        int noc = jTable1.getColumnCount(); // number of columns
        for(int i = 0; i<noc; i++){
            
            if(jTable1.getValueAt(sr, i) == null)
            {
                tf[i].setText(" ");
                tf[i].setVisible(true);     
                lf[i].setText(jTable1.getColumnName(i));        // function from the jTable class
                lf[i].setVisible(true);
            }
            else
            {
                tf[i].setText(jTable1.getValueAt(sr, i).toString());
                tf[i].setVisible(true);                
                
                lf[i].setText(jTable1.getColumnName(i));        // function from the jTable class
                lf[i].setVisible(true);
                
            }            
        }
        
    }
    
    private TableModel rsToTableModel(ResultSet rs){
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            // data for columns 
            Vector cnames = new Vector();
            int noc = rsmd.getColumnCount();
            for(int i = 1; i <= noc; i++)
                cnames.addElement(rsmd.getColumnLabel(i));
            
            // data for rows
            Vector rows = new Vector();
            while(rs.next()){   // if there is any recard, while roop will continue
                Vector newRow = new Vector();
                for(int i = 1; i <= noc; i++)
                    newRow.addElement(rs.getObject(i));   // get one row
                rows.addElement(newRow);
            }
            return new DefaultTableModel(rows, cnames);
        }catch(Exception e){return null;}
    }
            
    private void showTableData(String st){
        String query = "select * from "+st+"";
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            jTable1.setModel(rsToTableModel(rs));
        }catch(Exception e){}
        
    }
    
    private String mySelectedNode(){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
        String ns = node.getUserObject().toString(); // give the printed name
        return ns;
    }
    
    
    private void addNode(){
        DefaultTreeModel tModel = (DefaultTreeModel)jTree1.getModel();

       // Create new node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Market");
        DefaultMutableTreeNode tables = new DefaultMutableTreeNode("Tables");
        DefaultMutableTreeNode reports = new DefaultMutableTreeNode("Reports");
        DefaultMutableTreeNode utl = new DefaultMutableTreeNode("Utilities");
        DefaultMutableTreeNode about = new DefaultMutableTreeNode("About");
        DefaultMutableTreeNode ext = new DefaultMutableTreeNode("Exit");
        
        tModel.setRoot(root);
        root.add(tables);
        root.add(reports);
        root.add(utl);
        root.add(about);
        root.add(ext);
        
        DefaultMutableTreeNode t1 = new DefaultMutableTreeNode(Tables.Customer);
        DefaultMutableTreeNode t2 = new DefaultMutableTreeNode(Tables.Product);
        DefaultMutableTreeNode t3 = new DefaultMutableTreeNode(Tables.Supplier);
        DefaultMutableTreeNode t4 = new DefaultMutableTreeNode(Tables.PC_Order);
        tables.add(t1);
        tables.add(t2);
        tables.add(t3);
        tables.add(t4);
        
        tModel.reload();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane4.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

        jTree1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("First Name");

        jLabel2.setText("Middle");

        jLabel3.setText("Last Name");

        jTextField3.setText("jTextField3");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField2.setText("jTextField2");

        jTextField1.setText("jTextField1");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        jLabel7.setText("jLabel7");

        jLabel8.setText("jLabel8");

        jTextField4.setText("jTextField4");

        jTextField5.setText("jTextField5");

        jTextField6.setText("jTextField6");

        jTextField7.setText("jTextField7");

        jTextField8.setText("jTextField8");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel8))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(jTextField4)
                            .addComponent(jTextField5)
                            .addComponent(jTextField6)
                            .addComponent(jTextField7)
                            .addComponent(jTextField8))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setText("New");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Update");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setText("Search");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane5.setViewportView(jTextArea3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearTextFields();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Add button
        String st = mySelectedNode();   // return string
        if (st == Tables.Customer.toString())
            addCustomerRS();
        else if (st == Tables.Product.toString())
            addProductIS();
        else if (st == Tables.Supplier.toString())
            addSupplierIS();
        else if (st == Tables.PC_Order.toString())
            addPCOrderSP();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed

    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        // TODO add your handling code here:
        String st = mySelectedNode();
        showTableData(st);
    }//GEN-LAST:event_jTree1ValueChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        showFields();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Update Button
        String st = mySelectedNode();   // return string
        if (st == Tables.Customer.toString())
            updateCustomerRS();
        else if (st == Tables.Product.toString())
            updateProductUS();
        else if (st == Tables.Supplier.toString())
            updateSupplierUS();
        else if (st == Tables.PC_Order.toString())
            updatePCOrderSP();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Search Button
        String st = mySelectedNode();   // return string
        if (st == Tables.Customer.toString())
            searchCustomerID();
        else if (st == Tables.Product.toString())
            searchProductID();
        else if (st == Tables.Supplier.toString())
            searchSupplierID();
        else if (st == Tables.PC_Order.toString())
            searchPCOrderID();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Delete button
        String st = mySelectedNode();   // return string
        if (st == Tables.Customer.toString())
            deleteCustomerRS();
        else if (st == Tables.Product.toString())
            deleteProductDS();
        else if (st == Tables.Supplier.toString())
            deleteSupplierDS();
        else if (st == Tables.PC_Order.toString())
            deletePCOrderSP();
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
