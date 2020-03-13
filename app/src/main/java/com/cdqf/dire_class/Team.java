package com.cdqf.dire_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Team {

    private Username username;

    private List<String> username1 = new CopyOnWriteArrayList<>();

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public List<String> getUsername1() {
        return username1;
    }

    public void setUsername1(List<String> username1) {
        this.username1 = username1;
    }

    public class Username {
        private String teams;
        private String nickname;
        private String username;
        private int user_id;
        private String phone;

        public String getTeams() {
            return teams;
        }

        public void setTeams(String teams) {
            this.teams = teams;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
