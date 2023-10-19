import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.lang.System.Logger;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import java.sql.*;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.ImageIcon;


public class Main {

	private JFrame frmStoreSystem;
	private JTextField productNameTextField;
	private JTextField priceTextField;
	private JTextField quantityTextField;
	private JComboBox productIDComboBox = new JComboBox();
	private JScrollPane Table = new JScrollPane();
	private JTable table;
	
	
	
	
	
	
	
	
	
// Check if Product Name is exist or not 
// if not : Insert in oracle database 
//THEN Clear all data in application "kda kda mawgoda fy oracle"
//THEN copy data in oracle database to application
	void Insert(String insertProductName , int insertPrice , int insertQuantity) {
		try { 
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","warehouse","sys"); 
            
//Return True if Product Name is exist from oracle data base "Function CheckDataIsExist e7na elly 3amlenha"
// 5ly balk e7na 3awzenha non-exist 34an n3ml insert "can't be Insert when product is existed"
			if(CheckDataIsExist(insertProductName))
				JOptionPane.showMessageDialog(null,"product is already exist");
			else {
				Statement st = conn.createStatement(); 
				String query="INSERT INTO product (PRODUCT_NAME,PRICE,QUANTITY) VALUES ('"+insertProductName +"',"+insertPrice+" , "+insertQuantity +")";
				st.executeUpdate(query); 
				
				
				
				DisplayData();
	            conn.close(); 
	            JOptionPane.showMessageDialog(null,"Data inserted");
			}
			
            } 
		catch (Exception e) { 
                System.err.println(e.getMessage()); 
            }
	}

	
	
	
	
	void Delete(String ID) {
		
		try { 
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","warehouse","sys"); 
            Statement st = conn.createStatement(); 
			String query="DELETE FROM product WHERE product_id = "+ID;
			st.executeUpdate(query);
			DisplayData();
			
            } 
		catch (Exception e) { 
                System.err.println(e.getMessage()); 
            }
		
	}

	
	
	
	
	void Search(String ID) {
		try { 
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","warehouse","sys"); 
 // elquery dy elmafrood btraga3 table feha column wa7da "product_ID" wy feha row wa7da "el ID elly 3awzenha"
            String query="SELECT * FROM product WHERE product_id = '"+ID+"'";
            PreparedStatement st = conn.prepareStatement(query); 
            ResultSet rs = st.executeQuery(); 
            
// b3d ma gebt eldata mn oracle wy 5zntaha fy variable rs b3ml loop wa7da bs bgeb eldata mn rs wy ba7otaha fy eltextField
			while(rs.next()) {
				productNameTextField.setText(rs.getString("product_name"));
				priceTextField.setText(rs.getString("price"));
				quantityTextField.setText(rs.getString("quantity"));
			}
			
            } 
		catch (Exception e) { 
                System.err.println(e.getMessage()); 
            }
	}

	
	
	
// Check if Product Name is existed
// if existed : Update in oracle database 
//THEN Clear all data(table and ComboBox) in application "kda kda mawgoda fy oracle"
//THEN copy data in oracle database to application
	void Update(String name) {
		try { 
//Return True if Product Name is exist from oracle data base "Function CheckDataIsExist e7na elly 3amlenha"
//5ly balk e7na 3awzenha exist 34an n3ml update "can't be Update when product is not existed"
			if(!CheckDataIsExist(name))
				JOptionPane.showMessageDialog(null,"No data exists to update");
			else {
				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","warehouse","sys"); 
	            String query="UPDATE product SET price = "+priceTextField.getText()+", quantity= "+quantityTextField.getText()+" WHERE product_name = '"+name+"'";
	            Statement st = conn.createStatement(); 
	            st.executeUpdate(query); 
	            DisplayData();
			}
            
			
            } 
		catch (Exception e) { 
                System.err.println(e.getMessage()); 
            }
	}
	
	
	
	
	
	
	
	
	
