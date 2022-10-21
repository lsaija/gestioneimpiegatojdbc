package it.provagestioneimpiegatojdbc.model;

import java.util.Date;

public class Compagnia {
	private Long id;
	private String ragioneSociale;
	private int fatturatoAnnuo;
	private Date dataFondazione;

	// costruttori
	public Compagnia() {
	}

	public Compagnia(String ragioneSociale, int fatturatoAnnuo, Date dataFondazione) {
		super();
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
		this.dataFondazione = dataFondazione;
	}

	public Compagnia(Long id, String ragioneSociale, int fatturatoAnnuo, Date dataFondazione) {
		super();
		this.id = id;
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
		this.dataFondazione = dataFondazione;
	}

	public Compagnia(Long id, String ragioneSociale) {
		super();
		this.id = id;
		this.ragioneSociale = ragioneSociale;
	}

	// get and set
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public int getFatturatoAnnuo() {
		return fatturatoAnnuo;
	}

	public void setFatturatoAnnuo(int fatturatoAnnuo) {
		this.fatturatoAnnuo = fatturatoAnnuo;
	}

	public Date getDataFondazione() {
		return dataFondazione;
	}

	public void setDataFondazione(Date dataFondazione) {
		this.dataFondazione = dataFondazione;
	}

	// toString
	@Override
	public String toString() {
		return "Compagnia [id=" + id + ", ragioneSociale=" + ragioneSociale + ", fatturatoAnnuo=" + fatturatoAnnuo
				+ ", dataFondazione=" + dataFondazione + "]";
	}

}
