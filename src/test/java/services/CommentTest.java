
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Comment;
import domain.Game;

/**
 * Esta clase permite la realizacion de los test correspondientes
 * al caso de uso "A�adir comentarios a los juegos" para comprobar
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
public class CommentTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CustomerService	customerService;

	@Autowired
	private CommentService	commentService;

	@Autowired
	private GameService		gameService;


	// Tests ------------------------------------------------------------------
	// FUNCTIONAL REQUIREMENTS
	//-	Un actor autenticado como cliente debe ser capaz de:
	//	A�adir comentarios a los juegos.

	//El primer test negativo es causado porque no nos hemos logueado correctmente como customer, el segundo de
	//ellos se produce porque le ponemos un score fuera del rango 0-10 y el tercero es provocado porque le
	//pasamos un id de game que no existe.
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * - Un actor autenticado como cliente debe ser capaz de: A�adir comentarios a los juegos.
	 * 
	 * En este test vamos a comprobar que un usuario puede a�adir correctamente
	 * comentarios a un juego.
	 * 
	 * El primer test negativo es causado porque no nos hemos logueado correctmente como customer, el segundo de
	 * ellos se produce porque le ponemos un score fuera del rango 0-10 y el tercero es provocado porque le
	 * pasamos un id de game que no existe.
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 * 
	 */
	@Test
	public void driverA�adirComentarioAUnJuego() {
		final Object testingData[][] = {
			{
				"customer1", 94, "Titulo", "Descripcion", 0, null
			}, {
				"customer2", 95, "Titulo", "Descripcion", 8, null
			}, {
				"customer3", 96, "Titulo", "Descripcion", 10, null
			}, {
				"customerNoExist", 94, "Titulo", "Descripcion", 5, IllegalArgumentException.class
			}, {
				"customer1", 95, "Titulo", "Descripcion", 11, ConstraintViolationException.class
			}, {
				"customer3", 0, "Titulo", "Descripcion", 5, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.a�adirComentarioAUnJuego((String) testingData[i][0], (int) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void a�adirComentarioAUnJuego(final String username, final int idGame, final String titulo, final String descripcion, final int score, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final Game game = this.gameService.findOne(idGame);
			final Comment comment = this.commentService.create(game);
			comment.setTitle(titulo);
			comment.setDescription(descripcion);
			comment.setScore(score);
			this.commentService.save(comment);

			this.commentService.findAll();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

}
