package it.prova.gestioneimpiegatojdbc.dao.compagnia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.AbstractMySQLDAO;

import it.provagestioneimpiegatojdbc.model.Compagnia;
import it.provagestioneimpiegatojdbc.model.Impiegato;

public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	public List<Compagnia> list() throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTemp.setDataFondazione(rs.getDate("datafondazione"));
				compagniaTemp.setId(rs.getLong("id"));
				result.add(compagniaTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	// metodo simile a findById
	@Override
	public Compagnia get(Long idInput) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (idInput == null || idInput < 1) {
			throw new Exception("Valore di input non ammesso.");
		}
		Compagnia result = null;

		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where id=?")) {
			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setRagioneSociale(rs.getString("ragionesociale"));
					result.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					result.setDataFondazione(rs.getDate("datafondazione"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia input) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (input == null) {
			throw new Exception("Valore di input non ammesso.");
		}
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"insert into compagnia (ragioneSociale, fatturatoAnnuo, dataFondazione) values (?, ?, ?);")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Compagnia input) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (input == null || input.getId() == null || input.getId() < 1) {
			throw new Exception("Valore di input non ammesso.");
		}
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"update compagnia set ragioneSociale=?, fatturatoAnnuo=?, dataFondazione=? where id=?;")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			ps.setLong(4, input.getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia input) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (input == null || input.getId() == null || input.getId() < 1) {
			throw new Exception("Valore di input non ammesso.");
		}
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("delete from compagnia where id=?")) {
			ps.setLong(1, input.getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {
		if (isNotActive()) {
			throw new Exception("Connessione interrotta. Impossibile fare operazioni DAO");
		}
		String query = "select* from compagnia  where 1=1 ";
		List<Compagnia> result = new ArrayList<Compagnia>();

		if (input == null) {
			throw new Exception("Non è ammesso una compagnia null");
		}

		if (input.getRagioneSociale() != null && !input.getRagioneSociale().isBlank()) {
			query += " and ragionesociale like '" + input.getRagioneSociale() + "%' ";
		}

		if (input.getFatturatoAnnuo() > 0) {
			query += " and fatturatoannuo like '" + input.getFatturatoAnnuo() + "%'";
		}

		if (input.getDataFondazione() != null) {
			query += "and datafondazione  >'" + new java.sql.Date(input.getDataFondazione().getTime()) + "'";
		}
		query += ";";

		try (Statement state = connection.createStatement()) {
			try (ResultSet rs = state.executeQuery(query);) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTemp.setDataFondazione(rs.getDate("datafondazione"));
					compagniaTemp.setId(rs.getLong("id"));
					result.add(compagniaTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	@Override
	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date dataMin) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (dataMin == null) {
			throw new Exception("Valore di input non ammesso.");
		}
		ArrayList<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection
				.prepareStatement("select * from impiegato i inner join compagnia c on i.dataAssunzione > ? ;")) {
			ps.setDate(1, new java.sql.Date(dataMin.getTime()));

			try (ResultSet rs = ps.executeQuery();) {
				Compagnia tempo = new Compagnia();
				tempo.setRagioneSociale(rs.getString("ragionesociale"));
				tempo.setId(rs.getLong("id"));

				Impiegato temp = new Impiegato();
				temp.setNome(rs.getString("nome"));
				temp.setCognome(rs.getString("cognome"));
				temp.setCodiceFiscale(rs.getString("codicefiscale"));
				temp.setDataNascita(rs.getDate("datanascita"));
				temp.setDataAssunzione(rs.getDate("dataassunzione"));
				temp.setCompagnia(tempo);
				temp.setId(rs.getLong("id"));
				result.add(temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	@Override
	public List<Compagnia> findAllByRagioneSocialeContiene(String ragioneSocialeCercare) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}

		if (ragioneSocialeCercare.equals(null)) {
			throw new Exception("Valore di input non ammesso.");
		}

		List<Compagnia> result = new ArrayList<Compagnia>();

		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where ragionesociale=? ;")) {
			ps.setString(1, ragioneSocialeCercare);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTemp.setDataFondazione(rs.getDate("datafondazione"));
					compagniaTemp.setId(rs.getLong("id"));
					result.add(compagniaTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

}
