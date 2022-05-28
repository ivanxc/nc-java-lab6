package com.ivanxc.netcracker.bookshop.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.ivanxc.netcracker.bookshop.dto.CustomerSurnameDiscountDto;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import com.ivanxc.netcracker.bookshop.integration.IntegrationTestBase;
import com.ivanxc.netcracker.bookshop.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class CustomerRepositoryTest extends IntegrationTestBase {

    private final CustomerRepository repository;

    @Test
    void findDistinctDistricts() {
        Customer customer1 = Customer
            .builder().surname("Петров").district("Советский").discount((short) 5).build();
        Customer customer2 = Customer
            .builder().surname("Иванов").district("Советский").discount((short) 5).build();
        Customer customer3 = Customer
            .builder().surname("Сидоров").district("Нижегородский").discount((short) 10).build();

        repository.save(customer1);
        repository.save(customer2);
        repository.save(customer3);

        List<String> distinctDistricts = List.of("Нижегородский", "Советский");
        List<String> actual = repository.findDistinctDistricts();

        assertThat(actual).hasSize(2).hasSameElementsAs(distinctDistricts);
    }

    @Test
    void findCustomerSurnameAndDiscountByDistrict() {
        Customer customer1 = Customer
            .builder().surname("Петров").district("Советский").discount((short) 5).build();
        Customer customer2 = Customer
            .builder().surname("Иванов").district("Советский").discount((short) 5).build();
        Customer customer3 = Customer
            .builder().surname("Сидоров").district("Нижегородский").discount((short) 10).build();

        repository.save(customer1);
        repository.save(customer2);
        repository.save(customer3);

        List<CustomerSurnameDiscountDto> actual =
            repository.findCustomerSurnameAndDiscountLivingIn("Нижегородский");

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getSurname()).isEqualTo("Сидоров");
    }

}