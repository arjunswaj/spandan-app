package com.asb.spandan2014.db.eo;

public class SpocDetails {

  private int _id;
  private String spocName;
  private String spocNumber;
  private String spocEmail;
  private String spocImage;

  public SpocDetails(int _id, String spocName, String spocNumber,
      String spocEmail, String spocImage) {
    super();
    this._id = _id;
    this.spocName = spocName;
    this.spocNumber = spocNumber;
    this.spocEmail = spocEmail;
    this.spocImage = spocImage;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public String getSpocName() {
    return spocName;
  }

  public void setSpocName(String spocName) {
    this.spocName = spocName;
  }

  public String getSpocNumber() {
    return spocNumber;
  }

  public void setSpocNumber(String spocNumber) {
    this.spocNumber = spocNumber;
  }

  public String getSpocEmail() {
    return spocEmail;
  }

  public void setSpocEmail(String spocEmail) {
    this.spocEmail = spocEmail;
  }

  public String getSpocImage() {
    return spocImage;
  }

  public void setSpocImage(String spocImage) {
    this.spocImage = spocImage;
  }

}
