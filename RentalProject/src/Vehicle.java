public abstract class Vehicle {
    public enum VehicleStatus { AVAILABLE, RENTED }

    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public Vehicle(String make, String model, int year) {
        this.make = capitalize(make);
        this.model = capitalize(model);
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String plate) {
        if (!isValidPlate(plate)) {
            throw new IllegalArgumentException("License plate must be in format AAA123.");
        }
        this.licensePlate = plate.toUpperCase();
    }

    private boolean isValidPlate(String plate) {
        return plate != null && plate.matches("[A-Z]{3}[0-9]{3}");
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }

    public String getInfo() {
        return licensePlate + " | " + make + " " + model + " | " + year + " | " + status;
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
