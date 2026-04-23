// Appliance Object
class Appliance {
    //Variables
    private String type = "X";
    private String brand = "X";
    private long serial = 1000000;
    private double price = 1;
    //Variables shared by all appliances
    private static long nextSerial = 1000000;
    private static int applianceCount = 0;

    //Constructor
    public Appliance(String type, String brand, double price) {
        this.type = type;
        this.brand = brand;
        this.price = price;
        this.serial = nextSerial;
        nextSerial++;
        applianceCount++;
    }

    // Load Constructor (does not auto-generate serial)
    public Appliance(String type, String brand, double price, long serial) {
        this.type = type;
        this.brand = brand;
        this.price = price;
        this.serial = serial;
        if (serial >= nextSerial) nextSerial = serial + 1; // keep nextSerial ahead
        applianceCount++;
    }

    //Setters
    public void setType(String type) {this.type = type;}
    public void setBrand(String brand) { this.brand = brand; }
    public void setPrice(double price) { this.price = price; }

    //Getters
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public long getSerial() { return serial; }
    public double getPrice() { return price; }
    public static int findNumberOfCreatedAppliances() { return applianceCount; }

    // Save Method
    public String toCSV() {
        return serial + "," + type + "," + brand + "," + price;
    }

    //Override toString Method
    @Override
    public String toString() {
        return "Appliance Serial # " + serial + "\nBrand: " + brand
                + "\nType: " + type + "\nPrice: " + price ;
    }

    //Override Equals Method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appliance other = (Appliance) obj;
        return (this.type.equals(other.type) &&
                this.brand.equals(other.brand) &&
                Double.compare(this.price, other.price) == 0);
    }
}