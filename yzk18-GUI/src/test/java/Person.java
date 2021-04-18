import java.time.LocalDate;

public class Person {
    private String name;
    private String s1;

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public Person setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    private String phoneNum;
    private int height;
    private LocalDate birthDay;


    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Person setHeight(int height) {
        this.height = height;
        return this;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public Person setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
        return this;
    }
}
