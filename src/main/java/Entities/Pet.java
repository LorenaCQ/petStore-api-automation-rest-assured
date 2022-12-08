package Entities;

public class Pet {
    private int petid;
    private String namepet;

    public Pet(int petid, String namepet) {
        this.petid = petid;
        this.namepet = namepet;
    }

    public int getIdpet() {
        return petid;
    }

    public void setIdpet(int petid) {
        this.petid = petid;
    }

    public String getNamepet() {
        return namepet;
    }

    public void setNamepet(String namepet) {
        this.namepet = namepet;
    }
}
