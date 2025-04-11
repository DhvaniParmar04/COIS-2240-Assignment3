import java.util.Scanner;
import java.time.LocalDate;

public class VehicleRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RentalSystem rentalSystem = RentalSystem.getInstance();

        while (true) {
            System.out.println("\n1: Add Vehicle\n2: Add Customer\n3: Rent Vehicle\n4: Return Vehicle\n5: Display Available Vehicles\n6: Show Rental History\n0: Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("  1: Car\n  2: Motorcycle\n  3: Truck");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter license plate: ");
                    String plate = scanner.nextLine().toUpperCase();
                    System.out.print("Enter make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    Vehicle vehicle = null;

                    try {
                        if (type == 1) {
                            System.out.print("Enter number of seats: ");
                            int seats = scanner.nextInt();
                            scanner.nextLine();
                            vehicle = new Car(make, model, year, seats);
                        } else if (type == 2) {
                            System.out.print("Has sidecar? (true/false): ");
                            boolean sidecar = scanner.nextBoolean();
                            scanner.nextLine();
                            vehicle = new Motorcycle(make, model, year, sidecar);
                        } else if (type == 3) {
                            System.out.print("Enter the cargo capacity: ");
                            double cargoCapacity = scanner.nextDouble();
                            scanner.nextLine();
                            vehicle = new Truck(make, model, year, cargoCapacity);
                        } else {
                            System.out.println("Invalid vehicle type.");
                        }

                        if (vehicle != null) {
                            vehicle.setLicensePlate(plate);
                            if (rentalSystem.addVehicle(vehicle)) {
                                System.out.println("Vehicle added.");
                            } else {
                                System.out.println("Duplicate vehicle. Vehicle not added.");
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;

                case 2:
                    System.out.print("Enter customer ID: ");
                    int cid = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    String cname = scanner.nextLine();

                    Customer customer = new Customer(cid, cname);
                    if (rentalSystem.addCustomer(customer)) {
                        System.out.println("Customer added.");
                    } else {
                        System.out.println("Duplicate customer. Customer not added.");
                    }
                    break;

                case 3:
                    System.out.println("Available Vehicles:");
                    rentalSystem.displayAvailableVehicles();

                    System.out.print("Enter license plate: ");
                    String rentPlate = scanner.nextLine().toUpperCase();

                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer ID: ");
                    String cidRent = scanner.nextLine();

                    System.out.print("Enter rental amount: ");
                    double rentAmount = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vehicleToRent = rentalSystem.findVehicleByPlate(rentPlate);
                    Customer customerToRent = rentalSystem.findCustomerById(Integer.parseInt(cidRent));

                    if (vehicleToRent == null || customerToRent == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    if (rentalSystem.rentVehicle(vehicleToRent, customerToRent, LocalDate.now(), rentAmount)) {
                        System.out.println("Vehicle rented successfully.");
                    } else {
                        System.out.println("Vehicle is already rented.");
                    }
                    break;

                case 4:
                    System.out.println("All Vehicles:");
                    rentalSystem.displayAllVehicles();

                    System.out.print("Enter license plate: ");
                    String returnPlate = scanner.nextLine().toUpperCase();

                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer ID: ");
                    String cidReturn = scanner.nextLine();

                    System.out.print("Enter return fees: ");
                    double returnFees = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vehicleToReturn = rentalSystem.findVehicleByPlate(returnPlate);
                    Customer customerToReturn = rentalSystem.findCustomerById(Integer.parseInt(cidReturn));

                    if (vehicleToReturn == null || customerToReturn == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    if (rentalSystem.returnVehicle(vehicleToReturn, customerToReturn, LocalDate.now(), returnFees)) {
                        System.out.println("Vehicle returned successfully.");
                    } else {
                        System.out.println("Vehicle was not rented.");
                    }
                    break;

                case 5:
                    System.out.println("Available Vehicles:");
                    rentalSystem.displayAvailableVehicles();
                    break;

                case 6:
                    System.out.println("Rental History:");
                    rentalSystem.displayRentalHistory();
                    break;

                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
