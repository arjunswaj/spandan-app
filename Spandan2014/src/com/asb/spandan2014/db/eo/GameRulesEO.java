package com.asb.spandan2014.db.eo;

public class GameRulesEO {

  private int _id;
  private String infoType;
  private String info;

  public GameRulesEO(int _id, String infoType, String info) {
    super();
    this._id = _id;
    this.infoType = infoType;
    this.info = info;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public String getInfoType() {
    return infoType;
  }

  public void setInfoType(String infoType) {
    this.infoType = infoType;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

}
