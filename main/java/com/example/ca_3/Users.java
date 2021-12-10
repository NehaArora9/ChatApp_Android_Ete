package com.example.ca_3;
//This is a java class i.e: Users
public class Users {
    String profilepic, userName, mail, password, userId, lastMessage, status;

    public Users(String profilepic, String userName, String mail, String password, String userId, String lastMessage, String status) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.status = status;
    }

    //By Getter And Setter (userId)
    public String getUserId()
    {
        return userId;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }


    //Create Empty Constructor
    public Users(){}
    //Create Constructor For SignUp
    public Users(String userName, String mail, String password) {

        this.userName = userName;
        this.mail = mail;
        this.password = password;

    }
//getter and setter method for Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
//Generate Getter And Setter

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }



    public String getLastMessage() {
        return lastMessage;
    }



}
