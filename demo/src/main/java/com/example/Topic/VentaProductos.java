package com.example.Topic;

import java.util.Random;

public class VentaProductos {
	private String jarduera;
	private String kategoria;
	private String enpresa;
	Random generador;
	//Esta suscrito al 1,1 / 1,4 / 2,2 --> conseguir que se quite la suscripcion al topic
	// Lista de jarduerak, kategoriak y enpresak
	private final static String[] jarduerak = { "Lana", "Kurtsoa" };
	private final static String[] kategoriak = { "IT", "Marketing", "Finantzak", "Osasuna", "Heziketa" };
	private final static String[][] enpresak = {
			{ "Fagor", "Ikerlan", "Laboral_Kutxa", "Orona" },
			{ "LaMagnetica", "Aurea", "Iberdrola", "Moviestar" },
			{ "Laboral_Kutxa", "La_caixa", "Kutxabank", "BBVA" },
			{ "Osakidetza", "Sanitas", "Quiron", "Viamed" },
			{ "Harvard", "Mondragon_Unibertsitatea", "Oxford", "UPV" }
	};

	public VentaProductos() {
		this.generador = new Random();
		this.generarVentaProducto();
	}

	// Generador de venta de enpresa aleatoriamente
	void generarVentaProducto() {
		this.jarduera = jarduerak[generador.nextInt(jarduerak.length)];
		this.kategoria = kategoriak[generador.nextInt(kategoriak.length)];

		switch (kategoria) {
			case "IT":
				this.enpresa = enpresak[0][generador.nextInt(enpresak[0].length)];
				break;
			case "Marketing":
				this.enpresa = enpresak[1][generador.nextInt(enpresak[0].length)];
				break;
			case "Finantzak":
				this.enpresa = enpresak[2][generador.nextInt(enpresak[0].length)];
				break;
			case "Osasuna":
				this.enpresa = enpresak[3][generador.nextInt(enpresak[0].length)];
				break;
			case "Heziketa":
				this.enpresa = enpresak[4][generador.nextInt(enpresak[0].length)];
				break;
		}
	}

	public String getJarduera() {
		return jarduera;
	}

	public String getKategoria() {
		return kategoria;
	}

	public String getEnpresa() {
		return enpresa;
	}


	@Override
	public String toString() {
		String evento = this.getJarduera() + " " + this.getKategoria() + " " + this.getEnpresa();
		return evento;
	}
}
