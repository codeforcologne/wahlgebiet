package de.illilli.opendata.service.wahlgebiet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.illilli.opendata.service.Config;

@Path("/")
public class Service {

	private final static Logger logger = Logger.getLogger(Service.class);
	public static final String ENCODING = Config.getProperty("encoding");

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/empty")
	public String getEmpty() {
		return "{empty}";
	}

	/**
	 * Example:
	 * <p>
	 * <a href="http://localhost:8080/wahlgebiet/service/stimmbezirke">
	 * /wahlgebiet/service/stimmbezirke</a>
	 * </p>
	 * 
	 * @return
	 * @throws MismatchedDimensionException
	 * @throws JsonProcessingException
	 * @throws NoSuchAuthorityCodeException
	 * @throws IOException
	 * @throws FactoryException
	 * @throws TransformException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/stimmbezirke")
	public String getStimmbezirke() throws MismatchedDimensionException, JsonProcessingException,
			NoSuchAuthorityCodeException, IOException, FactoryException, TransformException {

		request.setCharacterEncoding(Config.getProperty("encoding"));
		response.setCharacterEncoding(Config.getProperty("encoding"));

		URL url = GeoJsonWahllokalFacade.class.getResource("/stimmbezirk/Stimmbezirk.shp");
		return new GeoJsonStimmbezirkeFacade(url).getJson();
	}

}
