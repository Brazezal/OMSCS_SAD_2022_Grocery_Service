package edu.gatech.cs6310.Entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @Column
    protected String firstName;

    @Column
    protected String lastName;

    @Column
    protected String phoneNumber;

    @Column
    protected String password;


//    @OneToMany(cascade = CascadeType.REFRESH ,fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    protected Set<Role> roles = new HashSet<>();

//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
//    //setRole for customer
//    public void setRole(String roleName) {
//        Role role = new Role(roleName);
//        this.roles = roles;
//    }

    public User () {

    }

    public User(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }
//    public String getDecodedPassword(){
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPass=password;
//        String decodedPassword = encoder.(encodedPass);
//        return decodedPassword;
//
//    }
    public void setEncodedPassword(String originPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(originPassword);
        this.password = password;

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public long getId() {
        return Id;
    }



    public void setId(long id) {
        Id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
//    public void addRole(Role role) {
//        this.roles.add(role);
//    }
    @Override
    public int hashCode() {
        int result;
        result = getFirstName().hashCode();
        result = 29 * result + getLastName().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        final User user = (User) obj;

        if (!user.getFirstName().equals(getFirstName())) {
            return false;
        }

        if (!(user.getLastName().equals(getLastName()))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
