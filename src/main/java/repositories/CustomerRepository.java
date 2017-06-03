
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select c from Customer c where c.userAccount.id = ?1")
	Customer findByUserAccountId(int userAccountId);

	//C2:Los clientes que m�s comentarios tengan publicados
	@Query("select c1 from Customer c1 where c1.comments.size=(select max(c.comments.size) from Customer c)")
	Collection<Customer> findCustomersWithMoreComments();

}
