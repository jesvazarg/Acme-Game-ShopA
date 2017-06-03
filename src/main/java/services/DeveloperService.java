
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.DeveloperRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Developer;
import domain.Game;
import domain.MessageEmail;
import forms.CreateActorForm;
import forms.CreateDeveloperForm;

@Service
@Transactional
public class DeveloperService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private DeveloperRepository	developerRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public DeveloperService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Developer findOne(final int developerId) {
		Developer developer;

		developer = this.developerRepository.findOne(developerId);
		Assert.notNull(developer);

		return developer;
	}

	public Collection<Developer> findAll() {
		Collection<Developer> result;

		result = this.developerRepository.findAll();

		return result;
	}

	public Developer create() {
		Developer result;
		UserAccount userAccount;
		Authority authority;
		Collection<MessageEmail> sentMessages;
		Collection<MessageEmail> receivedMessages;
		Collection<Game> games;

		userAccount = new UserAccount();
		authority = new Authority();
		sentMessages = new ArrayList<MessageEmail>();
		receivedMessages = new ArrayList<MessageEmail>();
		games = new ArrayList<Game>();

		authority.setAuthority(Authority.DEVELOPER);
		userAccount.addAuthority(authority);

		result = new Developer();
		result.setUserAccount(userAccount);
		result.setSentMessages(sentMessages);
		result.setReceivedMessages(receivedMessages);
		result.setGames(games);

		return result;
	}

	public Developer save(final Developer developer) {
		Assert.notNull(developer);
		Developer result;

		result = this.developerRepository.save(developer);
		return result;
	}

	// Other business methods -------------------------------------------------

	public Developer findByPrincipal() {
		Developer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Developer findByUserAccountId(final int userAccountId) {
		Assert.isTrue(userAccountId != 0);

		Developer result;

		result = this.developerRepository.findByUserAccountId(userAccountId);

		return result;
	}

	public Developer reconstructCreate(final CreateActorForm createActorForm) {
		Assert.notNull(createActorForm);
		Developer developer;
		String password;

		Assert.isTrue(createActorForm.getPassword().equals(createActorForm.getConfirmPassword())); // Comprobamos que las dos contrase�as sean la misma
		Assert.isTrue(createActorForm.getIsAgree()); // Comprobamos que acepte las condiciones

		developer = this.create();
		password = this.encryptPassword(createActorForm.getPassword());

		developer.getUserAccount().setUsername(createActorForm.getUsername());
		developer.getUserAccount().setPassword(password);
		developer.setName(createActorForm.getName());
		developer.setSurname(createActorForm.getSurname());
		developer.setEmail(createActorForm.getEmail());
		developer.setPhone(createActorForm.getPhone());

		return developer;
	}

	public CreateActorForm desreconstructCreate(final Developer developer) {
		CreateActorForm createActorForm;

		createActorForm = new CreateActorForm();

		createActorForm.setUsername(developer.getUserAccount().getUsername());
		createActorForm.setName(developer.getName());
		createActorForm.setName(developer.getSurname());
		createActorForm.setEmail(developer.getEmail());
		createActorForm.setPhone(developer.getPhone());

		return createActorForm;
	}

	public String encryptPassword(String password) {
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);

		return password;
	}

	public Collection<Developer> developerMoreSells() {
		return this.developerRepository.developerMoreSells();
	}

	public Collection<Developer> developerLessSells() {
		return this.developerRepository.developerLessSells();
	}

	public Double avgDeveloperPerSellGames() {
		Double res = this.developerRepository.avgDeveloperPerSellGames();
		res = Math.round(res * 100) / 100.0;
		return res;
	}

	public List<Collection<Developer>> developerWithGameBetterAndWorstReview() {
		final List<Collection<Developer>> res = new ArrayList<Collection<Developer>>();
		final Collection<Developer> resMax = new ArrayList<Developer>();
		final Collection<Developer> resMin = new ArrayList<Developer>();
		List<Object[]> lista;
		Game game;
		Double avg = 0.0;
		Double max = -1.0;
		Double min = 11.0;

		lista = this.developerRepository.developerWithGameBetterAndWorstReview();
		for (int i = 0; i < lista.size(); i++) {
			avg = (Double) lista.get(i)[1];
			if (avg >= max) {
				game = (Game) lista.get(i)[0];
				resMax.add(game.getDeveloper());
				max = avg;
			} else
				break;
		}
		res.add(resMax);
		for (int i = lista.size() - 1; i >= 0; i--) {
			avg = (Double) lista.get(i)[1];
			if (avg <= min) {
				game = (Game) lista.get(i)[0];
				resMin.add(game.getDeveloper());
				min = avg;
			} else
				break;
		}
		res.add(resMin);

		return res;
	}
	public Developer reconstructProfile(final CreateDeveloperForm createDeveloperForm, final String type) {
		Assert.notNull(createDeveloperForm);
		Developer developer = null;
		Md5PasswordEncoder encoder;
		String password;

		Assert.isTrue(createDeveloperForm.getPassword().equals(createDeveloperForm.getConfirmPassword()));

		//Creo uno nuevo vacio para meterle los datos del formulario a dicho customer
		if (type.equals("create")) {
			developer = this.create();
			Assert.isTrue(createDeveloperForm.getIsAgree());
		} else if (type.equals("edit"))
			developer = this.findByPrincipal();

		password = createDeveloperForm.getPassword();

		encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);

		developer.getUserAccount().setUsername(createDeveloperForm.getUsername());
		developer.getUserAccount().setPassword(password);
		developer.setName(createDeveloperForm.getName());
		developer.setSurname(createDeveloperForm.getSurname());
		developer.setEmail(createDeveloperForm.getEmail());
		developer.setPhone(createDeveloperForm.getPhone());
		developer.setCompany(createDeveloperForm.getCompany());

		return developer;
	}

	public CreateDeveloperForm constructProfile(final Developer developer) {
		Assert.notNull(developer);
		CreateDeveloperForm createDeveloperForm;

		createDeveloperForm = new CreateDeveloperForm();
		createDeveloperForm.setUsername(developer.getUserAccount().getUsername());
		createDeveloperForm.setPassword(developer.getUserAccount().getPassword());
		createDeveloperForm.setName(developer.getName());
		createDeveloperForm.setSurname(developer.getSurname());
		createDeveloperForm.setEmail(developer.getEmail());
		createDeveloperForm.setPhone(developer.getPhone());
		createDeveloperForm.setCompany(developer.getCompany());

		return createDeveloperForm;
	}

	public Collection<Developer> developersWithBestSellersQuantity() {
		Collection<Object[]> aux;
		final Collection<Developer> result = new HashSet<Developer>();
		aux = this.developerRepository.developersWithBestSellersQuantity();
		Long vendedorDeReferencia = 0L;

		//Obtengo el valor del numero de ventas del mejor vendedor.
		for (final Object[] valueAux : aux) {

			vendedorDeReferencia = (Long) valueAux[1];

			break;

		}

		//Comparo el numero de ventas de cada developer y en cuanto llego al primer developer
		//que est� por debajo, paro el for y devuelvo los developers con mejores ventas.

		for (final Object[] valueAux : aux) {
			final Long sells = (Long) valueAux[1];

			if (sells < vendedorDeReferencia)
				break;
			Developer developer;
			developer = this.findOne((int) valueAux[0]);
			result.add(developer);
		}
		return result;
	}

}
