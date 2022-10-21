package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAO;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAO;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class TestGestioneImpiegato {

	public static void main(String[] args) {

		CompagniaDAO compagniaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;

		// ################################################################################
		// ################################################################################
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			compagniaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			// ---------------------------------------------------
			System.out.println("In tabella user ci sono " + impiegatoDAOInstance.list().size() + " impiegati.");
			System.out.println("In tabella user ci sono " + compagniaDAOInstance.list().size() + " compagnie.");

			testInsertCompagnia(compagniaDAOInstance);
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " compagnie.");

			testDeleteCompagnia(compagniaDAOInstance);
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testFindAllByRagioneSocialeContiene(compagniaDAOInstance);
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testInsertImpiegato(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " impiegati.");

			testFindAllByDataAssunzioneMaggioreDi(compagniaDAOInstance, impiegatoDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testGetImpiegato(impiegatoDAOInstance, compagniaDAOInstance);

			testDeleteImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " impiegati.");

			testFindAllByCompagnia(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testcountByDataFondazioneCompagniaGreaterThan(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testfindAllErroriAssunzione(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testInsertCompagnia inizio.............");

		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		Compagnia temp = new Compagnia("tesla", 100000, dataCreazione);
		int quantiElementiInseriti = compagniaDAOInstance.insert(temp);
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertCompagnia : FAILED ,non inserito");

		System.out.println(".......testInsertCompagnia fine: PASSED.............");
	}

	private static void testGetCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testGet inizio.............");

		List<Compagnia> elencoPresenti = compagniaDAOInstance.list();
		Compagnia elementoRicercoColDAO = compagniaDAOInstance.get(0L);
		System.out.println(elementoRicercoColDAO);

		System.out.println(".......testGet fine: PASSED.............");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testDeleteCompagnia inizio.............");

		List<Compagnia> listaCompagniePresenti = compagniaDAOInstance.list();
		if (listaCompagniePresenti.isEmpty()) {
			throw new RuntimeException("testDeleteCompagnia : FAILED,nessun elemento da rimuovere");
		}

		Compagnia compagniaDaRimuovere = new Compagnia("RagioneSociale", 100,
				new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022"));
		compagniaDAOInstance.insert(compagniaDaRimuovere);
		listaCompagniePresenti = compagniaDAOInstance.list();
		compagniaDaRimuovere = listaCompagniePresenti.get(listaCompagniePresenti.size() - 1);

		int verifica = compagniaDAOInstance.delete(compagniaDaRimuovere);
		if (verifica < 1) {
			throw new RuntimeException("testDeleteCompagnia : FAILED");
		} else {
			System.out.println(".......testDeleteCompagnia fine: PASSED.............");
		}
	}

	private static void testFindAllByRagioneSocialeContiene(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindAllByRagioneSocialeContiene inizio.............");

		List<Compagnia> listaCompagniePresenti = compagniaDAOInstance.list();
		if (listaCompagniePresenti.isEmpty()) {
			throw new RuntimeException("testFindAllByRagioneSocialeContiene : FAILED, nessun elemento");
		}
		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		Compagnia temp = new Compagnia("cercato", 100000, dataCreazione);
		compagniaDAOInstance.insert(temp);
		String ragioneSocialeCercata = "cercato";
		listaCompagniePresenti = compagniaDAOInstance.findAllByRagioneSocialeContiene(ragioneSocialeCercata);

		if (listaCompagniePresenti.isEmpty()) {
			throw new RuntimeException("testFindAllByRagioneSocialeContiene : FAILED");
		}
		System.out.println(".......testFindAllByRagioneSocialeContiene fine: PASSED.............");

	}

	private static void testFindAllByDataAssunzioneMaggioreDi(CompagniaDAO compagniaDAOInstance,
			ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testFindAllByDataAssunzioneMaggioreDi inizio.............");
		List<Impiegato> listaImpiegatiPresenti = new ArrayList();

		Date dataRicercata = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2000");
		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		Compagnia temp = new Compagnia("tesla", 100000, dataCreazione);
		compagniaDAOInstance.insert(temp);
		Date dataNa = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");
		Date dataAssu = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2023");
		Compagnia prov = compagniaDAOInstance.list().get(compagniaDAOInstance.list().size() - 1);
		Impiegato assunto = new Impiegato("Mario", "Rossi", "mrrss##", dataNa, dataAssu, prov);
		impiegatoDAOInstance.insert(assunto);

		listaImpiegatiPresenti = compagniaDAOInstance.findAllByDataAssunzioneMaggioreDi(dataRicercata);
		if (listaImpiegatiPresenti.isEmpty()) {
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED");
		}
		System.out.println(".......testFindAllByDataAssunzioneMaggioreDi fine: PASSED.............");

	}

	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance, CompagniaDAO compagniaDAOInstance)
			throws Exception {
		System.out.println(".......testInsertImpiegato inizio.............");
		List<Compagnia> listaCompagniePresenti = compagniaDAOInstance.list();
		Compagnia compagniaTemp = new Compagnia("RagioneSociale", 100,
				new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022"));
		compagniaDAOInstance.insert(compagniaTemp);
		listaCompagniePresenti = compagniaDAOInstance.list();
		compagniaTemp = listaCompagniePresenti.get(listaCompagniePresenti.size() - 1);
		Date dataNa = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");
		Date dataAssu = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2023");

		int inseriti = impiegatoDAOInstance
				.insert(new Impiegato("Paolo", "Bianco", "polbnc###", dataNa, dataAssu, compagniaTemp));
		if (inseriti < 1)
			throw new RuntimeException("testInsertCompagnia : FAILED");

		System.out.println(".......testInsertImpiegato fine: PASSED.............");
	}

	private static void testGetImpiegato(ImpiegatoDAO impiegatoDAOInstance, CompagniaDAO compagniaDAOInstance)
			throws Exception {
		System.out.println(".......testGet inizio.............");

		List<Impiegato> elencoPresenti = impiegatoDAOInstance.list();
		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		Compagnia temp = new Compagnia("tesla", 100000, dataCreazione);
		compagniaDAOInstance.insert(temp);
		Compagnia prov = compagniaDAOInstance.list().get(compagniaDAOInstance.list().size() - 1);
		Date dataNa = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");
		Date dataAssu = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2023");
		Impiegato tempImp = new Impiegato("Mario", "Rossi", "mrrss##", dataNa, dataAssu, prov);
		impiegatoDAOInstance.insert(tempImp);
		Impiegato provImp = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);

		System.out.println(prov.getId());

		System.out.println(".......testGet fine: PASSED.............");
	}

	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testDeleteImpiegato inizio.............");

		List<Impiegato> listaImpiegatiPresenti = impiegatoDAOInstance.list();
		if (listaImpiegatiPresenti.isEmpty()) {
			throw new RuntimeException("testDeleteImpiegato : FAILED,nessun elemento da rimuovere");

		}

		Impiegato impiegatoDaRimuovere = listaImpiegatiPresenti.get(0);
		int verifica = impiegatoDAOInstance.delete(impiegatoDaRimuovere);
		if (verifica < 1) {
			throw new RuntimeException("testDeleteImpiegato : FAILED");
		} else {
			System.out.println(".......testDeleteImpiegato fine: PASSED.............");
		}
	}

	private static void testFindAllByCompagnia(ImpiegatoDAO impiegatoDAOInstance, CompagniaDAO compagniaDAOInstance)
			throws Exception {
		System.out.println(".......testFindAllByCompagnia inizio.............");
		List<Impiegato> impiegatiCer = impiegatoDAOInstance.list();
		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		Compagnia temp = new Compagnia("tesla", 100000, dataCreazione);
		Compagnia cercata = compagniaDAOInstance.list().get(compagniaDAOInstance.list().size() - 1);
		Date dataNa = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");
		Date dataAssu = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2023");
		Impiegato tempImp = new Impiegato("Mario", "Rossi", "mrrss##", dataNa, dataAssu, cercata);
		impiegatoDAOInstance.insert(tempImp);
		Impiegato provImp = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);
		impiegatiCer = impiegatoDAOInstance.findAllByCompagnia(cercata);
		if (impiegatiCer.size() < 1) {
			throw new RuntimeException("testFindAllByCompagnia : FAILED");
		} else {
			System.out.println(".......testFindAllByCompagnia fine: PASSED.............");
		}

	}

	private static void testcountByDataFondazioneCompagniaGreaterThan(ImpiegatoDAO impiegatoDAOInstance,
			CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testcountByDataFondazioneCompagniaGreaterThan inizio.............");

		Compagnia compagniaTemp = new Compagnia("RagioneSociale", 100,
				new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022"));
		compagniaDAOInstance.insert(compagniaTemp);
		List<Impiegato> elencoPresenti = impiegatoDAOInstance.list();
		List<Compagnia> listaCompagniePresenti = compagniaDAOInstance.list();
		compagniaTemp = listaCompagniePresenti.get(listaCompagniePresenti.size() - 1);

		Date dataNa = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");
		Date dataAssu = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2023");
		Impiegato tempImp = new Impiegato("Mario", "Rossi", "mrrss##", dataNa, dataAssu, compagniaTemp);
		impiegatoDAOInstance.insert(tempImp);
		Impiegato provImp = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);

		Date dataCercata = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");

		int verifica = 0;
		verifica = impiegatoDAOInstance.countByDataFondazioneCompagniaGreaterThan(dataCercata);
		if (verifica < 1) {
			throw new RuntimeException("testcountByDataFondazioneCompagniaGreaterThan : FAILED");
		} else {
			System.out.println(".......testcountByDataFondazioneCompagniaGreaterThan fine: PASSED.............");
		}
	}

	private static void testfindAllErroriAssunzione(ImpiegatoDAO impiegatoDAOInstance,
			CompagniaDAO compagniaDAOInstance) throws Exception {
		Compagnia compagniaTemp = new Compagnia("RagioneSociale", 100,
				new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2023"));
		compagniaDAOInstance.insert(compagniaTemp);
		List<Impiegato> elencoPresenti = impiegatoDAOInstance.list();
		List<Compagnia> listaCompagniePresenti = compagniaDAOInstance.list();
		compagniaTemp = listaCompagniePresenti.get(listaCompagniePresenti.size() - 1);

		Date dataNa = new SimpleDateFormat("dd-MM-yyyy").parse("04-01-1997");
		Date dataAssu = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2020");
		Impiegato tempImp = new Impiegato("Mario", "Rossi", "mrrss##", dataNa, dataAssu, compagniaTemp);
		impiegatoDAOInstance.insert(tempImp);
		Impiegato provImp = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);

		elencoPresenti = impiegatoDAOInstance.findAllErroriAssunzione();
		if (elencoPresenti.isEmpty()) {
			throw new RuntimeException("testfindAllErroriAssunzione : FAILED");
		} else {
			System.out.println(".......testfindAllErroriAssunzione fine: PASSED.............");
		}
	}

}
