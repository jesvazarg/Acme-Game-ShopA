
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Game;
import domain.Sense;

/**
 * Esta clase permite la realizacion de los test correspondientes
 * al caso de uso "Darle me gusta/no me gusta a los juegos" para comprobar
 * que se crean adecuadamente.
 * 
 * @author Santiago Fraga Mart�n-Arroyo
 * 
 */
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SenseTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CustomerService	customerService;

	@Autowired
	private SenseService	senseService;

	@Autowired
	private GameService		gameService;


	// Tests ------------------------------------------------------------------
	// FUNCTIONAL REQUIREMENTS
	//-	Un actor autenticado como cliente debe ser capaz de:
	//	Darle "me gusta/no me gusta" a los juegos.

	//El primer caso negativo se produce porque intentamos darle like a un juego sin
	//estar logueados, en el segundo nos logueamos como un developer que tampoco puede darle
	//like a un juego y por ultimo introducimos un id que no pertenece a ningun juego
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * - Un actor autenticado como cliente debe ser capaz de: Darle "me gusta/no me gusta" a los juegos..
	 * 
	 * En este test vamos a comprobar que un usuario puede darle "me gusta" a un juego
	 * 
	 * El primer caso negativo se produce porque intentamos darle like a un juego sin
	 * estar logueados, en el segundo nos logueamos como un developer que tampoco puede darle
	 * like a un juego y por ultimo introducimos un id que no pertenece a ningun juego
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 */
	@Test
	public void driverDarMeGustaAUnJuego() {
		final Object testingData[][] = {
			{
				"customer1", 96, null
			}, {
				"customer2", 95, null
			}, {
				"customer3", 96, null
			}, {
				"customerNoExist", 94, IllegalArgumentException.class
			}, {
				"developer1", 94, IllegalArgumentException.class
			}, {
				"customer3", 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.darMeGustaAUnJuego((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void darMeGustaAUnJuego(final String username, final int idGame, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final Game game = this.gameService.findOne(idGame);
			this.senseService.createDislike(game);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//El primer caso negativo se produce porque intentamos darle dislike a un juego sin
	//estar logueados, en el segundo nos logueamos como un developer que tampoco puede darle
	//dislike a un juego y por ultimo introducimos un id que no pertenece a ningun juego

	/**
	 * FUNCTIONAL REQUIREMENTS
	 * - Un actor autenticado como cliente debe ser capaz de: Darle "me gusta/no me gusta" a los juegos..
	 * 
	 * En este test vamos a comprobar que un usuario puede darle "no me gusta" a un juego
	 * 
	 * El primer caso negativo se produce porque intentamos darle like a un juego sin
	 * estar logueados, en el segundo nos logueamos como un developer que tampoco puede darle
	 * like a un juego y por ultimo introducimos un id que no pertenece a ningun juego
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 */
	@Test
	public void driverDarNoGustaAUnJuego() {
		final Object testingData[][] = {
			{
				"customer1", 96, null
			}, {
				"customer2", 95, null
			}, {
				"customer3", 96, null
			}, {
				"customerNoExist", 94, IllegalArgumentException.class
			}, {
				"developer1", 94, IllegalArgumentException.class
			}, {
				"customer3", 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.darNoGustaAUnJuego((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void darNoGustaAUnJuego(final String username, final int idGame, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final Game game = this.gameService.findOne(idGame);
			this.senseService.createDislike(game);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	/**
	 * FUNCTIONAL REQUIREMENTS
	 * - Un actor autenticado como cliente debe ser capaz de: Darle "me gusta/no me gusta" a los juegos..
	 * 
	 * En este test vamos a comprobar que un usuario que ya le ha dado "me gusta" o "no me gusta"
	 * a un juego es capaz de cambiar ese "me gusta/no me gusta" adecuadamente
	 * 
	 * El primer caso negativos se produce porque intentamos darle like a un juego sin
	 * estar logueados, en el segundo nos logueamos como un developer que tampoco puede darle
	 * like a un juego y por ultimo introducimos un id que no pertenece a ningun juego
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 */
	@Test
	public void driverCambiarLikeODislike() {
		final Object testingData[][] = {
			{
				"customer1", 94, 107, false, null
			}, {
				"customer2", 96, 109, true, null
			}, {
				"customer2", 94, 110, false, null
			}, {
				"customerNoExist", 94, 110, false, IllegalArgumentException.class
			}, {
				"developer1", 96, 109, true, IllegalArgumentException.class
			}, {
				"customer2", 96, 0, true, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.cambiarLikeODislike((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void cambiarLikeODislike(final String username, final int idGame, final int idSense, final Boolean likeOrDislike, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final Sense sense = this.senseService.findOne(idSense);
			this.senseService.change(sense, likeOrDislike);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

}
