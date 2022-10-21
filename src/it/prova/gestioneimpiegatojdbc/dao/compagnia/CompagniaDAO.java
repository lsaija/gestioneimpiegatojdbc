package it.prova.gestioneimpiegatojdbc.dao.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.IBaseDAO;
import it.provagestioneimpiegatojdbc.model.Compagnia;
import it.provagestioneimpiegatojdbc.model.Impiegato;



public interface CompagniaDAO extends IBaseDAO<Compagnia> {
	
	 public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date dataMin) throws Exception;
	 public List<Compagnia> findAllByRagioneSocialeContiene(String ragioneSociale) throws Exception;

}
