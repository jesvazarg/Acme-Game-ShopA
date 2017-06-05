
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Discount;

/**
 * Esta clase permite la realizacion de los test correspondientes
 * a los casos de uso "Crear cupón descuento", "Editar cupón descpues" y
 * "Borrar cupón descuento" para comprobar que se crean adecuadamente.
 * 
 * @author Jesús Vázquez Argumedo
 * 
 */

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DiscountTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private DiscountService	discountService;


	// Tests ------------------------------------------------------------------
	// FUNCTIONAL REQUIREMENTS

	//Registrar un nuevo descuento
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * 
	 * En este test vamos a comprobar que un usuario puede crear correctamente
	 * un cupón descuento.
	 * 
	 * El primer test negativo es causado porque estamos logeado por otro actor y
	 * el segundo se produce porque introducimos una porcentaje fuera de rango del establecido.
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 * 
	 */
	@Test
	public void driverRegisterDiscount() {
		final Object testingData[][] = {
			{
				"admin", 25, null
			}, {
				"admin", 75, null
			}, {
				"critic1", 50, IllegalArgumentException.class
			}, {
				"admin", 200, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerDiscount((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void registerDiscount(final String loged, final Integer percentage, final Class<?> expected) {

		Discount discount = null;
		Class<?> caught = null;

		try {
			this.authenticate(loged);

			discount = this.discountService.create();

			discount.setPercentage(percentage);

			discount = this.discountService.save(discount);
			this.discountService.findAll();
			this.discountService.findOne(discount.getId());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Editar un descuento
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * 
	 * En este test vamos a comprobar que un usuario puede editar correctamente
	 * un cupón descuento.
	 * 
	 * El primer test negativo es causado porque no podemos editar un cupón ya usado,
	 * el segundo se produce porque estamos logeado por otro actor y
	 * el tercero porque introducimos una porcentaje fuera de rango del establecido.
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 * 
	 */
	@Test
	public void driverEditDiscount() {
		final Object testingData[][] = {
			{
				"admin", 121, 1, null
			}, {
				"admin", 118, 100, null
			}, {
				"admin", 117, 1, IllegalArgumentException.class
			}, {
				"critic1", 119, 50, IllegalArgumentException.class
			}, {
				"admin", 120, 400, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDiscount((String) testingData[i][0], (int) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void editDiscount(final String loged, final int discountId, final Integer percentage, final Class<?> expected) {

		Discount discount = null;
		Class<?> caught = null;

		try {
			this.authenticate(loged);

			discount = this.discountService.findOne(discountId);

			discount.setPercentage(percentage);

			discount = this.discountService.save(discount);
			this.discountService.findAll();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Borrar un descuento
	/**
	 * FUNCTIONAL REQUIREMENTS
	 * 
	 * En este test vamos a comprobar que un usuario puede borrar correctamente
	 * un cupón descuento.
	 * 
	 * El primer y único test negativo es causado porque estamos logeado por otro actor.
	 * 
	 * @param No
	 *            es necesario parametro
	 * 
	 * 
	 */
	@Test
	public void driverBorrarDiscount() {
		final Object testingData[][] = {
			{
				"admin", 117, null
			}, {
				"admin", 118, null
			}, {
				"critic1", 119, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.borrarDiscount((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void borrarDiscount(final String loged, final int discountId, final Class<?> expected) {

		Discount discount = null;
		Class<?> caught = null;

		try {
			this.authenticate(loged);

			discount = this.discountService.findOne(discountId);

			this.discountService.delete(discount);
			this.discountService.findAll();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
