package de.illilli.opendata.service.wahlgebiet.stimmbezirk.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import de.illilli.jdbc.Select;

public class SelectStimmbezirkWherePoint extends Select<StimmbezirkDTO> {

	private String queryString = "/selectStimmbezirkWherePoint.sql";
	private static final int SRID = 4326;

	public SelectStimmbezirkWherePoint(double lng, double lat) throws SQLException, NamingException, IOException {
		setQueryString(queryString);
		Object[] params = new Object[] { lng, lat, SRID };
		runSelect(new BeanListHandler<StimmbezirkDTO>(StimmbezirkDTO.class), params);
	}
}