	boolean CheckDataIsExist(String Data) {
		boolean check=true;
		try { 
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","warehouse","sys"); 

// elquery dy elmafrood btraga3 table feha column wa7da "product_name" wy feha row wa7da "el ID elly 3awzenha"
            String query="SELECT * FROM product WHERE product_name = '"+Data+"'";
            PreparedStatement st = conn.prepareStatement(query); 
            ResultSet rs = st.executeQuery(); 
            
// rs.next() btreturn false lw mfe4 table zahart fel SQL Commands "byb2a maktob ta7t 3la el4mal fy oracle no data found"
            check = rs.next();
            
		} 
		
		catch (Exception e) { 
                System.err.println(e.getMessage()); 
            }
		
		return check;
	}

	
	
	
	
	void DisplayData() {
		try { 
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","warehouse","sys"); 

            String query="SELECT * FROM product ORDER BY product_id";
            PreparedStatement st = conn.prepareStatement(query); 
            
 // store table "all data in oracle" in variable rs ordered by ID
            ResultSet rs = st.executeQuery(); 
            
            
// Clear all data in table "Application"
            table.setModel(new DefaultTableModel(
        			new Object[][] {
        				
        			},
        			new String[] {
        				"ProductID", "Product Name", "Price", "Quantity"
        			}
        		));

 // Clear all data in ComboBox "Application"
            productIDComboBox.setModel(new DefaultComboBoxModel(new String[] {}));
            
            
//loop and add data from oracle row by row in table and ComboBox
            while(rs.next()) {
            	
            	productIDComboBox.addItem(rs.getString("product_id"));
            	
            	
    			DefaultTableModel model = (DefaultTableModel) table.getModel();
    			Object[] rowData = {rs.getString("product_id"),rs.getString("product_name"), rs.getString("price"), rs.getString("quantity")};
    			model.addRow(rowData);
            }

            
		}
            
		catch (Exception e) { 
                System.err.println(e.getMessage()); 
            }
		
	}

	
	
	
	
	boolean isText(String input) {
        return input.matches("[a-zA-Z][a-zA-Z0-9 ]*");     // return true if string has (letters ONLY)  <------------->    [a-zA-Z]+
        
                 // return true if string has (letters THENNNNN numbers) or (letters ONLY)  <------------->    [a-zA-Z][a-zA-Z0-9 ]*
    }
	
	
	
	
	
	
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmStoreSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		

		

			
// Create Frame
		frmStoreSystem = new JFrame();
		frmStoreSystem.setTitle("Store System");
		frmStoreSystem.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\khaled\\Downloads\\Icon2.jpg"));
		frmStoreSystem.setResizable(false);
		frmStoreSystem.setBounds(500, 150, 551, 575);
		frmStoreSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStoreSystem.getContentPane().setLayout(null);
		
		

		

// Create Label : Title
		JLabel Title = new JLabel("Java | Store System");
		Title.setIcon(new ImageIcon("C:\\Users\\khaled\\Downloads\\Logo.jpg"));
		Title.setFont(new Font("Tahoma", Font.BOLD, 35));
		Title.setBounds(0, 10, 538, 138);
		frmStoreSystem.getContentPane().add(Title);
		
		

		

		
// Create Label : Product Name
		JLabel productNameLabel = new JLabel("Product Name");
		productNameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		productNameLabel.setBounds(10, 167, 91, 19);
		frmStoreSystem.getContentPane().add(productNameLabel);
		
// Create TextField : Product Name
		productNameTextField = new JTextField();
		productNameTextField.setBounds(111, 168, 163, 19);
		frmStoreSystem.getContentPane().add(productNameTextField);
		productNameTextField.setColumns(10);
		
		

		

		
// Create Label : Price
		JLabel priceLabel = new JLabel("Price");
		priceLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		priceLabel.setBounds(10, 196, 91, 19);
		frmStoreSystem.getContentPane().add(priceLabel);
		
// Create TextField : Price
		priceTextField = new JTextField();
		priceTextField.setColumns(10);
		priceTextField.setBounds(111, 197, 163, 19);
		frmStoreSystem.getContentPane().add(priceTextField);
		
		

		

		

// Create Label : Quantity
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		quantityLabel.setBounds(10, 225, 91, 19);
		frmStoreSystem.getContentPane().add(quantityLabel);
		
// Create TextField : Quantity
		quantityTextField = new JTextField();
		quantityTextField.setColumns(10);
		quantityTextField.setBounds(111, 226, 163, 19);
		frmStoreSystem.getContentPane().add(quantityTextField);
		
		

		

		

// Create Label : ProductID
		JLabel productIDLabel = new JLabel("ProductID");
		productIDLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		productIDLabel.setBounds(432, 154, 91, 19);
		frmStoreSystem.getContentPane().add(productIDLabel);
		
// Create ComboBox : ProductID
		productIDComboBox.setFont(new Font("Tahoma", Font.BOLD, 13));
		productIDComboBox.setBounds(432, 183, 86, 21);
		frmStoreSystem.getContentPane().add(productIDComboBox);
		
		

		

		

// Create Table
		Table.setBounds(10, 346, 528, 182);
		frmStoreSystem.getContentPane().add(Table);
		table = new JTable();
		Table.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				
			},
			new String[] {
				"ProductID", "Product Name", "Price", "Quantity"
			}
		));
		table.getColumnModel().getColumn(0).setResizable(false);
		
		

		

		


		
		

		

		

