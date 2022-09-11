package communitygarden.model;

public class Garden {

    private int id;
    private String name;
    private String street;
    private int houseNum;
    private String postalCode;

    private String city;

    private String province;

    private Double latitude;
    private Double longitude;

    public Garden(int id, String name, int houseNum, String street, String city, String province, String postalCode) {
        this.id = id;
        this.name = name;
        this.houseNum = houseNum;
        this.street = street;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public Garden(int id, String name, int houseNum, String street, String city, String province, String postalCode, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.houseNum = houseNum;
        this.street = street;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNum() {
        return houseNum;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return houseNum + " " + street;
    }

    public String getCityProvince() {
        return city + ", " + province;
    }
}
