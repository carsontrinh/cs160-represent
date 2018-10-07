package com.carsontrinh.cs160.represent;

public class LegislatorInfo {

    public String firstName;  // e.g. Kamala
    public String lastName;  // e.g. Harris

    public String representativeType;  // e.g. Representative, Senator
    public String party;  // e.g. Democrat, Republican, Independent

    public String state;  // e.g. CA
    public String district;  // e.g. Congressional District 13

    public String url;  // e.g. https://www.harris.senate.gov
    public String phoneNumber;  // e.g. 202-224-3553
    public String contactForm;  // e.g. https://www.harris.senate.gov/contact

    public LegislatorInfo(String firstName, String lastName,
                          String representativeType, String party,
                          String state, String district,
                          String url, String phoneNumber, String contactForm) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.representativeType = representativeType;
        this.party = party;
        this.state = state;
        this.district = district;
        this.url = url;
        this.phoneNumber = phoneNumber;
        this.contactForm = contactForm;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        LegislatorInfo legislator = (LegislatorInfo) object;
        if (this.firstName.equals(legislator.firstName) && this.lastName.equals(legislator.lastName)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        String fullName = this.firstName + this.lastName;
        return fullName.hashCode();
    }

}
