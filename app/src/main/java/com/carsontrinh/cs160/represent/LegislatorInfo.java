package com.carsontrinh.cs160.represent;

import android.graphics.Color;

import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Map;

public class LegislatorInfo {

    public static String D_COLOR = "#3582E9";
    public static String R_COLOR = "#E9585B";
    public static String I_COLOR = "#808080";

    public String getFirstName() {
        return capitalizeString(firstName);
    }

    public String getLastName() {
        return capitalizeString(lastName);
    }

    public String getRepresentativeType() {
        return capitalizeString(representativeType);
    }

    public String getParty() {
        if (party.equalsIgnoreCase("Democrat")) {
            party = "Democratic";
        } else if (party.equalsIgnoreCase("Republican")) {
            party = "Republican";
        } else if  (party.equalsIgnoreCase("Independent")) {
            party = "Independent";
        }
        return capitalizeString(party);
    }

    public String getState() {
        return state;
    }

    public String getStateFormatted() {
        return STATE_MAP.get(state.toUpperCase());
    }

    public String getDistrict() {
        return capitalizeString(district);
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getSimpleAddress() {
        return formattedAddress.substring(formattedAddress.indexOf(',') + 2);
    }

    public String getUrl() {
        return url;
    }

    public String getPhoneNumber() {
        return capitalizeString(phoneNumber);
    }

    public String getContactForm() {
        return contactForm;
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {

        return String.format("https://theunitedstates.io/images/congress/%s/%s.jpg\n", "original", this.getId());
    }

    public int getColor() {
        if (party.equalsIgnoreCase("democrat") || party.equalsIgnoreCase("democratic")) {
            return Color.parseColor(D_COLOR);
        } else if (party.equalsIgnoreCase("republican")) {
            return Color.parseColor(R_COLOR);
        } else {
            return Color.parseColor(I_COLOR);
        }
    }

    private String firstName;  // e.g. Kamala
    private String lastName;  // e.g. Harris

    private String representativeType;  // e.g. Representative, Senator
    private String party;  // e.g. Democrat, Republican, Independent

    private String state;  // e.g. CA
    private String district;  // e.g. Congressional District 13
    private String formattedAddress;

    private String url;  // e.g. https://www.harris.senate.gov
    private String phoneNumber;  // e.g. 202-224-3553
    private String contactForm;  // e.g. https://www.harris.senate.gov/contact

    private String id;  // e.g. H001075 (Bioguide ID)

    LegislatorInfo(String firstName, String lastName,
                          String representativeType, String party,
                          String state, String district, String formattedAddress,
                          String url, String phoneNumber, String contactForm,
                          String id) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.representativeType = representativeType;
        this.party = party;
        this.state = state;
        this.district = district;
        this.formattedAddress = formattedAddress;
        this.url = url;
        this.phoneNumber = phoneNumber;
        this.contactForm = contactForm;
        this.id = id;

    }

    @Override
    public boolean equals(Object object) {

        LegislatorInfo legislator = (LegislatorInfo) object;
        return this.id.equalsIgnoreCase(legislator.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /** Returns a capitalized version. */
    private String capitalizeString(String input) {
        return WordUtils.capitalizeFully(input);
    }

    private static final Map<String, String> STATE_MAP;
    static {
        STATE_MAP = new HashMap<String, String>();
        STATE_MAP.put("AL", "Alabama");
        STATE_MAP.put("AK", "Alaska");
        STATE_MAP.put("AB", "Alberta");
        STATE_MAP.put("AZ", "Arizona");
        STATE_MAP.put("AR", "Arkansas");
        STATE_MAP.put("BC", "British Columbia");
        STATE_MAP.put("CA", "California");
        STATE_MAP.put("CO", "Colorado");
        STATE_MAP.put("CT", "Connecticut");
        STATE_MAP.put("DE", "Delaware");
        STATE_MAP.put("DC", "District Of Columbia");
        STATE_MAP.put("FL", "Florida");
        STATE_MAP.put("GA", "Georgia");
        STATE_MAP.put("GU", "Guam");
        STATE_MAP.put("HI", "Hawaii");
        STATE_MAP.put("ID", "Idaho");
        STATE_MAP.put("IL", "Illinois");
        STATE_MAP.put("IN", "Indiana");
        STATE_MAP.put("IA", "Iowa");
        STATE_MAP.put("KS", "Kansas");
        STATE_MAP.put("KY", "Kentucky");
        STATE_MAP.put("LA", "Louisiana");
        STATE_MAP.put("ME", "Maine");
        STATE_MAP.put("MB", "Manitoba");
        STATE_MAP.put("MD", "Maryland");
        STATE_MAP.put("MA", "Massachusetts");
        STATE_MAP.put("MI", "Michigan");
        STATE_MAP.put("MN", "Minnesota");
        STATE_MAP.put("MS", "Mississippi");
        STATE_MAP.put("MO", "Missouri");
        STATE_MAP.put("MT", "Montana");
        STATE_MAP.put("NE", "Nebraska");
        STATE_MAP.put("NV", "Nevada");
        STATE_MAP.put("NB", "New Brunswick");
        STATE_MAP.put("NH", "New Hampshire");
        STATE_MAP.put("NJ", "New Jersey");
        STATE_MAP.put("NM", "New Mexico");
        STATE_MAP.put("NY", "New York");
        STATE_MAP.put("NF", "Newfoundland");
        STATE_MAP.put("NC", "North Carolina");
        STATE_MAP.put("ND", "North Dakota");
        STATE_MAP.put("NT", "Northwest Territories");
        STATE_MAP.put("NS", "Nova Scotia");
        STATE_MAP.put("NU", "Nunavut");
        STATE_MAP.put("OH", "Ohio");
        STATE_MAP.put("OK", "Oklahoma");
        STATE_MAP.put("ON", "Ontario");
        STATE_MAP.put("OR", "Oregon");
        STATE_MAP.put("PA", "Pennsylvania");
        STATE_MAP.put("PE", "Prince Edward Island");
        STATE_MAP.put("PR", "Puerto Rico");
        STATE_MAP.put("QC", "Quebec");
        STATE_MAP.put("RI", "Rhode Island");
        STATE_MAP.put("SK", "Saskatchewan");
        STATE_MAP.put("SC", "South Carolina");
        STATE_MAP.put("SD", "South Dakota");
        STATE_MAP.put("TN", "Tennessee");
        STATE_MAP.put("TX", "Texas");
        STATE_MAP.put("UT", "Utah");
        STATE_MAP.put("VT", "Vermont");
        STATE_MAP.put("VI", "Virgin Islands");
        STATE_MAP.put("VA", "Virginia");
        STATE_MAP.put("WA", "Washington");
        STATE_MAP.put("WV", "West Virginia");
        STATE_MAP.put("WI", "Wisconsin");
        STATE_MAP.put("WY", "Wyoming");
        STATE_MAP.put("YT", "Yukon Territory");
    }


}
