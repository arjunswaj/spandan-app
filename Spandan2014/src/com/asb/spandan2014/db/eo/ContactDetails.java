package com.asb.spandan2014.db.eo;

public class ContactDetails {

  private int _id;
  private String contactName;
  private String contactNumber;
  private String contactEmail;
  private String contactImage;
  private String contactCourse;

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getContactImage() {
    return contactImage;
  }

  public void setContactImage(String contactImage) {
    this.contactImage = contactImage;
  }

  public String getContactCourse() {
    return contactCourse;
  }

  public void setContactCourse(String contactCourse) {
    this.contactCourse = contactCourse;
  }

  public ContactDetails(int _id, String contactName, String contactNumber,
      String contactEmail, String contactImage, String contactCourse) {
    super();
    this._id = _id;
    this.contactName = contactName;
    this.contactNumber = contactNumber;
    this.contactEmail = contactEmail;
    this.contactImage = contactImage;
    this.contactCourse = contactCourse;
  }

}
