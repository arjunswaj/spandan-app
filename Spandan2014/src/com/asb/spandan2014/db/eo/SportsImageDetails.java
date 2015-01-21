package com.asb.spandan2014.db.eo;

public class SportsImageDetails {

  private int gameId;
  private String gameName;
  private String imageFileName;

  public SportsImageDetails(int gameId, String gameName, String imageFileName) {
    super();
    this.gameId = gameId;
    this.gameName = gameName;
    this.imageFileName = imageFileName;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public String getImageFileName() {
    return imageFileName;
  }

  public void setImageFileName(String imageFileName) {
    this.imageFileName = imageFileName;
  }

}
