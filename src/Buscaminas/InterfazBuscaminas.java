package Buscaminas;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * 
 * @author David Hurtado Peez y Jose
 * @version 2.2
 *
 */
public class InterfazBuscaminas {

	private JFrame frame;
	private CampoMinado campo;
	private JPanel panel_campo;
	private JPanel panel_accesorio;
	private Rectangle tamano;
	private String dificultad_actual;
	private JLabel contador_banderas;
	private JLabel crono_label;
	private Timer cronometro;
	private TimerTask tarea_cronometro;
	private String record_tiempo;
	private Records ganadores_lista;

	/**
	 * Lanza la aplicacion
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazBuscaminas window = new InterfazBuscaminas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Contruye la aplicacion InterfazBuscaminas e instancia el cronometro, el
	 * tamano de ventana y la lista de ganadores
	 */
	public InterfazBuscaminas() {
		this.dificultad_actual = "Easy";
		this.tamano = new Rectangle(450, 300);
		cronometro = new Timer();
		Cronometro();
		ganadores_lista = new Records();
		initialize();
	}

	/**
	 * Inicializa el contenido de la ventana
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(tamano);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		frame.getContentPane().setLayout(new BorderLayout(2, 1));

		panel_campo = new JPanel();
		panel_accesorio = new JPanel();
		panel_accesorio.setLayout(new BorderLayout(1, 2));

		crono_label = new JLabel("Timer");
		crono_label.setIcon(new ImageIcon("img/timer.png"));
		panel_accesorio.add(crono_label, BorderLayout.WEST);

		contador_banderas = new JLabel("Flags");
		contador_banderas.setIcon(new ImageIcon("img/flag.png"));
		panel_accesorio.add(contador_banderas, BorderLayout.EAST);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewGame = new JMenuItem("New Game");
		comienzaJuego(mntmNewGame);
		mnFile.add(mntmNewGame);

		JMenuItem mntmExit = new JMenuItem("Exit");
		Salir(mntmExit);
		mnFile.add(mntmExit);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		JMenu mnDifficulty = new JMenu("Difficulty");

		mnOptions.add(mnDifficulty);

		JMenuItem mntmEasy = new JMenuItem("Easy");
		seleccionarDificultad(mntmEasy);
		mnDifficulty.add(mntmEasy);

		JMenuItem mntmModerate = new JMenuItem("Moderate");
		seleccionarDificultad(mntmModerate);
		mnDifficulty.add(mntmModerate);

		JMenuItem mntmHard = new JMenuItem("Hard");
		seleccionarDificultad(mntmHard);
		mnDifficulty.add(mntmHard);

		JMenuItem mntmRecords = new JMenuItem("Records");
		mostrarRecords(mntmRecords);
		mnOptions.add(mntmRecords);

		frame.setResizable(false);

	}

	/**
	 * Determina si el jugadores desea ver la lista de ganadores
	 * 
	 * @param mntmRecords
	 */
	public void mostrarRecords(JMenuItem mntmRecords) {
		mntmRecords.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mostrarGanadores();
			}
		});
	}

	/**
	 * Determina y cierra la aplicacion si el jugador desea salir de esta
	 * 
	 * @param mntmExit
	 */
	public void Salir(JMenuItem mntmExit) {
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * Determina si el jugador desea comenzar un juego nuevo
	 * 
	 * @param mntmNewGame
	 */
	public void comienzaJuego(JMenuItem mntmNewGame) {
		mntmNewGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				estableceNuevoJuego();
			}
		});
	}

	/**
	 * Crea o resetea el juego, el tamaño de ventana y el cronometro
	 */
	public void estableceNuevoJuego() {
		estableceDificultad();
		frame.setBounds(tamano);
		mostrarCampo();
		reseteaCronometro();
		campo.setNum_banderas(campo.getNum_min());
		contador_banderas.setText(Integer.toString(campo.getNum_banderas()));
		frame.getContentPane().add(panel_accesorio, BorderLayout.NORTH);
		frame.getContentPane().add(panel_campo, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().repaint();
	}

	/**
	 * Establece la barra de menu
	 * 
	 * @param component
	 * @param popup
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	/**
	 * Determina la dificultad seleccionada por el jugador
	 * 
	 * @param dificultad
	 */
	public void seleccionarDificultad(JMenuItem dificultad) {
		dificultad.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dificultad_actual = dificultad.getText();
			}
		});
	}

	/**
	 * Crea el campo minado según la dificultad y establece el tamano de la
	 * ventana
	 * 
	 * @param type_dif
	 */
	public void estableceDificultad() {
		if (dificultad_actual.equals("Easy")) {
			campo = new CampoMinado(Dificultad.FACIL);
			tamano.height = 300;
			tamano.width = 400;
		} else if (dificultad_actual.equals("Moderate")) {
			campo = new CampoMinado(Dificultad.MODERADO);
			tamano.height = 500;
			tamano.width = 700;
		} else {
			campo = new CampoMinado(Dificultad.DIFICIL);
			tamano.height = 800;
			tamano.width = 1250;
		}
	}

	/**
	 * Muestra el campo en la ventana
	 * 
	 * @param num_filas
	 * @param num_columnas
	 * @param num_minas
	 */
	public void mostrarCampo() {
		int num_filas = campo.getMax_filas();
		int num_columnas = campo.getMax_columnas();
		panel_campo.removeAll();
		panel_campo.setLayout(new GridLayout(num_filas, num_columnas));
		for (int i = 0; i < num_filas; i++) {
			for (int j = 0; j < num_columnas; j++) {
				Celda bt = campo.getCampo()[i][j];
				selecButton(bt);
				panel_campo.add(bt);
			}
		}
	}

	/**
	 * Determina si el jugador selecciona una celda y actua en funcion del boton
	 * de seleccion. Establece las condiciones de victoria o derrota
	 * 
	 * @param bt
	 */
	public void selecButton(Celda bt) {
		bt.addMouseListener(new MouseAdapter() {
			// Click en Celda
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					bt.setIcon(null);
					campo.destaparCeldas(bt.getPosX(), bt.getPosY());
				}

				else if (e.getButton() == MouseEvent.BUTTON3) {
					marcarCelda(bt);
				}
			}

			// Establece condiciones de victoria o derrota
			@Override
			public void mouseReleased(MouseEvent e) {

				if (bt.getNivel_peligro() == 9 && e.getButton() == MouseEvent.BUTTON1) {
					bt.setText(null);
					mostrarMinas();
					JOptionPane.showMessageDialog(null, "Perdiste");
					panel_campo.removeAll();

				} else if (campo.getNum_min() < 1 && e.getButton() == MouseEvent.BUTTON3) {
					tarea_cronometro.cancel();
					String nombre = JOptionPane.showInputDialog(null, "Introduce tu nombre", "Has Ganado");
					ganadores_lista.guardarRecord(nombre, record_tiempo);
					mostrarGanadores();
					panel_campo.removeAll();
				}
			}
		});
	}

	/**
	 * Muestra la lista de ganadores en una nueva ventana
	 */
	public void mostrarGanadores() {
		ganadores_lista.obtenerRecord();
		ganadores_lista.getFrame().setVisible(true);
	}

	/**
	 * Muetra las minas ocultas en el campo
	 */
	public void mostrarMinas() {
		for (Celda[] filaCampo : campo.getCampo()) {
			for (Celda celda : filaCampo) {
				if (celda.getNivel_peligro() == 9) {
					celda.setIcon(new ImageIcon("img/bomba.jpg"));
				}
			}
		}
	}

	/**
	 * Marca como mina encontrada o supuesta a la celda
	 * 
	 * @param bt
	 */
	public void marcarCelda(Celda bt) {
		if (bt.isDudosa()) {
			bt.setDudosa(false);
			bt.setIcon(null);
		} else if (bt.isMarcada()) {
			bt.setDudosa(true);
			bt.setMarcada(false);
			bt.setIcon(new ImageIcon("img/interrogacion.png"));
			campo.incrementa_banderas();
			if (bt.getNivel_peligro() == 9) {
				campo.incrementa_numMinas();
			}
		} else {
			if (campo.getNum_banderas() > 0 && bt.isEnabled()) {
				bt.setMarcada(true);
				bt.setIcon(new ImageIcon("img/flag.png"));
				campo.descrementa_banderas();
				if (bt.getNivel_peligro() == 9) {
					campo.descrementa_NumMinas();
				}
			}
		}
		contador_banderas.setText(Integer.toString(campo.getNum_banderas()));
	}

	/**
	 * Establece las funciones del Cronometro
	 */
	public void Cronometro() {
		tarea_cronometro = new TimerTask() {
			int seg = 0;
			int min = 0;
			int h = 0;
			int count = 0;

			@Override
			public void run() {
				if (seg == 60) {
					min++;
					seg = 0;
				} else if (min == 60) {
					h++;
					min = 0;
				} else {
					if (count == 100) {
						count = 0;
					}
					seg++;
					count++;
				}

				record_tiempo = (h <= 9 ? "0" : "") + h + ":" + (min <= 9 ? "0" : "") + min + ":"
						+ (seg <= 9 ? "0" : "") + seg;
				crono_label.setText(Integer.toString(count));
			}
		};
	}

	/**
	 * Resetea el cronometro
	 */
	public void reseteaCronometro() {
		tarea_cronometro.cancel();
		Cronometro();
		cronometro.schedule(tarea_cronometro, 0, 1000);
	}
}
