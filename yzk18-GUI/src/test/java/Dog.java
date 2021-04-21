import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Dog
{

    private int age;
    private String name;
    private File photo;
    private boolean gender;
    private double height;
    private LocalDate 生日;
    private LocalTime 时辰;
    private LocalDateTime 注册时间;

    public int getAge() {
        return age;
    }

    public Dog setAge(int age) {
        this.age = age;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dog setName(String name) {
        this.name = name;
        return this;
    }

    public File getPhoto() {
        return photo;
    }

    public Dog setPhoto(File photo) {
        this.photo = photo;
        return this;
    }

    public boolean isGender() {
        return gender;
    }

    public double getHeight() {
        return height;
    }

    public Dog setHeight(double height) {
        this.height = height;
        return this;
    }

    public Dog setGender(boolean gender) {
        this.gender = gender;
        return this;
    }

    public LocalDate get生日() {
        return 生日;
    }

    public Dog set生日(LocalDate 生日) {
        this.生日 = 生日;
        return this;
    }

    public LocalTime get时辰() {
        return 时辰;
    }

    public Dog set时辰(LocalTime 时辰) {
        this.时辰 = 时辰;
        return this;
    }

    public LocalDateTime get注册时间() {
        return 注册时间;
    }

    public Dog set注册时间(LocalDateTime 注册时间) {
        this.注册时间 = 注册时间;
        return this;
    }

}