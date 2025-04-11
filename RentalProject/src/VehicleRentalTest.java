import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;

public class VehicleRentalTest {

    private RentalSystem rentalSystem;
    private Vehicle car;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        rentalSystem = RentalSystem.getInstance();
        car = new Car("Toyota", "Corolla", 2019, 5);
        car.setLicensePlate("AAA123");
        customer = new Customer(1, "George");
        rentalSystem.addVehicle(car);
        rentalSystem.addCustomer(customer);
    }

    @Test
    public void testLicensePlateValidation() {
        assertDoesNotThrow(() -> {
            Vehicle v = new Car("Honda", "Civic", 2020, 5);
            v.setLicensePlate("BBB456");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Vehicle v = new Car("Test", "Test", 2020, 5);
            v.setLicensePlate("12ABCD");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Vehicle v = new Car("Test", "Test", 2020, 5);
            v.setLicensePlate("A1");
        });
    }

    @Test
    public void testRentAndReturnVehicle() {
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, car.getStatus());

        assertTrue(rentalSystem.rentVehicle(car, customer, LocalDate.now(), 150.0));
        assertEquals(Vehicle.VehicleStatus.RENTED, car.getStatus());

        assertFalse(rentalSystem.rentVehicle(car, customer, LocalDate.now(), 100.0));

        assertTrue(rentalSystem.returnVehicle(car, customer, LocalDate.now(), 10.0));
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, car.getStatus());

        assertFalse(rentalSystem.returnVehicle(car, customer, LocalDate.now(), 10.0));
    }

    @Test
    public void testSingletonRentalSystem() throws Exception {
        Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        RentalSystem r1 = RentalSystem.getInstance();
        RentalSystem r2 = RentalSystem.getInstance();
        assertSame(r1, r2);
    }
}
