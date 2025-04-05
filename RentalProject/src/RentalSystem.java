import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;


public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
private static RentalSystem instance = null; // PRIVATE SINGLETON VARIABLE

//PRIVATE CONSTRUCTOR
private RentalSystem() {
loadData(); 
}

// INTRODUCING getInstance()
public static RentalSystem getInstance() {
	if ( instance == null) {
			instance = new RentalSystem ();
		}
		return instance;
	}

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle); 
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer); 
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            saveRecord(new RentalRecord ( vehicle, customer,date,amount,"RENT"));// ADD THIS
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(boolean onlyAvailable) {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (!onlyAvailable || v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(String id) {
        for (Customer c : customers)
            if (c.getCustomerId() == Integer.parseInt(id))
                return c;
        return null;
    }
 //  Save a vehicle to vehicles.txt
    public void saveVehicle(Vehicle vehicle) {
        try (FileWriter writer = new FileWriter("vehicles.txt", true)) { // true = append mode
            writer.write(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving vehicle: " + e.getMessage());
        }
    }

    //  Save a customer to customers.txt
    public void saveCustomer(Customer customer) {
        try (FileWriter writer = new FileWriter("customers.txt", true)) {
            writer.write(customer.getCustomerId() + "," + customer.getCustomerName() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    // Save a rental record to rental_records.txt
    public void saveRecord(RentalRecord record) {
        try (FileWriter writer = new FileWriter("rental_records.txt", true)) {
            writer.write(record.toString() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving rental record: " + e.getMessage());
        }
    }
    
    private void loadData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("vehicles.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Vehicle v = new Car(parts[1], parts[2], Integer.parseInt(parts[3]), 4);
                    v.setLicensePlate(parts[0]);
                    v.setStatus(Vehicle.VehicleStatus.valueOf(parts[4]));
                    vehicles.add(v);
                }
            }
        } catch (Exception e) {
            System.out.println("No vehicles loaded.");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get("customers.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    customers.add(new Customer(Integer.parseInt(parts[0]), parts[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("No customers loaded.");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get("rental_records.txt"));
            for (String line : lines) {
               
            }
        } catch (Exception e) {
            System.out.println("No rental records loaded.");
        }
    }
}