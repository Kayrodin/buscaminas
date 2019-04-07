package Buscaminas;

import java.util.Random;

enum Dificultad {
	FACIL, MODERADO, DIFICIL
};

public class CampoMinado {

	Random r_number = new Random();
	private Celda[][] campo;
	private int num_min;
	private int max_filas;
	private int max_columnas;
	private int num_banderas;

	/**
	 * Contructor del campo de minas
	 * 
	 * @param dif
	 */
	public CampoMinado(Dificultad dif) {
		if (dif == null)
			estableceDificultad(Dificultad.FACIL);
		else
			estableceDificultad(dif);
	}

	/**
	 * Establece la dificultad actualmente seleccionada y designa los parametros
	 * del campo
	 * 
	 * @param dificultad_actual
	 */
	private void estableceDificultad(Dificultad dificultad_actual) {
		switch (dificultad_actual) {
		case FACIL:
			colocarMinas(9, 9, 10);
			break;
		case MODERADO:
			colocarMinas(16, 16, 40);
			break;
		case DIFICIL:
			colocarMinas(30, 30, 99);
		}
	}

	/**
	 * Rellena un campo inicial sin minas de tamaño m x n
	 * 
	 * @param m
	 * @param n
	 */
	public void rellenarCampo(int m, int n) {
		campo = new Celda[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				this.campo[i][j] = new Celda(0);
				this.campo[i][j].setPosX(i);
				this.campo[i][j].setPosY(j);
			}
		}
	}

	/**
	 * Coloca aleatoriamente el numero designado de minas en el campo
	 * 
	 * @param m
	 * @param n
	 * @param num_minas
	 */
	public void colocarMinas(int m, int n, int num_minas) {
		rellenarCampo(m, n);
		this.setNum_min(num_minas);
		this.setMax_filas(m);
		this.setMax_columnas(n);
		this.setNum_banderas(num_minas);
		int fila, columna;
		do {
			do {
				fila = r_number.nextInt(m);
				columna = r_number.nextInt(n);
			} while (this.campo[fila][columna].getNivel_peligro() == 9);
			this.campo[fila][columna].setNivel_peligro(9);
			num_minas--;
			this.radioPeligro(fila, columna);
		} while (num_minas > 0);
	}

	/**
	 * Determina el radio de peligro de cada mina colocada en el campo
	 * 
	 * @param fila
	 * @param columna
	 */
	public void radioPeligro(int fila, int columna) {
		Celda cld;
		if ((fila - 1) >= 0) {
			cld = campo[fila - 1][columna];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (fila + 1 < max_filas) {
			cld = campo[fila + 1][columna];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (columna - 1 >= 0) {
			cld = campo[fila][columna - 1];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (columna + 1 < max_columnas) {
			cld = campo[fila][columna + 1];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (columna - 1 >= 0 && fila - 1 >= 0) {
			cld = campo[fila - 1][columna - 1];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (columna + 1 < max_columnas && fila + 1 < max_filas) {
			cld = campo[fila + 1][columna + 1];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (columna + 1 < max_columnas && fila - 1 >= 0) {
			cld = campo[fila - 1][columna + 1];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
		if (columna - 1 >= 0 && fila + 1 < max_filas) {
			cld = this.campo[fila + 1][columna - 1];
			if (cld.getNivel_peligro() < 9) {
				cld.incrementaPeligro();
			}
		}
	}

	/**
	 * Muestra recursivamente las celdas sin peligro y su vecindad
	 * 
	 * @param x
	 * @param y
	 */
	public void destaparCeldas(int x, int y) {
		if (campo[x][y].getNivel_peligro() == 0) {
			if (!campo[x][y].isMarcada() && !campo[x][y].isDudosa()) {
				campo[x][y].setEnabled(false);
			}

			if (x - 1 >= 0 && campo[x - 1][y].getNivel_peligro() == 0 && campo[x - 1][y].isEnabled()) {
				destaparCeldas(x - 1, y);
			}

			if (y - 1 >= 0 && campo[x][y - 1].getNivel_peligro() == 0 && campo[x][y - 1].isEnabled()) {
				destaparCeldas(x, y - 1);
			}

			if (x + 1 < max_filas && campo[x + 1][y].getNivel_peligro() == 0 && campo[x + 1][y].isEnabled()) {
				destaparCeldas(x + 1, y);
			}

			if (y + 1 < max_columnas && campo[x][y + 1].getNivel_peligro() == 0 && campo[x][y + 1].isEnabled()) {
				destaparCeldas(x, y + 1);
			}

			vecindad(x, y);

		} else {
			campo[x][y].setEnabled(false);
			campo[x][y].setText(Integer.toString(campo[x][y].getNivel_peligro()));
		}
	}

	/**
	 * Determina la vecindad de una celda
	 * 
	 * @param fila
	 * @param columna
	 */
	private void vecindad(int fila, int columna) {
		Celda celda;
		if ((fila - 1) >= 0) {
			celda = campo[fila - 1][columna];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (fila + 1 < max_filas) {
			celda = campo[fila + 1][columna];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (columna - 1 >= 0) {
			celda = campo[fila][columna - 1];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (columna + 1 < max_columnas) {
			celda = campo[fila][columna + 1];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (columna - 1 >= 0 && fila - 1 >= 0) {
			celda = campo[fila - 1][columna - 1];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (columna + 1 < max_columnas && fila + 1 < max_filas) {
			celda = campo[fila + 1][columna + 1];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (columna + 1 < max_columnas && fila - 1 >= 0) {
			celda = campo[fila - 1][columna + 1];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}
		if (columna - 1 >= 0 && fila + 1 < max_filas) {
			celda = this.campo[fila + 1][columna - 1];
			if (isCeldaComprometida(celda)) {
				celda.setText(Integer.toString(celda.getNivel_peligro()));
				celda.setEnabled(false);
			}
		}

	}

	/**
	 * Determina si la celda porta un nivel de peligro y si no está comprometida
	 * por el jugador
	 * 
	 * @param celda
	 * @return
	 */
	public boolean isCeldaComprometida(Celda celda) {
		boolean is_comprometida;
		if (celda.isMarcada() || celda.isDudosa() || !(celda.getNivel_peligro() < 9 && celda.getNivel_peligro() > 0))
			is_comprometida = false;
		else
			is_comprometida = true;
		return is_comprometida;
	}

	// Getters y Setters
	public int getNum_min() {
		return num_min;
	}

	public void setNum_min(int num_min) {
		this.num_min = num_min;
	}

	public void incrementa_numMinas() {
		this.num_min++;
	}

	public void descrementa_NumMinas() {
		this.num_min--;
	}

	public int getMax_filas() {
		return max_filas;
	}

	public void setMax_filas(int max_filas) {
		this.max_filas = max_filas;
	}

	public int getMax_columnas() {
		return max_columnas;
	}

	public void setMax_columnas(int max_columnas) {
		this.max_columnas = max_columnas;
	}

	public int getNum_banderas() {
		return num_banderas;
	}

	public void setNum_banderas(int num_banderas) {
		this.num_banderas = num_banderas;
	}

	public void incrementa_banderas() {
		this.num_banderas++;
	}

	public void descrementa_banderas() {
		this.num_banderas--;
	}

	public Celda[][] getCampo() {
		return campo;
	}

}
