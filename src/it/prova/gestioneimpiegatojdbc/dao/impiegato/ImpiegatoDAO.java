package it.prova.gestioneimpiegatojdbc.dao.impiegato;

import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.IBaseDAO;
import it.provagestioneimpiegatojdbc.model.Compagnia;
import it.provagestioneimpiegatojdbc.model.Impiegato;

public interface ImpiegatoDAO extends  IBaseDAO<Impiegato> {
	
	public List<Impiegato> findAllByCompagnia(Compagnia compagnia) throws Exception;
	public int countByDataFondazioneCompagniaGreaterThan(Date data) throws Exception;
	public List<Impiegato> findAllErroriAssunzione() throws Exception;

}
