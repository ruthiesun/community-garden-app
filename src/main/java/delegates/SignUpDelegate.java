package delegates;

public interface SignUpDelegate {
    boolean registerUser(String streetName, String houseNum, String postalCode, String province, String email, String password,String cityName,String username);
}