// Create (Add) ActionListener
		JButton btnAdd = new JButton("Add");
		btnAdd.setBackground(new Color(128, 255, 128));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(productNameTextField.getText().isEmpty() || priceTextField.getText().isEmpty() || quantityTextField.getText().isEmpty())
					JOptionPane.showMessageDialog(null,"Must fill all inputs","Error",JOptionPane.ERROR_MESSAGE);
				else if(!isText(productNameTextField.getText()))
					JOptionPane.showMessageDialog(null,"Product Name must to be string","Error",JOptionPane.ERROR_MESSAGE);
				else {
					String productNameInput = productNameTextField.getText();
					int priceInput = Integer.parseInt(priceTextField.getText());
					int quantityInput = Integer.parseInt(quantityTextField.getText());
					Insert(productNameInput,priceInput,quantityInput);
				}

			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(10, 281, 91, 55);
		frmStoreSystem.getContentPane().add(btnAdd);
		
		

		

		

// Create (Update) ActionListener
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBackground(new Color(135, 206, 250));
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String Name=productNameTextField.getText();
				Update(Name);
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBounds(216, 281, 91, 55);
		frmStoreSystem.getContentPane().add(btnUpdate);
		
		

		

		

// Create (Delete) ActionListener
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBackground(new Color(255, 128, 128));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
//table.getSelectedRow()  btraga3 index ">=0" elrow elly 3awzen nms7ha mn table elapplication 
// wy btraga3 "-1" lw mfe4 ay row dosna 3leh
				if(table.getSelectedRow()>=0) {
//method getValueAt bta5od (index elrow wy index elcolumn) by btraga3 eldata ely fel column wy elrow dah
// e7na 3amlen index elcolumn 0 34an 3awzen awel column "ProductID"
					String selectedID=(table.getValueAt(table.getSelectedRow(), 0).toString());
					Delete(selectedID);
				}
				
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(432, 281, 91, 55);
		frmStoreSystem.getContentPane().add(btnDelete);
		
		

		

		

// Create (Search) ActionListener
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground(new Color(200, 162, 200));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedID=(productIDComboBox.getSelectedItem()).toString();
				Search(selectedID);
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSearch.setBounds(432, 214, 86, 40);
		frmStoreSystem.getContentPane().add(btnSearch);
		
		
		
		
		
		
		
		
		
// Clear all data in application THEN copy data in oracle database to application
				DisplayData();
		
		
		

		
		
		
		
	}
}