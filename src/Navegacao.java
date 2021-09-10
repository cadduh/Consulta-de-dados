import java.sql.*;
import java.text.ParseException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;

import com.sun.jdi.Value;

public class Navegacao extends JFrame{
	private JLabel fundo, textoTitulo, label1, label2, label3, label4,icon,icon2;
	private JButton btProx, btAnt, btPrimeiro,btSair, botaoBuscar;
	private JTextField tfCodigo, tfTitulo, tfGenero, tfAno, textoDeBusca;
	private MaskFormatter msAno;
	private BD bd;
	private PreparedStatement st;
	private ResultSet resultSet;
	private JComboBox coluna;
	
	public static void main(String args[]) {
		JFrame frame = new Navegacao();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public Navegacao(){
		inicializarComponentes();
		definirEventos();
	}
	
	public void inicializarComponentes(){
		setLayout(null);
		setTitle("Consulta de Dados");
		setSize(620,330);
		setResizable(false);
		fundo = new JLabel(new ImageIcon("res//nuvem.jpg.jpg"));
		fundo.setBounds(0,0,620,400);
		
		textoTitulo = new JLabel("Consulta De Dados");
		textoTitulo.setForeground(new Color(50,99,143));
		textoTitulo.setHorizontalAlignment(textoTitulo.CENTER);
		textoTitulo.setFont(new Font("Times New Roman",0,24));
		textoTitulo.setBounds(196, 23, 200, 35);
		add(textoTitulo);

		label1 = new JLabel("CPF    :");
		label2 = new JLabel("Nome  :");
		label3 = new JLabel("Gênero :");
		label4 = new JLabel("Ano    :");
		
		String[] colunasDb = {
			"Nome",
			"CPF",
			"Gênero",
			"Ano"
		};
		
		coluna = new JComboBox(colunasDb);
		coluna.setBounds(26,170,100,20);
		add(coluna);	
		
		textoDeBusca = new JTextField(10);
		textoDeBusca.setVisible(true);
		textoDeBusca.setBounds(26,200,100,20);
		add(textoDeBusca);
		
		botaoBuscar = new JButton("Buscar");
		botaoBuscar.setBounds(26,230,100,20);
		add(botaoBuscar);
		icon = new JLabel();
		icon2 = new JLabel();
		tfCodigo = new JTextField(10);
		tfCodigo.setEditable(false);
		tfTitulo = new JTextField(35);
		tfTitulo.setEditable(false);
		tfGenero = new JTextField(15);
		tfGenero.setEditable(false);
		tfAno = new JTextField(10);
		tfAno.setEditable(false);
		btProx = new JButton("Próximo");
		btAnt = new JButton("Anterior");
		btPrimeiro = new JButton("Primeiro");
		btSair = new JButton("Sair");
		
		label1.setBounds(142,94,80,30);
		label1.setHorizontalAlignment(textoTitulo.CENTER);
		label1.setForeground(new Color(70,132,189));
		label1.setFont(new Font("Times New Roman",0,20));
		add(label1);
		
		tfCodigo.setBounds(230,98,100,20);
		add(tfCodigo);
		
		label2.setBounds(150,70,70,30);
		label2.setHorizontalAlignment(textoTitulo.CENTER);
		label2.setForeground(new Color(70,132,189));
		label2.setFont(new Font("Times New Roman",0,20));
		add(label2);
		
		tfTitulo.setBounds(230,75,300,20);
		add(tfTitulo);
		
		label3.setBounds(350,94,70,30);
		label3.setHorizontalAlignment(textoTitulo.CENTER);
		label3.setForeground(new Color(70,132,189));
		label3.setFont(new Font("Times New Roman",0,20));
		add(label3);
		
		tfGenero.setBounds(430,98,100,20);
		add(tfGenero);
		
		label4.setBounds(147,116,70,30);
		label4.setHorizontalAlignment(textoTitulo.CENTER);
		label4.setForeground(new Color(70,132,189));
		label4.setFont(new Font("Times New Roman",0,20));
		add(label4);
		
		tfAno.setBounds(230,122,100,20);
		add(tfAno);
		
		btPrimeiro.setBounds(470,165,100,25);
		add(btPrimeiro);
		
		btAnt.setBounds(470,194,99,25);
		add(btAnt);
		
		btProx.setBounds(470,223, 100, 25);
		add(btProx);
		add(btSair);


		bd = new BD();
		if(!bd.getConnection()){
			JOptionPane.showMessageDialog(null,"Falha na conexão!");
			System.exit(0);
		}
		carregarTabela();
		atualizarCampos();	

	}
	
	public void definirEventos(){
		btProx.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					resultSet.next();
					atualizarCampos();
				}catch(SQLException erro){
			}
			}
		});
		
		btAnt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					resultSet.previous();
					atualizarCampos();
				}catch(SQLException erro){
			}
			}
		});
		
		btPrimeiro.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					carregarTabela();
					resultSet.first();
					atualizarCampos();
				}catch(SQLException erro){
			}
			}
		});
		
		btSair.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
				resultSet.close();
				st.close();
				}catch(SQLException erro){
				}
				bd.close();
				System.exit(0);
			}
		});
		botaoBuscar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tfAno.setText("");
				tfCodigo.setText("");
				tfGenero.setText("");
				tfTitulo.setText("");
				String sql1 ="";
				
				if(coluna.getSelectedIndex() == 0) {
				    sql1 = "select * from dados where Nome = ?";
				}
				if(coluna.getSelectedIndex() == 1) {
					sql1 = "select * from dados where CPF = ?";
				}
				if(coluna.getSelectedIndex() == 2) {
					sql1 = "select * from dados where Genero = ?";
				}
				if(coluna.getSelectedIndex() == 3) {
					sql1 = "select * from dados where Ano = ?";
				}
				
				

				try{
					st = bd.c.prepareStatement(sql1);
					st.setString(1, textoDeBusca.getText());
                    System.out.println(st);
					resultSet = st.executeQuery();
                    atualizarCampos();
		


				}catch(SQLException erro){
					JOptionPane.showMessageDialog(null,"Erro! " +erro.toString() );
				}
				
				
			}
		});
	}
	public void carregarTabela(){
		String sql = "select * from dados where id";
		try{
			st = bd.c.prepareStatement(sql);
			resultSet = st.executeQuery();
		}catch (SQLException erro){
			JOptionPane.showMessageDialog(null,"Erro! " +erro.toString() );
		}
	}
	public void iconImage() {
		try {
			if(resultSet.getString("Genero").equals("Masculino")) {
				icon.setIcon(new ImageIcon("res//man.png"));
				icon.setBounds(20,50,110,110);
				icon.setVisible(true);
				icon2.setVisible(false);
				add(icon);
				add(fundo);
				System.out.println("Man");
			}
			else {
				icon2.setIcon(new ImageIcon("res//mulher1.png"));
				icon2.setBounds(20,50,110,110);
				icon2.setVisible(true);
				icon.setVisible(false);
				add(icon2);
				add(fundo);
				System.out.println("Woman");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void atualizarCampos(){
		try{
			if(resultSet.isAfterLast()){
				resultSet.last();
			}
			if(resultSet.isBeforeFirst()){
				resultSet.first();
			}			
			tfCodigo.setText(resultSet.getString("CPF"));
			tfTitulo.setText(resultSet.getString("Nome"));
			tfGenero.setText(resultSet.getString("Genero"));
			tfAno.setText(resultSet.getString("Ano"));
			iconImage();


			
			
		} catch(SQLException erro){
			
		}
		
	}
	}