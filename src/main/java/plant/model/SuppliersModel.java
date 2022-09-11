package plant.model;

/**
 * Models plant suppliers
 */
public class SuppliersModel {
    private String businessName;
    private int streetNum;
    private String streetName;
    private String postalCode;
    private String city;
    private String province;
    public String getBusinessName() {
        return businessName;
    }
    public int getStreetNum() {
        return streetNum;
    }
    public String getStreetName() {
        return streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }


    public SuppliersModel(String businessName, int streetNum, String streetName, String postalCode, String city, String province) {
        this.businessName = businessName;
        this.streetNum = streetNum;
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.city = city;
        this.province = province;
    }
}
