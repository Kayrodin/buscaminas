package Buscaminas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Records {

	private JFrame frame;
	private BufferedReader flujo_lectura;
	private PrintWriter escritor;
	private JList<String> lista;
	
	/**
	 * Construye la ventana, establece la ruta del fichero y crea flujos de lectura y escritura
	 */
	public Records() {
		File archivo = new File ("records/records.txt");
		FileReader lector_fichero;
		FileWriter escritor_fichero;
		BufferedWriter flujo_escritura;
		
		try {
			escritor_fichero = new FileWriter(archivo,true);
			flujo_escritura = new BufferedWriter(escritor_fichero);
			escritor = new PrintWriter(flujo_escritura);
			lector_fichero = new FileReader (archivo);
			flujo_lectura = new BufferedReader(lector_fichero);
		} catch (FileNotFoundException e) {
			System.out.println("Fichero de lectura no encontrado ");
		}
		catch (IOException e) {
			System.out.println("Error de lectura, comprueba que el fichero está en la dirección correcta ");
		}
        
		initialize();
	}

	/**
	 * Inicializa el contenido de la ventana.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 235, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Exit");
		cerrarRecord(btnNewButton_1);
		panel.add(btnNewButton_1);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		lista = new JList<String>();
		scrollPane.setViewportView(lista);
	}

	/**
	 * Cierra la ventana que muestra la lista de ganadores
	 * @param btnNewButton_1
	 */
	public void cerrarRecord(JButton btnNewButton_1) {
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});
	}
	
	/**
	 * Lee desde el fichero la lista de ganadores y lo muestra en la ventana
	 */
	public void obtenerRecord(){
		String linea;
		String[] lista_array;
		ArrayList<String> lista_nombre = new ArrayList<>();
        try {
			while((linea=flujo_lectura.readLine())!=null){
			   lista_nombre.add(linea);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}
        lista_array = new String[lista_nombre.size()];
        lista_nombre.toArray(lista_array);
        lista.setListData(lista_array);
	}
	
	/**
	 * Escribe un nuevo ganador en el fichero
	 * @param nombre
	 * @param tiempo
	 */
	public void guardarRecord(String nombre, String tiempo){
		escritor.println("   " + nombre + " ------------------- " + tiempo);
		escritor.close();
	}

	//Getters y Setters
	public JFrame getFrame() {
		return frame;
	}
	
	public JList<String> getLista() {
		return lista;
	}

	public void setLista(JList<String> lista) {
		this.lista = lista;
	}
}
