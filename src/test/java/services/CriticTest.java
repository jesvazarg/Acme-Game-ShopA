
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Critic;

/**
 * Esta clase permite la realizacion de los test correspondientes a los casos de uso
 * "Registrar crítico" y "Editar perfi del crítico" para comprobar que se crean adecuadamente.
 * 
 * @author Jesús Vázquez Argumedo
 * 
 */

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CriticTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CriticService	criticService;


	// Tests ------------------------------------------------------------------
	// FUNCTIONAL REQUIREMENTS
	// Un actor autenticado como administrador debe ser capaz de:
	// Dar de alta a un crítico.

	//Registrar un nuevo crítico
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * 
	 * En este test vamos a comprobar que un usuario puede registrar correctamente
	 * un crítico estando logueado como administador.
	 * 
	 * El primer test negativo es causado porque no estamos logueado,
	 * el segundo de ellos se produce porque no aceptamos las condiones de uso,
	 * el tercero se produce porque dejamos el campo revista en blanco y
	 * el cuarto es provocado porque intentamos registrar con un nombre de usuario ya existente.
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 * 
	 */
	@Test
	public void driverRegisterCritic() {
		final Object testingData[][] = {
			{
				"admin", "critic10", "critic10", "critic10", "Raul", "López", "critic10@gmail.com", "1234", "ese cadi oe!", true, null
			}, {
				"admin", "critic11", "critic11", "critic11", "Jesusito", "Velázquez", "critic11@gmail.com", "1234", "ese cadi oe!", true, null
			}, {
				null, "critic12", "critic12", "critic12", "Aridane", "Hernández", "critic12@gmail.com", "1234", "ese cadi oe!", true, IllegalArgumentException.class
			}, {
				"admin", "critic13", "critic13", "critic13", "Matias", "Pavoni", "critic13@gmail.com", "1234", "ese cadi oe!", false, IllegalArgumentException.class
			}, {
				"admin", "critic14", "critic14", "critic14", "Francisco Javier", "Zafra", "critic14@gmail.com", "1234", "", true, ConstraintViolationException.class
			}, {
				"admin", "critic1", "critic1", "critic1", "Lucas", "Lobos", "critic1@gmail.com", "1234", "ese cadi oe!", true, DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerCritic((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (Boolean) testingData[i][9], (Class<?>) testingData[i][10]);
	}
	protected void registerCritic(final String loged, final String username, final String password, final String confirmPassword, final String name, final String surname, final String email, final String phone, final String magazine,
		final Boolean isAgree, final Class<?> expected) {

		Critic critic = null;
		Class<?> caught = null;
		Md5PasswordEncoder encoder;
		String passwordEncoded;

		try {
			this.authenticate(loged);

			critic = this.criticService.create();
			critic.getUserAccount().setUsername(username);

			Assert.isTrue(password.equals(confirmPassword));
			encoder = new Md5PasswordEncoder();
			passwordEncoded = encoder.encodePassword(password, null);
			critic.getUserAccount().setPassword(passwordEncoded);

			critic.setName(name);
			critic.setSurname(surname);
			critic.setEmail(email);
			critic.setPhone(phone);
			critic.setMagazine(magazine);

			Assert.isTrue(isAgree);

			this.criticService.saveRegister(critic);
			this.criticService.findAll();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Editar el perfil de un critic
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * 
	 * En este test vamos a comprobar que un usuario puede editar el perfil de usuario
	 * correctamente de un crítico.
	 * 
	 * El primer test negativo es causado porque no coinciden la contraseña y la repetición,
	 * el segundo de ellos se produce porque introducimos el correo con un formato incorrectoy
	 * el tercero se produce porque dejamos el campo revista en blanco.
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 * 
	 */
	@Test
	public void driverEditCritic() {
		final Object testingData[][] = {
			{
				"critic1", "critic1", "critic1", "Manolo", "Pérez", "manolo@gmail.com", "1234", "ese cadi oe!", null
			}, {
				"critic2", "critic2", "critic2", "Alfredo", "Ortuño", "alfredo@gmail.com", "1234", "ese cadi oe!", null
			}, {
				"critic1", "critic1", "crtc4", "Mágico", "González", "magico@gmail.com", "1234", "ese cadi oe!", IllegalArgumentException.class
			}, {
				"critic2", "critic2", "critic2", "Salvi", "Sánchez", "salvi", "1234", "ese cadi oe!", ConstraintViolationException.class
			}, {
				"critic3", "critic3", "critic3", "Armando", "Ribeiro", "armando@gmail.com", "1234", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editCritic((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(Class<?>) testingData[i][8]);
	}

	protected void editCritic(final String username, final String password, final String confirmPassword, final String name, final String surname, final String email, final String phone, final String magazine, final Class<?> expected) {

		Critic critic = null;
		Class<?> caught = null;
		Md5PasswordEncoder encoder;
		String passwordEncoded;

		try {
			this.authenticate(username);

			critic = this.criticService.findByPrincipal();

			Assert.isTrue(password.equals(confirmPassword));
			encoder = new Md5PasswordEncoder();
			passwordEncoded = encoder.encodePassword(password, null);
			critic.getUserAccount().setPassword(passwordEncoded);

			critic.setName(name);
			critic.setSurname(surname);
			critic.setEmail(email);
			critic.setPhone(phone);
			critic.setMagazine(magazine);

			this.criticService.save(critic);
			this.criticService.findAll();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
