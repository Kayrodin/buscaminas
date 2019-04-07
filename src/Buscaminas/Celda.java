package Buscaminas;

import javax.swing.JButton;

public class Celda extends JButton {

	private int nivel_peligro;
	private boolean marcada;
	private boolean dudosa;

	private int posX;
	private int posY;

	/**
	 * Costructor de celdas del campo
	 * 
	 * @param nivel_peligro
	 */
	protected Celda(int nivel_peligro) {
		super();
		this.setNivel_peligro(nivel_peligro);
	}

	//Getters y Setters
	public int getNivel_peligro() {
		return nivel_peligro;
	}

	public boolean isMarcada() {
		return marcada;
	}

	public void setMarcada(boolean marcada) {
		this.marcada = marcada;
	}

	public void setNivel_peligro(int nivel_peligro) {
		this.nivel_peligro = nivel_peligro;
	}

	public void incrementaPeligro() {
		this.setNivel_peligro(this.getNivel_peligro() + 1);
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public boolean isDudosa() {
		return dudosa;
	}

	public void setDudosa(boolean dudosa) {
		this.dudosa = dudosa;
	}
}
